from __future__ import annotations

import json
from pathlib import Path
from typing import Dict, Tuple

import numpy as np
import pandas as pd


RISK_FREE_RATE_PERCENT = 6.0


def safe_read_csv(path: Path) -> pd.DataFrame:
    try:
        return pd.read_csv(path)
    except FileNotFoundError as exc:
        raise FileNotFoundError(f"Input file not found: {path}") from exc
    except pd.errors.EmptyDataError as exc:
        raise ValueError(f"Input file is empty or corrupted: {path}") from exc
    except pd.errors.ParserError as exc:
        raise ValueError(f"Input CSV parsing failed for {path}") from exc


def load_data(base_dir: Path) -> Dict[str, pd.DataFrame]:
    files = {
        "funds": "funds.csv",
        "investors": "investors.csv",
        "transactions": "transactions.csv",
        "nav_history": "nav_history.csv",
    }
    return {name: safe_read_csv(base_dir / filename) for name, filename in files.items()}


def clean_data(frames: Dict[str, pd.DataFrame]) -> Tuple[Dict[str, pd.DataFrame], Dict[str, Dict[str, int]]]:
    cleaned = {name: df.copy() for name, df in frames.items()}

    missing_before = {
        name: {col: int(count) for col, count in df.isna().sum().to_dict().items() if int(count) > 0}
        for name, df in cleaned.items()
    }

    for name in cleaned:
        cleaned[name] = cleaned[name].drop_duplicates().reset_index(drop=True)

    if "InvestorType" in cleaned["investors"].columns:
        cleaned["investors"]["InvestorType"] = cleaned["investors"]["InvestorType"].fillna("Retail")

    if "Date" in cleaned["nav_history"].columns:
        cleaned["nav_history"]["Date"] = pd.to_datetime(cleaned["nav_history"]["Date"], errors="coerce")
    if "PurchaseDate" in cleaned["transactions"].columns:
        cleaned["transactions"]["PurchaseDate"] = pd.to_datetime(cleaned["transactions"]["PurchaseDate"], errors="coerce")

    cleaned["nav_history"] = cleaned["nav_history"].sort_values(["FundID", "Date"]).reset_index(drop=True)
    cleaned["nav_history"]["NAV"] = cleaned["nav_history"].groupby("FundID")["NAV"].ffill()

    cleaned["nav_history"] = cleaned["nav_history"][cleaned["nav_history"]["NAV"] >= 0].copy()

    missing_after = {
        name: {col: int(count) for col, count in df.isna().sum().to_dict().items() if int(count) > 0}
        for name, df in cleaned.items()
    }

    return cleaned, {"missing_before": missing_before, "missing_after": missing_after}


def build_merged_dataframe(cleaned: Dict[str, pd.DataFrame]) -> pd.DataFrame:
    latest_nav = (
        cleaned["nav_history"]
        .sort_values(["FundID", "Date"])
        .groupby("FundID", as_index=False)
        .tail(1)[["FundID", "NAV"]]
        .rename(columns={"NAV": "LatestNAV"})
        .reset_index(drop=True)
    )

    merged = cleaned["transactions"].merge(cleaned["investors"], on="InvestorID", how="left")
    merged = merged.merge(cleaned["funds"], on="FundID", how="left")
    merged = merged.merge(latest_nav, on="FundID", how="left")

    merged["InvestmentAmount"] = merged["UnitsPurchased"] * merged["PurchaseNAV"]
    merged["CurrentValue"] = merged["UnitsPurchased"] * merged["LatestNAV"]
    merged["Profit"] = merged["CurrentValue"] - merged["InvestmentAmount"]

    merged["ROI%"] = np.where(
        merged["InvestmentAmount"] != 0,
        ((merged["CurrentValue"] - merged["InvestmentAmount"]) / merged["InvestmentAmount"]) * 100,
        np.nan,
    )

    merged["AbsoluteReturn"] = merged["CurrentValue"] - merged["InvestmentAmount"]
    merged["AnnualReturn%"] = merged["ROI%"]

    return merged


def numpy_nav_metrics(nav_history: pd.DataFrame) -> Dict[str, object]:
    nav_values = nav_history["NAV"].dropna().to_numpy(dtype=float)
    if nav_values.size == 0:
        return {
            "AverageNAV": np.nan,
            "MaximumNAV": np.nan,
            "MinimumNAV": np.nan,
            "VarianceNAV": np.nan,
            "StdDevNAV": np.nan,
            "RollingAverageWindow5": [],
        }

    if nav_values.size >= 5:
        rolling_avg = np.convolve(nav_values, np.ones(5) / 5, mode="valid")
        rolling_out = rolling_avg.round(4).tolist()
    else:
        rolling_out = []

    return {
        "AverageNAV": float(np.mean(nav_values)),
        "MaximumNAV": float(np.max(nav_values)),
        "MinimumNAV": float(np.min(nav_values)),
        "VarianceNAV": float(np.var(nav_values)),
        "StdDevNAV": float(np.std(nav_values)),
        "RollingAverageWindow5": rolling_out,
    }


def pandas_analysis(merged: pd.DataFrame) -> Dict[str, pd.DataFrame]:
    top_5_investors = (
        merged.groupby(["InvestorID", "InvestorName"], as_index=False)["InvestmentAmount"].sum()
        .sort_values("InvestmentAmount", ascending=False)
        .head(5)
    )

    fund_perf = (
        merged.groupby(["FundID", "FundName"], as_index=False)
        .agg(
            TotalInvestment=("InvestmentAmount", "sum"),
            TotalCurrentValue=("CurrentValue", "sum"),
            TotalProfit=("Profit", "sum"),
        )
    )
    fund_perf["ROI%"] = np.where(
        fund_perf["TotalInvestment"] != 0,
        ((fund_perf["TotalCurrentValue"] - fund_perf["TotalInvestment"]) / fund_perf["TotalInvestment"]) * 100,
        np.nan,
    )

    top_5_profitable_funds = fund_perf.sort_values("TotalProfit", ascending=False).head(5)
    worst_performing_fund = fund_perf.sort_values("ROI%", ascending=True).head(1)

    highest_nav_fund = (
        merged.groupby(["FundID", "FundName"], as_index=False)["LatestNAV"].max()
        .sort_values("LatestNAV", ascending=False)
        .head(1)
    )
    lowest_nav_fund = (
        merged.groupby(["FundID", "FundName"], as_index=False)["LatestNAV"].max()
        .sort_values("LatestNAV", ascending=True)
        .head(1)
    )

    return {
        "top_5_investors": top_5_investors,
        "top_5_profitable_funds": top_5_profitable_funds,
        "worst_performing_fund": worst_performing_fund,
        "highest_nav_fund": highest_nav_fund,
        "lowest_nav_fund": lowest_nav_fund,
        "fund_performance": fund_perf,
    }


def groupby_summaries(merged: pd.DataFrame) -> Dict[str, pd.DataFrame]:
    by_category = (
        merged.groupby("Category", as_index=False)
        .agg(
            AverageROI=("ROI%", "mean"),
            AverageNAV=("LatestNAV", "mean"),
            TotalInvestment=("InvestmentAmount", "sum"),
        )
        .sort_values("AverageROI", ascending=False)
    )

    by_amc = (
        merged.groupby("AMC", as_index=False)
        .agg(
            NumberOfFunds=("FundID", pd.Series.nunique),
            AverageNAV=("LatestNAV", "mean"),
            TotalInvestment=("InvestmentAmount", "sum"),
        )
        .sort_values("TotalInvestment", ascending=False)
    )

    by_state = (
        merged.groupby("State", as_index=False)
        .agg(
            NumberOfInvestors=("InvestorID", pd.Series.nunique),
            TotalInvestment=("InvestmentAmount", "sum"),
            AverageROI=("ROI%", "mean"),
        )
        .sort_values("TotalInvestment", ascending=False)
    )

    by_investor_type = (
        merged.groupby("InvestorType", as_index=False)
        .agg(
            TotalInvestment=("InvestmentAmount", "sum"),
            AverageProfit=("Profit", "mean"),
        )
        .sort_values("TotalInvestment", ascending=False)
    )

    return {
        "by_category": by_category,
        "by_amc": by_amc,
        "by_state": by_state,
        "by_investor_type": by_investor_type,
    }


def detect_issues(cleaned: Dict[str, pd.DataFrame]) -> Dict[str, pd.DataFrame]:
    today = pd.Timestamp.today().normalize()

    nav = cleaned["nav_history"]
    transactions = cleaned["transactions"]
    funds = cleaned["funds"]
    investors = cleaned["investors"]

    duplicate_nav_records = nav[nav.duplicated(subset=["FundID", "Date"], keep=False)].copy()
    negative_nav = nav[nav["NAV"] < 0].copy()

    future_dates_nav = nav[nav["Date"] > today].copy()
    future_dates_txn = transactions[transactions["PurchaseDate"] > today].copy()
    future_dates = pd.concat([future_dates_nav, future_dates_txn], ignore_index=True, sort=False)

    missing_fund_ids = transactions[~transactions["FundID"].isin(funds["FundID"])].copy()
    missing_investor_ids = transactions[~transactions["InvestorID"].isin(investors["InvestorID"])].copy()
    invalid_purchase_nav = transactions[transactions["PurchaseNAV"] < 0].copy()

    return {
        "duplicate_nav_records": duplicate_nav_records,
        "negative_nav": negative_nav,
        "future_dates": future_dates,
        "missing_fund_ids": missing_fund_ids,
        "missing_investor_ids": missing_investor_ids,
        "invalid_purchase_nav": invalid_purchase_nav,
    }


def finance_metrics(merged: pd.DataFrame, nav_history: pd.DataFrame) -> Dict[str, float]:
    total_investment = float(merged["InvestmentAmount"].sum())
    total_current_value = float(merged["CurrentValue"].sum())

    roi_percent = ((total_current_value - total_investment) / total_investment * 100) if total_investment else np.nan
    absolute_return = total_current_value - total_investment

    annual_return_percent = roi_percent

    nav_values = nav_history["NAV"].dropna().to_numpy(dtype=float)
    volatility = float(np.std(nav_values)) if nav_values.size else np.nan

    sharpe_ratio = (
        (annual_return_percent - RISK_FREE_RATE_PERCENT) / volatility
        if pd.notna(annual_return_percent) and pd.notna(volatility) and volatility != 0
        else np.nan
    )

    return {
        "ROI%": float(roi_percent) if pd.notna(roi_percent) else np.nan,
        "AbsoluteReturn": float(absolute_return),
        "AnnualReturn%": float(annual_return_percent) if pd.notna(annual_return_percent) else np.nan,
        "Volatility": float(volatility) if pd.notna(volatility) else np.nan,
        "SharpeRatio": float(sharpe_ratio) if pd.notna(sharpe_ratio) else np.nan,
    }


def export_reports(
    base_dir: Path,
    analysis: Dict[str, pd.DataFrame],
    groupby_data: Dict[str, pd.DataFrame],
    merged: pd.DataFrame,
) -> None:
    output_dir = base_dir / "final_reports"
    output_dir.mkdir(parents=True, exist_ok=True)

    top_funds_path = output_dir / "TopFunds.xlsx"
    with pd.ExcelWriter(top_funds_path, engine="openpyxl") as writer:
        analysis["top_5_profitable_funds"].to_excel(writer, sheet_name="TopProfitableFunds", index=False)
        analysis["fund_performance"].sort_values("ROI%", ascending=False).to_excel(
            writer, sheet_name="FundPerformance", index=False
        )
        analysis["highest_nav_fund"].to_excel(writer, sheet_name="HighestNAVFund", index=False)
        analysis["worst_performing_fund"].to_excel(writer, sheet_name="WorstFund", index=False)

    investor_summary_path = output_dir / "InvestorSummary.xlsx"
    with pd.ExcelWriter(investor_summary_path, engine="openpyxl") as writer:
        analysis["top_5_investors"].to_excel(writer, sheet_name="Top5Investors", index=False)
        merged[
            [
                "InvestorID",
                "InvestorName",
                "State",
                "InvestorType",
                "InvestmentAmount",
                "CurrentValue",
                "Profit",
                "ROI%",
            ]
        ].to_excel(writer, sheet_name="InvestorWisePortfolio", index=False)
        groupby_data["by_state"].to_excel(writer, sheet_name="StateWise", index=False)

    category_summary_path = output_dir / "CategorySummary.csv"
    groupby_data["by_category"].to_csv(category_summary_path, index=False)


def print_expected_outputs(
    analysis: Dict[str, pd.DataFrame],
    groupby_data: Dict[str, pd.DataFrame],
) -> None:
    top_funds = analysis["fund_performance"].sort_values("ROI%", ascending=False).head(5)
    print("\nTop Performing Funds:")
    print(top_funds[["FundID", "FundName", "ROI%", "TotalProfit"]].to_string(index=False))

    print("\nHighest ROI Fund:")
    print(top_funds.head(1)[["FundID", "FundName", "ROI%"]].to_string(index=False))

    print("\nHighest Profit Fund:")
    print(analysis["top_5_profitable_funds"].head(1)[["FundID", "FundName", "TotalProfit"]].to_string(index=False))

    print("\nHighest NAV Fund:")
    print(analysis["highest_nav_fund"].to_string(index=False))

    print("\nWorst Performing Fund (Lowest ROI):")
    print(analysis["worst_performing_fund"][["FundID", "FundName", "ROI%"]].to_string(index=False))

    print("\nState-wise Investment:")
    print(groupby_data["by_state"][["State", "TotalInvestment"]].to_string(index=False))

    print("\nAMC-wise Investment:")
    print(groupby_data["by_amc"][["AMC", "TotalInvestment"]].to_string(index=False))

    print("\nCategory-wise ROI:")
    print(groupby_data["by_category"][["Category", "AverageROI"]].to_string(index=False))


def run(base_dir: Path) -> None:
    output_dir = base_dir / "final_reports"
    output_dir.mkdir(parents=True, exist_ok=True)

    frames = load_data(base_dir)
    cleaned, missing_snapshot = clean_data(frames)

    merged = build_merged_dataframe(cleaned)

    required_columns = [
        "InvestorName",
        "FundName",
        "Category",
        "AMC",
        "State",
        "UnitsPurchased",
        "PurchaseNAV",
        "LatestNAV",
    ]
    merged_required = merged[required_columns].copy()

    np_metrics = numpy_nav_metrics(cleaned["nav_history"])
    analysis = pandas_analysis(merged)
    grouped = groupby_summaries(merged)
    issues = detect_issues(cleaned)
    fin_metrics = finance_metrics(merged, cleaned["nav_history"])

    export_reports(base_dir, analysis, grouped, merged)

    diagnostics = {
        "missing_values": missing_snapshot,
        "numpy_nav_metrics": np_metrics,
        "finance_metrics": fin_metrics,
        "issue_counts": {key: int(len(df)) for key, df in issues.items()},
        "merged_required_preview_rows": int(len(merged_required.head(10))),
    }

    with open(output_dir / "analysis_summary.json", "w", encoding="utf-8") as f:
        json.dump(diagnostics, f, indent=2, default=str)

    print_expected_outputs(analysis, grouped)
    print("\nReports generated:")
    print(f"- {output_dir / 'TopFunds.xlsx'}")
    print(f"- {output_dir / 'InvestorSummary.xlsx'}")
    print(f"- {output_dir / 'CategorySummary.csv'}")
    print(f"- {output_dir / 'analysis_summary.json'}")


def main() -> None:
    base_dir = Path(__file__).resolve().parent
    try:
        run(base_dir)
    except Exception as exc:
        print(f"Execution failed: {exc}")
        raise


if __name__ == "__main__":
    main()
