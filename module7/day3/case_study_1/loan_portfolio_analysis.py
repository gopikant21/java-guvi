import json
from dataclasses import dataclass
from pathlib import Path
from typing import Dict, Tuple

import numpy as np
import pandas as pd


@dataclass
class Loan:
    loan_id: str
    customer_id: int
    loan_amount: float
    interest_rate: float
    tenure: int
    emi: float
    paid_emis: int
    default_flag: int

    def debt_to_income(self, salary: float) -> float:
        if salary <= 0:
            return np.nan
        return self.loan_amount / salary

    def utilization(self) -> float:
        if self.tenure <= 0:
            return np.nan
        return self.paid_emis / self.tenure


class LoanPortfolioAnalysis:
    def __init__(self, data_dir: Path) -> None:
        self.data_dir = data_dir
        self.output_dir = self.data_dir / "final_reports"
        self.customers = pd.DataFrame()
        self.loans = pd.DataFrame()
        self.credit_scores = pd.DataFrame()
        self.merged = pd.DataFrame()

    def _safe_read_csv(self, file_name: str) -> pd.DataFrame:
        file_path = self.data_dir / file_name
        try:
            return pd.read_csv(file_path)
        except FileNotFoundError as exc:
            raise FileNotFoundError(f"Missing input file: {file_path}") from exc
        except pd.errors.EmptyDataError as exc:
            raise ValueError(f"Empty or corrupted CSV file: {file_path}") from exc
        except pd.errors.ParserError as exc:
            raise ValueError(f"Invalid CSV format in file: {file_path}") from exc

    def read_data(self) -> None:
        self.customers = self._safe_read_csv("customers.csv")
        self.loans = self._safe_read_csv("loans.csv")
        # Dataset has credit_scores.csv in folder, while assignment says credit_score.csv
        self.credit_scores = self._safe_read_csv("credit_scores.csv")

    def clean_data(self) -> None:
        self.customers = self.customers.drop_duplicates().copy()
        self.loans = self.loans.drop_duplicates().copy()
        self.credit_scores = self.credit_scores.drop_duplicates().copy()

        if "Salary" in self.customers.columns:
            self.customers["Salary"] = pd.to_numeric(self.customers["Salary"], errors="coerce")
            self.customers["Salary"] = self.customers["Salary"].fillna(self.customers["Salary"].median())

        if "CreditScore" in self.credit_scores.columns:
            self.credit_scores["CreditScore"] = pd.to_numeric(
                self.credit_scores["CreditScore"], errors="coerce"
            )
            self.credit_scores["CreditScore"] = self.credit_scores["CreditScore"].fillna(
                self.credit_scores["CreditScore"].mean()
            )

        if "InterestRate" in self.loans.columns:
            self.loans["InterestRate"] = pd.to_numeric(self.loans["InterestRate"], errors="coerce")
            self.loans["InterestRate"] = self.loans["InterestRate"].ffill()

        numeric_cols = ["LoanAmount", "EMI", "PaidEMIs", "Tenure", "DefaultFlag"]
        for col in numeric_cols:
            if col in self.loans.columns:
                self.loans[col] = pd.to_numeric(self.loans[col], errors="coerce")

        if "LoanAmount" in self.loans.columns:
            p99 = self.loans["LoanAmount"].quantile(0.99)
            self.loans = self.loans[self.loans["LoanAmount"] <= p99].copy()

    def merge_data(self) -> None:
        merged = self.customers.merge(self.loans, on="CustomerID", how="inner")
        merged = merged.merge(self.credit_scores, on="CustomerID", how="left")

        merged["Age"] = pd.to_numeric(merged["Age"], errors="coerce")
        merged["Salary"] = pd.to_numeric(merged["Salary"], errors="coerce")
        merged["CreditScore"] = pd.to_numeric(merged["CreditScore"], errors="coerce")

        self.merged = merged

    def calculate_numpy_metrics(self) -> Dict[str, float]:
        loan_amount = self.merged["LoanAmount"].to_numpy(dtype=float)
        salary = self.merged["Salary"].to_numpy(dtype=float)
        interest = self.merged["InterestRate"].to_numpy(dtype=float)

        return {
            "mean_loan_amount": float(np.nanmean(loan_amount)),
            "median_salary": float(np.nanmedian(salary)),
            "percentile_interest_rate_90": float(np.nanpercentile(interest, 90)),
            "corr_salary_loan_amount": float(np.corrcoef(salary, loan_amount)[0, 1]),
            "std_loan_amount": float(np.nanstd(loan_amount)),
        }

    def apply_finance_metrics(self) -> None:
        self.merged["DebtToIncomeRatio"] = self.merged["LoanAmount"] / self.merged["Salary"]
        self.merged["LoanUtilization"] = self.merged["PaidEMIs"] / self.merged["Tenure"]

        self.merged["RemainingTenure"] = (self.merged["Tenure"] - self.merged["PaidEMIs"]).clip(lower=0)
        self.merged["OutstandingPrincipal"] = (
            self.merged["EMI"] * self.merged["RemainingTenure"]
        )

        self.merged["ProbabilityDefault"] = np.where(
            self.merged["CreditScore"] <= 0,
            0.5,
            (850 - self.merged["CreditScore"]) / 550,
        )
        self.merged["ProbabilityDefault"] = self.merged["ProbabilityDefault"].clip(0, 1)

        # Expected Loss = PD * LGD * EAD (LGD assumed as 45%)
        self.merged["ExpectedLoss"] = (
            self.merged["ProbabilityDefault"] * 0.45 * self.merged["OutstandingPrincipal"]
        )

    def get_high_risk_customers(self) -> pd.DataFrame:
        strict_mask = (
            (self.merged["CreditScore"] < 650)
            & (self.merged["Salary"] < 60000)
            & (self.merged["LoanAmount"] > 10_00_000)
            & (self.merged["DefaultFlag"] == 1)
        )

        strict_high_risk = self.merged[strict_mask].copy()
        if not strict_high_risk.empty:
            strict_high_risk["RiskScore"] = (
                (700 - strict_high_risk["CreditScore"]).clip(lower=0) * 0.4
                + (strict_high_risk["LoanAmount"] / strict_high_risk["Salary"]) * 25 * 0.3
                + (strict_high_risk["DefaultFlag"] * 100) * 0.3
            )
            strict_high_risk["RiskSelectionMode"] = "StrictAssignmentFilter"
            return strict_high_risk.sort_values("RiskScore", ascending=False).head(20)

        # Fallback: if strict criteria returns no rows, rank by weighted risk score.
        candidates = self.merged.copy()

        max_loan = candidates["LoanAmount"].max()
        max_salary = candidates["Salary"].max()

        candidates["LowCreditFlag"] = (candidates["CreditScore"] < 650).astype(int)
        candidates["LowSalaryFlag"] = (candidates["Salary"] < 60000).astype(int)
        candidates["HighLoanFlag"] = (candidates["LoanAmount"] > 10_00_000).astype(int)
        candidates["DefaultFlagBinary"] = (candidates["DefaultFlag"] == 1).astype(int)

        candidates["RiskRuleMatches"] = (
            candidates["LowCreditFlag"]
            + candidates["LowSalaryFlag"]
            + candidates["HighLoanFlag"]
            + candidates["DefaultFlagBinary"]
        )

        candidates["CreditRiskComponent"] = ((850 - candidates["CreditScore"]).clip(lower=0) / 850) * 100
        candidates["SalaryRiskComponent"] = ((max_salary - candidates["Salary"]) / max_salary).clip(lower=0) * 100
        candidates["LoanRiskComponent"] = (candidates["LoanAmount"] / max_loan).clip(lower=0) * 100
        candidates["DefaultRiskComponent"] = candidates["DefaultFlagBinary"] * 100

        candidates["RiskScore"] = (
            candidates["CreditRiskComponent"] * 0.35
            + candidates["SalaryRiskComponent"] * 0.20
            + candidates["LoanRiskComponent"] * 0.25
            + candidates["DefaultRiskComponent"] * 0.20
            + candidates["RiskRuleMatches"] * 5
        )

        candidates["RiskSelectionMode"] = "ScoreFallback"

        return candidates.sort_values(
            ["RiskRuleMatches", "RiskScore", "LoanAmount"], ascending=[False, False, False]
        ).head(20)

    def summarize_metrics(self, numpy_metrics: Dict[str, float]) -> Dict[str, float]:
        default_pct = float((self.merged["DefaultFlag"].mean()) * 100)
        npa_pct = float(
            (
                self.merged.loc[self.merged["OutstandingPrincipal"] > 0, "DefaultFlag"].mean()
            )
            * 100
        )
        average_emi = float(self.merged["EMI"].mean())
        expected_loss_total = float(self.merged["ExpectedLoss"].sum())

        summary = {
            "record_count": int(len(self.merged)),
            "default_percent": default_pct,
            "npa_percent": npa_pct,
            "average_emi": average_emi,
            "expected_loss_total": expected_loss_total,
        }
        summary.update(numpy_metrics)
        return summary

    def loan_objects_preview(self, n: int = 5) -> Tuple[Loan, ...]:
        rows = self.merged.head(n)
        loans = []
        for _, row in rows.iterrows():
            loans.append(
                Loan(
                    loan_id=str(row["LoanID"]),
                    customer_id=int(row["CustomerID"]),
                    loan_amount=float(row["LoanAmount"]),
                    interest_rate=float(row["InterestRate"]),
                    tenure=int(row["Tenure"]),
                    emi=float(row["EMI"]),
                    paid_emis=int(row["PaidEMIs"]),
                    default_flag=int(row["DefaultFlag"]),
                )
            )
        return tuple(loans)

    def export_reports(self, high_risk: pd.DataFrame, summary: Dict[str, float]) -> None:
        self.output_dir.mkdir(parents=True, exist_ok=True)

        output_excel = self.output_dir / "risk_report.xlsx"
        output_csv = self.output_dir / "high_risk_customers.csv"
        output_json = self.output_dir / "summary.json"

        with pd.ExcelWriter(output_excel, engine="openpyxl") as writer:
            self.merged.to_excel(writer, sheet_name="merged_data", index=False)
            high_risk.to_excel(writer, sheet_name="top_risky_customers", index=False)

            metrics_df = pd.DataFrame(list(summary.items()), columns=["Metric", "Value"])
            metrics_df.to_excel(writer, sheet_name="portfolio_metrics", index=False)

        high_risk.to_csv(output_csv, index=False)

        with open(output_json, "w", encoding="utf-8") as file:
            json.dump(summary, file, indent=2)


def main() -> None:
    analysis = LoanPortfolioAnalysis(data_dir=Path(__file__).parent)

    analysis.read_data()
    analysis.clean_data()
    analysis.merge_data()
    analysis.apply_finance_metrics()

    numpy_metrics = analysis.calculate_numpy_metrics()
    high_risk = analysis.get_high_risk_customers()
    summary = analysis.summarize_metrics(numpy_metrics=numpy_metrics)

    # OOP demonstration: create Loan objects from sample rows
    _sample_loan_objects = analysis.loan_objects_preview(n=5)

    analysis.export_reports(high_risk=high_risk, summary=summary)

    print("Analysis complete.")
    print(f"Records processed: {len(analysis.merged)}")
    print("Generated files:")
    print(f"- {analysis.output_dir / 'risk_report.xlsx'}")
    print(f"- {analysis.output_dir / 'high_risk_customers.csv'}")
    print(f"- {analysis.output_dir / 'summary.json'}")


if __name__ == "__main__":
    main()
