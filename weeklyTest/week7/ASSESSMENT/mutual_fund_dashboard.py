import json
import logging
from pathlib import Path
from typing import Dict, Tuple

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


class FundPortfolio:
    def __init__(self, base_dir: Path, output_dir: str = "final-reports"):
        self.base_dir = Path(base_dir)
        self.output_dir = self.base_dir / output_dir
        self.charts_dir = self.output_dir / "charts"
        self.log_file = self.output_dir / "execution.log"

        self.investors = pd.DataFrame()
        self.funds = pd.DataFrame()
        self.transactions = pd.DataFrame()
        self.nav_history = pd.DataFrame()

        self.merged = pd.DataFrame()
        self.holdings = pd.DataFrame()

        self._setup_directories()
        self._setup_logger()

    def _setup_directories(self) -> None:
        self.output_dir.mkdir(parents=True, exist_ok=True)
        self.charts_dir.mkdir(parents=True, exist_ok=True)

    def _setup_logger(self) -> None:
        logging.basicConfig(
            filename=str(self.log_file),
            level=logging.INFO,
            format="%(asctime)s | %(levelname)s | %(message)s",
        )
        logging.info("Logger initialized.")

    def load_csv(self, filename: str) -> pd.DataFrame:
        file_path = self.base_dir / filename
        try:
            df = pd.read_csv(file_path)
            logging.info("Loaded %s with %d rows.", filename, len(df))
            return df
        except FileNotFoundError:
            logging.error("Missing file: %s", filename)
            raise
        except pd.errors.EmptyDataError:
            logging.error("Empty/corrupted file: %s", filename)
            raise
        except Exception as exc:
            logging.error("Unexpected error while loading %s: %s", filename, exc)
            raise

    @staticmethod
    def _normalize_id_series(series: pd.Series, prefix: str) -> pd.Series:
        numeric = (
            series.astype(str)
            .str.extract(r"(\d+)", expand=False)
            .fillna("0")
            .astype(int)
            .astype(str)
            .str.zfill(3)
        )
        return prefix + numeric

    def _ensure_columns(self) -> None:
        if "InvestorID" in self.investors.columns:
            self.investors["InvestorID"] = self._normalize_id_series(self.investors["InvestorID"], "I")

        if "FundID" in self.funds.columns:
            self.funds["FundID"] = self._normalize_id_series(self.funds["FundID"], "F")

        rename_map = {
            "UnitsPurchased": "Units",
            "PurchaseNAV": "NAV",
            "PurchaseDate": "TransactionDate",
        }
        self.transactions = self.transactions.rename(columns=rename_map)

        if "InvestorID" in self.transactions.columns:
            self.transactions["InvestorID"] = self._normalize_id_series(self.transactions["InvestorID"], "I")

        if "FundID" in self.transactions.columns:
            self.transactions["FundID"] = self._normalize_id_series(self.transactions["FundID"], "F")

        if "FundID" in self.nav_history.columns:
            self.nav_history["FundID"] = self._normalize_id_series(self.nav_history["FundID"], "F")

        if "AnnualIncome" not in self.investors.columns:
            type_income_map = {
                "corporate": 3_500_000,
                "retail": 900_000,
            }
            if "InvestorType" in self.investors.columns:
                self.investors["AnnualIncome"] = (
                    self.investors["InvestorType"]
                    .astype(str)
                    .str.lower()
                    .map(type_income_map)
                )
            else:
                self.investors["AnnualIncome"] = np.nan

        if "RiskProfile" not in self.investors.columns:
            if "InvestorType" in self.investors.columns:
                self.investors["RiskProfile"] = np.where(
                    self.investors["InvestorType"].astype(str).str.lower().eq("corporate"),
                    "High",
                    "Moderate",
                )
            else:
                self.investors["RiskProfile"] = "Moderate"

        if "ExpenseRatio" not in self.funds.columns:
            if "Category" in self.funds.columns:
                category_expense_map = {
                    "equity": 1.20,
                    "debt": 0.70,
                    "hybrid": 1.00,
                    "sectoral": 1.35,
                    "etf": 0.25,
                    "international": 1.45,
                }
                self.funds["ExpenseRatio"] = (
                    self.funds["Category"].astype(str).str.lower().map(category_expense_map)
                )
            else:
                self.funds["ExpenseRatio"] = np.nan

        if "TransactionType" not in self.transactions.columns:
            self.transactions["TransactionType"] = "Purchase"

        for required in ["Units", "NAV"]:
            if required not in self.transactions.columns:
                self.transactions[required] = np.nan

        if "Amount" not in self.transactions.columns:
            self.transactions["Amount"] = self.transactions["Units"] * self.transactions["NAV"]

    def clean_data(self) -> None:
        logging.info("Starting data cleaning.")

        self._ensure_columns()

        self.investors["AnnualIncome"] = pd.to_numeric(self.investors["AnnualIncome"], errors="coerce")
        self.funds["ExpenseRatio"] = pd.to_numeric(self.funds["ExpenseRatio"], errors="coerce")
        self.transactions["NAV"] = pd.to_numeric(self.transactions["NAV"], errors="coerce")
        self.transactions["Amount"] = pd.to_numeric(self.transactions["Amount"], errors="coerce")
        self.transactions["Units"] = pd.to_numeric(self.transactions["Units"], errors="coerce")
        self.nav_history["NAV"] = pd.to_numeric(self.nav_history["NAV"], errors="coerce")

        self.transactions["TransactionDate"] = pd.to_datetime(
            self.transactions["TransactionDate"], errors="coerce"
        )
        self.nav_history["Date"] = pd.to_datetime(self.nav_history["Date"], errors="coerce")

        annual_income_median = self.investors["AnnualIncome"].median()
        if pd.isna(annual_income_median):
            annual_income_median = 1_000_000
        self.investors["AnnualIncome"] = self.investors["AnnualIncome"].fillna(annual_income_median)

        expense_ratio_mean = self.funds["ExpenseRatio"].mean()
        if pd.isna(expense_ratio_mean):
            expense_ratio_mean = 1.0
        self.funds["ExpenseRatio"] = self.funds["ExpenseRatio"].fillna(expense_ratio_mean)

        self.investors["RiskProfile"] = self.investors["RiskProfile"].fillna("Moderate")

        self.nav_history = self.nav_history.sort_values(["FundID", "Date"])
        self.nav_history["NAV"] = self.nav_history.groupby("FundID")["NAV"].ffill()

        self.transactions = self.transactions.drop_duplicates(subset=["TransactionID"])

        amount_99 = self.transactions["Amount"].quantile(0.99)
        self.transactions = self.transactions[self.transactions["Amount"] <= amount_99].copy()

        self.nav_history["NAVChange"] = self.nav_history.groupby("FundID")["NAV"].diff()
        nav_change_std = self.nav_history["NAVChange"].std(skipna=True)
        if pd.notna(nav_change_std) and nav_change_std > 0:
            nav_threshold = 3 * nav_change_std
            keep_mask = (
                self.nav_history["NAVChange"].isna()
                | (self.nav_history["NAVChange"].abs() <= nav_threshold)
            )
            self.nav_history = self.nav_history[keep_mask].copy()

        self.nav_history.drop(columns=["NAVChange"], inplace=True, errors="ignore")

        self._export_cleaned_data()
        logging.info("Data cleaning complete.")

    def _export_cleaned_data(self) -> None:
        self.investors.to_csv(self.output_dir / "cleaned_investors.csv", index=False)
        self.funds.to_csv(self.output_dir / "cleaned_funds.csv", index=False)
        self.transactions.to_csv(self.output_dir / "cleaned_transactions.csv", index=False)
        self.nav_history.to_csv(self.output_dir / "cleaned_nav_history.csv", index=False)

    def merge_data(self) -> None:
        logging.info("Merging datasets.")

        latest_nav = self._latest_nav_with_fallback()

        txn_fund = self.transactions.merge(self.funds, on="FundID", how="left")
        txn_fund_nav = txn_fund.merge(latest_nav, on="FundID", how="left")
        self.merged = txn_fund_nav.merge(self.investors, on="InvestorID", how="left")

        self.merged.to_csv(self.output_dir / "merged_data.csv", index=False)
        logging.info("Merged dataset exported.")

    def _latest_nav_with_fallback(self) -> pd.DataFrame:
        # Primary source: latest NAV from nav_history.
        nav_latest = (
            self.nav_history.sort_values("Date")
            .groupby("FundID", as_index=False)
            .tail(1)[["FundID", "NAV"]]
            .rename(columns={"NAV": "LatestNAV"})
        )

        # Fallback source: most recent transaction NAV when NAV history is unavailable.
        txn_nav_latest = (
            self.transactions.sort_values("TransactionDate")
            .groupby("FundID", as_index=False)
            .tail(1)[["FundID", "NAV"]]
            .rename(columns={"NAV": "TxnLatestNAV"})
        )

        all_funds = self.funds[["FundID"]].drop_duplicates()
        latest_nav = all_funds.merge(nav_latest, on="FundID", how="left").merge(
            txn_nav_latest, on="FundID", how="left"
        )
        latest_nav["LatestNAV"] = latest_nav["LatestNAV"].fillna(latest_nav["TxnLatestNAV"])
        latest_nav["LatestNAV"] = latest_nav["LatestNAV"].fillna(0)

        return latest_nav[["FundID", "LatestNAV"]]

    def _compute_fund_returns(self) -> pd.DataFrame:
        nav_sorted = self.nav_history.sort_values(["FundID", "Date"])
        first_nav = nav_sorted.groupby("FundID", as_index=False).first()[["FundID", "NAV"]]
        last_nav = nav_sorted.groupby("FundID", as_index=False).last()[["FundID", "NAV"]]

        fund_returns = first_nav.merge(last_nav, on="FundID", suffixes=("First", "Last"))
        fund_returns["FundReturnPct"] = (
            (fund_returns["NAVLast"] - fund_returns["NAVFirst"]) / fund_returns["NAVFirst"]
        ) * 100
        return fund_returns

    def calculate_numpy_metrics(self) -> Dict[str, float]:
        mean_investment_amount = float(np.nanmean(self.transactions["Amount"].to_numpy()))
        median_investor_income = float(np.nanmedian(self.investors["AnnualIncome"].to_numpy()))
        std_nav = float(np.nanstd(self.nav_history["NAV"].to_numpy(), ddof=1))

        fund_returns_df = self._compute_fund_returns()
        returns_array = fund_returns_df["FundReturnPct"].to_numpy()
        p90 = float(np.nanpercentile(returns_array, 90))
        p95 = float(np.nanpercentile(returns_array, 95))

        investor_amount = (
            self.transactions.groupby("InvestorID", as_index=False)["Amount"].sum()
            .merge(self.investors[["InvestorID", "AnnualIncome"]], on="InvestorID", how="left")
            .dropna(subset=["AnnualIncome", "Amount"])
        )

        if len(investor_amount) > 1 and investor_amount["AnnualIncome"].nunique() > 1:
            correlation = float(
                np.corrcoef(
                    investor_amount["AnnualIncome"].to_numpy(),
                    investor_amount["Amount"].to_numpy(),
                )[0, 1]
            )
        else:
            correlation = float("nan")

        avg_daily_nav = float(self.nav_history.groupby("Date")["NAV"].mean().mean())

        metrics = {
            "MeanInvestmentAmount": mean_investment_amount,
            "MedianInvestorIncome": median_investor_income,
            "StdDevNAV": std_nav,
            "Percentile90FundReturns": p90,
            "Percentile95FundReturns": p95,
            "IncomeInvestmentCorrelation": correlation,
            "AverageDailyNAV": avg_daily_nav,
        }

        with open(self.output_dir / "numpy_metrics.json", "w", encoding="utf-8") as file:
            json.dump(metrics, file, indent=2)

        logging.info("NumPy metrics calculated and exported.")
        return metrics

    def _signed_units_and_amount(self) -> pd.DataFrame:
        df = self.transactions.copy()
        txn_type = df["TransactionType"].astype(str).str.lower().str.strip()
        sign = np.where(txn_type.eq("redemption"), -1, 1)
        df["SignedUnits"] = df["Units"] * sign
        df["SignedAmount"] = df["Amount"] * sign
        return df

    def build_holdings(self) -> None:
        latest_nav = self._latest_nav_with_fallback()

        signed_txn = self._signed_units_and_amount()

        holdings = (
            signed_txn.groupby(["InvestorID", "FundID"], as_index=False)
            .agg(NetUnits=("SignedUnits", "sum"), NetInvested=("SignedAmount", "sum"))
            .merge(latest_nav, on="FundID", how="left")
            .merge(self.funds[["FundID", "FundName", "Category", "ExpenseRatio"]], on="FundID", how="left")
            .merge(self.investors[["InvestorID", "InvestorName", "AnnualIncome", "RiskProfile"]], on="InvestorID", how="left")
        )

        holdings["LatestNAV"] = holdings["LatestNAV"].fillna(0)
        holdings["CurrentValue"] = holdings["NetUnits"] * holdings["LatestNAV"]

        self.holdings = holdings
        self.holdings.to_csv(self.output_dir / "holdings_snapshot.csv", index=False)
        logging.info("Holdings built and exported.")

    def identify_investors(self) -> Tuple[pd.DataFrame, pd.DataFrame]:
        investor_summary = (
            self.holdings.groupby("InvestorID", as_index=False)
            .agg(PortfolioValue=("CurrentValue", "sum"), NetInvested=("NetInvested", "sum"))
            .merge(
                self.investors[["InvestorID", "InvestorName", "AnnualIncome", "RiskProfile"]],
                on="InvestorID",
                how="left",
            )
        )

        txn_counts = (
            self.transactions.groupby("InvestorID", as_index=False)["TransactionID"]
            .count()
            .rename(columns={"TransactionID": "TransactionCount"})
        )
        investor_summary = investor_summary.merge(txn_counts, on="InvestorID", how="left")
        investor_summary["TransactionCount"] = investor_summary["TransactionCount"].fillna(0)

        top20 = investor_summary.sort_values("PortfolioValue", ascending=False).head(20)

        filtered = investor_summary[
            (investor_summary["PortfolioValue"] > 1_000_000)
            & (investor_summary["RiskProfile"].str.lower().eq("high"))
            & (investor_summary["TransactionCount"] > 10)
            & (investor_summary["AnnualIncome"] > 1_500_000)
        ].copy()

        top20.to_csv(self.output_dir / "top20_investors.csv", index=False)
        filtered.to_csv(self.output_dir / "high_value_high_risk_investors.csv", index=False)

        logging.info("Investor identification reports exported.")
        return top20, filtered

    def fund_analysis(self) -> Dict[str, object]:
        fund_returns = self._compute_fund_returns().merge(
            self.funds[["FundID", "FundName", "Category", "ExpenseRatio"]],
            on="FundID",
            how="left",
        )

        signed_txn = self._signed_units_and_amount()
        latest_nav = self._latest_nav_with_fallback()

        aum = (
            signed_txn.groupby("FundID", as_index=False)["SignedUnits"]
            .sum()
            .merge(latest_nav, on="FundID", how="left")
        )
        aum["AUM"] = aum["SignedUnits"] * aum["LatestNAV"].fillna(0)

        popularity = (
            self.transactions.groupby("FundID", as_index=False)["InvestorID"]
            .nunique()
            .rename(columns={"InvestorID": "UniqueInvestors"})
        )

        analysis_df = (
            fund_returns.merge(aum[["FundID", "AUM"]], on="FundID", how="left")
            .merge(popularity, on="FundID", how="left")
            .sort_values("FundReturnPct", ascending=False)
        )

        analysis_df.to_csv(self.output_dir / "fund_rankings.csv", index=False)

        best = analysis_df.iloc[0] if not analysis_df.empty else pd.Series(dtype=object)
        worst = analysis_df.iloc[-1] if not analysis_df.empty else pd.Series(dtype=object)

        if self.funds["ExpenseRatio"].notna().any():
            highest_expense = self.funds.loc[self.funds["ExpenseRatio"].idxmax()]
            highest_expense_name = highest_expense.get("FundName", None)
        else:
            highest_expense_name = None

        highest_aum = analysis_df.loc[analysis_df["AUM"].idxmax()] if not analysis_df.empty else pd.Series(dtype=object)
        most_popular = analysis_df.loc[analysis_df["UniqueInvestors"].idxmax()] if not analysis_df.empty else pd.Series(dtype=object)

        results = {
            "BestPerformingFund": best.get("FundName", None),
            "WorstPerformingFund": worst.get("FundName", None),
            "HighestExpenseRatioFund": highest_expense_name,
            "HighestAUMFund": highest_aum.get("FundName", None),
            "MostPopularFund": most_popular.get("FundName", None),
        }

        logging.info("Fund analysis completed.")
        return results

    def _calculate_simplified_sharpe_ratio(self) -> float:
        nav = self.nav_history.sort_values(["FundID", "Date"]).copy()
        nav["DailyReturn"] = nav.groupby("FundID")["NAV"].pct_change()

        daily_portfolio_returns = nav.groupby("Date")["DailyReturn"].mean().dropna()
        if len(daily_portfolio_returns) < 2:
            return 0.0

        avg_return = daily_portfolio_returns.mean()
        std_return = daily_portfolio_returns.std(ddof=1)
        if std_return == 0 or pd.isna(std_return):
            return 0.0

        return float((avg_return / std_return) * np.sqrt(252))

    def _category_returns(self) -> pd.DataFrame:
        nav_sorted = self.nav_history.sort_values(["FundID", "Date"])
        first_nav = nav_sorted.groupby("FundID", as_index=False).first()[["FundID", "NAV"]]
        last_nav = nav_sorted.groupby("FundID", as_index=False).last()[["FundID", "NAV"]]

        fund_returns = first_nav.merge(last_nav, on="FundID", suffixes=("First", "Last"))
        fund_returns["FundReturnPct"] = np.where(
            fund_returns["NAVFirst"] != 0,
            (fund_returns["NAVLast"] - fund_returns["NAVFirst"]) / fund_returns["NAVFirst"] * 100,
            0.0,
        )

        cat_returns = (
            fund_returns.merge(self.funds[["FundID", "Category"]], on="FundID", how="left")
            .groupby("Category", as_index=False)["FundReturnPct"]
            .mean()
            .rename(columns={"FundReturnPct": "ReturnPct"})
        )
        return cat_returns

    def calculate_finance_metrics(self) -> Dict[str, object]:
        if self.holdings.empty:
            self.build_holdings()

        total_portfolio_value = float(self.holdings["CurrentValue"].sum())
        total_net_invested = float(self.holdings["NetInvested"].sum())

        if total_net_invested == 0:
            portfolio_return_pct = 0.0
            absolute_return = 0.0
        else:
            absolute_return = total_portfolio_value - total_net_invested
            portfolio_return_pct = (absolute_return / total_net_invested) * 100

        start_date = self.transactions["TransactionDate"].min()
        end_date = self.transactions["TransactionDate"].max()
        years = max((end_date - start_date).days / 365.25, 1 / 365.25)

        if total_net_invested > 0 and total_portfolio_value > 0:
            cagr = ((total_portfolio_value / total_net_invested) ** (1 / years) - 1) * 100
            annualized_return = cagr
        else:
            cagr = 0.0
            annualized_return = 0.0

        investor_fund_counts = self.holdings[self.holdings["NetUnits"] > 0].groupby("InvestorID")["FundID"].nunique()
        total_funds = max(self.funds["FundID"].nunique(), 1)
        diversification_score = float((investor_fund_counts / total_funds).mean() * 100) if not investor_fund_counts.empty else 0.0

        as_of_date = self.nav_history["Date"].max()
        txn_age_days = (as_of_date - self.transactions["TransactionDate"]).dt.days
        avg_holding_period = float(txn_age_days.mean()) if not txn_age_days.empty else 0.0

        self.holdings["ExpenseImpact"] = self.holdings["CurrentValue"] * self.holdings["ExpenseRatio"].fillna(0) / 100
        expense_ratio_impact = float(self.holdings["ExpenseImpact"].sum())

        sharpe_ratio = self._calculate_simplified_sharpe_ratio()

        category_investment = self.holdings.groupby("Category", as_index=False)["NetInvested"].sum()
        cat_total = category_investment["NetInvested"].sum()
        category_investment["CategoryInvestmentPct"] = (
            (category_investment["NetInvested"] / cat_total * 100) if cat_total != 0 else 0.0
        )

        fund_allocation = self.holdings.groupby(["FundID", "FundName"], as_index=False)["CurrentValue"].sum()
        total_current = fund_allocation["CurrentValue"].sum()
        fund_allocation["FundAllocationPct"] = (
            (fund_allocation["CurrentValue"] / total_current * 100) if total_current != 0 else 0.0
        )

        investor_pl = (
            self.holdings.groupby(["InvestorID", "InvestorName"], as_index=False)
            .agg(CurrentValue=("CurrentValue", "sum"), NetInvested=("NetInvested", "sum"))
        )
        investor_pl["ProfitLoss"] = investor_pl["CurrentValue"] - investor_pl["NetInvested"]

        investor_pl.to_csv(self.output_dir / "investor_profit_loss.csv", index=False)
        category_investment.to_csv(self.output_dir / "category_investment_pct.csv", index=False)
        fund_allocation.to_csv(self.output_dir / "fund_allocation_pct.csv", index=False)

        metrics = {
            "TotalPortfolioValue": total_portfolio_value,
            "PortfolioReturnPct": portfolio_return_pct,
            "CAGR": cagr,
            "AbsoluteReturn": absolute_return,
            "AnnualizedReturn": annualized_return,
            "PortfolioDiversificationScore": diversification_score,
            "AverageHoldingPeriodDays": avg_holding_period,
            "ExpenseRatioImpact": expense_ratio_impact,
            "SharpeRatioSimplified": sharpe_ratio,
        }

        with open(self.output_dir / "portfolio_metrics.json", "w", encoding="utf-8") as file:
            json.dump(metrics, file, indent=2)

        logging.info("Finance metrics calculated and exported.")
        return metrics

    def create_visualizations(self) -> None:
        logging.info("Generating charts.")

        fund_alloc = pd.read_csv(self.output_dir / "fund_allocation_pct.csv")
        top20 = pd.read_csv(self.output_dir / "top20_investors.csv")

        plt.figure(figsize=(10, 7))
        pie_data = fund_alloc.sort_values("CurrentValue", ascending=False).head(10)
        plt.pie(pie_data["CurrentValue"], labels=pie_data["FundName"], autopct="%1.1f%%", startangle=140)
        plt.title("Portfolio Allocation")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "portfolio_allocation_pie.png", dpi=150)
        plt.close()

        plt.figure(figsize=(12, 6))
        bar_data = fund_alloc.sort_values("CurrentValue", ascending=False)
        plt.bar(bar_data["FundName"], bar_data["CurrentValue"])
        plt.title("Fund-wise Investment")
        plt.xlabel("Fund")
        plt.ylabel("Current Value")
        plt.xticks(rotation=70, ha="right")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "fund_wise_investment_bar.png", dpi=150)
        plt.close()

        monthly = (
            self.transactions.assign(Month=self.transactions["TransactionDate"].dt.to_period("M").astype(str))
            .groupby("Month", as_index=False)["Amount"]
            .sum()
        )
        plt.figure(figsize=(10, 5))
        plt.plot(monthly["Month"], monthly["Amount"], marker="o")
        plt.title("Monthly Investment Trend")
        plt.xlabel("Month")
        plt.ylabel("Investment Amount")
        plt.xticks(rotation=45, ha="right")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "monthly_investment_trend_line.png", dpi=150)
        plt.close()

        category_returns = self._category_returns()
        category_returns.to_csv(self.output_dir / "category_returns.csv", index=False)
        plt.figure(figsize=(10, 5))
        plt.bar(category_returns["Category"], category_returns["ReturnPct"])
        plt.title("Category-wise Returns")
        plt.xlabel("Category")
        plt.ylabel("Return %")
        plt.xticks(rotation=45, ha="right")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "category_wise_returns_bar.png", dpi=150)
        plt.close()

        # Plot fund-wise NAV movement so trend is not hidden by averaging.
        nav_plot_df = self.nav_history.merge(
            self.funds[["FundID", "FundName"]], on="FundID", how="left"
        )
        nav_pivot = nav_plot_df.pivot_table(
            index="Date", columns="FundName", values="NAV", aggfunc="mean"
        ).sort_index()

        plt.figure(figsize=(12, 6))
        max_lines = 10
        for fund_name in nav_pivot.columns[:max_lines]:
            plt.plot(nav_pivot.index, nav_pivot[fund_name], marker="o", linewidth=1.5, label=fund_name)

        avg_nav = nav_pivot.mean(axis=1)
        plt.plot(avg_nav.index, avg_nav.values, linestyle="--", linewidth=2.5, color="black", label="Average NAV")

        plt.title("NAV Movement (Fund-wise)")
        plt.xlabel("Date")
        plt.ylabel("NAV")
        plt.xticks(rotation=45, ha="right")
        plt.legend(fontsize=8, loc="best")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "nav_movement_line.png", dpi=150)
        plt.close()

        top10 = top20.sort_values("PortfolioValue", ascending=False).head(10).sort_values("PortfolioValue", ascending=True)
        plt.figure(figsize=(10, 6))
        plt.barh(top10["InvestorName"], top10["PortfolioValue"])
        plt.title("Top 10 Investors by Portfolio Value")
        plt.xlabel("Portfolio Value")
        plt.ylabel("Investor")
        plt.tight_layout()
        plt.savefig(self.charts_dir / "top10_investors_horizontal_bar.png", dpi=150)
        plt.close()

        logging.info("All charts generated.")

    def export_execution_summary(
        self,
        numpy_metrics: Dict[str, float],
        fund_results: Dict[str, object],
        finance_metrics: Dict[str, object],
    ) -> None:
        summary_path = self.output_dir / "execution_summary.txt"
        lines = [
            "Mutual Fund Performance Dashboard - Execution Summary",
            "=" * 60,
            "",
            "NumPy Metrics:",
        ]
        for key, value in numpy_metrics.items():
            lines.append(f"- {key}: {value}")

        lines.append("")
        lines.append("Fund Analysis:")
        for key, value in fund_results.items():
            lines.append(f"- {key}: {value}")

        lines.append("")
        lines.append("Portfolio Finance Metrics:")
        for key, value in finance_metrics.items():
            lines.append(f"- {key}: {value}")

        summary_path.write_text("\n".join(lines), encoding="utf-8")
        logging.info("Execution summary exported.")

    def run(self) -> None:
        logging.info("Assessment run started.")

        self.investors = self.load_csv("investors.csv")
        self.funds = self.load_csv("funds.csv")
        self.transactions = self.load_csv("transactions.csv")
        self.nav_history = self.load_csv("nav_history.csv")

        self.clean_data()
        self.merge_data()

        numpy_metrics = self.calculate_numpy_metrics()

        self.build_holdings()
        self.identify_investors()
        fund_results = self.fund_analysis()
        finance_metrics = self.calculate_finance_metrics()

        self.create_visualizations()
        self.export_execution_summary(numpy_metrics, fund_results, finance_metrics)

        logging.info("Assessment run completed successfully.")


def main() -> None:
    base_dir = Path(__file__).resolve().parent
    portfolio = FundPortfolio(base_dir=base_dir, output_dir="final-reports")
    portfolio.run()


if __name__ == "__main__":
    main()
