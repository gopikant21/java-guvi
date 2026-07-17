import pandas as pd
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parent
CUSTOMERS_FILE = BASE_DIR / "customers.csv"
LOANS_FILE = BASE_DIR / "loan_application.csv"
PAYMENTS_FILE = BASE_DIR / "loan_payments.csv"
OUTPUT_DIR = BASE_DIR / "final_reports"


def normalize_numeric_id(series: pd.Series) -> pd.Series:
    return pd.to_numeric(series.astype(str).str.extract(r"(\d+)")[0], errors="coerce")


def normalize_customer_ids(customers: pd.DataFrame, loans: pd.DataFrame) -> tuple[pd.DataFrame, pd.DataFrame]:
    customers = customers.copy()
    loans = loans.copy()

    customers["CustomerID_norm"] = normalize_numeric_id(customers["CustomerID"])
    loans["CustomerID_norm"] = normalize_numeric_id(loans["CustomerID"])

    return customers, loans


def normalize_loan_ids(loans: pd.DataFrame, payments: pd.DataFrame) -> tuple[pd.DataFrame, pd.DataFrame]:
    loans = loans.copy()
    payments = payments.copy()

    loans["LoanID_num"] = normalize_numeric_id(loans["LoanID"])
    payments["LoanID_num"] = normalize_numeric_id(payments["LoanID"])

    # Payments have ids like L101 while loans have L1001. Align by subtracting 900.
    loans["LoanID_norm"] = loans["LoanID_num"]
    loans.loc[loans["LoanID_norm"] >= 1000, "LoanID_norm"] = loans.loc[
        loans["LoanID_norm"] >= 1000, "LoanID_norm"
    ] - 900

    payments["LoanID_norm"] = payments["LoanID_num"]

    return loans, payments


def safe_to_excel(df: pd.DataFrame, file_name: str) -> None:
    try:
        df.to_excel(file_name, index=False)
        print(f"Exported: {file_name}")
    except Exception as exc:
        fallback = file_name.replace(".xlsx", ".csv")
        df.to_csv(fallback, index=False)
        print(f"Could not export {file_name} ({exc}). Exported fallback: {fallback}")


def classify_payment_status(row: pd.Series) -> str:
    pending = row.get("PendingEMIs", 0)
    paid = row.get("PaidEMIs", 0)
    if pd.isna(pending):
        return "Pending"
    if pending <= 0:
        return "Paid"
    if pd.notna(paid) and paid > 0:
        return "Partial"
    return "Pending"


def main() -> None:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    # Part 1 - Read Data
    customers = pd.read_csv(CUSTOMERS_FILE)
    loans = pd.read_csv(LOANS_FILE)
    payments = pd.read_csv(PAYMENTS_FILE)

    # Part 2 - Data Cleaning
    customers = customers.drop_duplicates()
    loans = loans.drop_duplicates()
    payments = payments.drop_duplicates()

    loans = loans.drop_duplicates(subset=["LoanID"])

    # Ensure required columns exist for requested transformations
    if "CreditScore" not in loans.columns:
        loans["CreditScore"] = float("nan")
    if "PaymentDate" not in payments.columns and "LastPaymentDate" in payments.columns:
        payments["PaymentDate"] = payments["LastPaymentDate"]

    print("Missing values before fill:")
    print("customers:")
    print(customers.isna().sum())
    print("loans:")
    print(loans.isna().sum())
    print("payments:")
    print(payments.isna().sum())

    customers["Salary"] = pd.to_numeric(customers["Salary"], errors="coerce")
    median_salary = customers["Salary"].median()
    customers["Salary"] = customers["Salary"].fillna(median_salary)

    loans["CreditScore"] = pd.to_numeric(loans["CreditScore"], errors="coerce")
    mean_credit = loans["CreditScore"].mean()
    if pd.isna(mean_credit):
        mean_credit = 700
    loans["CreditScore"] = loans["CreditScore"].fillna(mean_credit)

    loans["ApplicationDate"] = pd.to_datetime(loans["ApplicationDate"], errors="coerce")
    payments["PaymentDate"] = pd.to_datetime(payments["PaymentDate"], errors="coerce")

    loans["LoanAmount"] = pd.to_numeric(loans["LoanAmount"], errors="coerce")
    payments["EMIAmount"] = pd.to_numeric(payments["EMIAmount"], errors="coerce")
    payments["PaidEMIs"] = pd.to_numeric(payments["PaidEMIs"], errors="coerce")
    payments["PendingEMIs"] = pd.to_numeric(payments["PendingEMIs"], errors="coerce")

    loans = loans[loans["LoanAmount"] >= 0]
    payments = payments[payments["EMIAmount"] > 0]

    today = pd.Timestamp.today().normalize()
    payments = payments[payments["PaymentDate"] <= today]

    # Derive AmountPaid and PaymentStatus for analytics
    payments["AmountPaid"] = payments["EMIAmount"] * payments["PaidEMIs"]
    payments["PaymentStatus"] = payments.apply(classify_payment_status, axis=1)

    # Part 3 - Merge Datasets
    customers, loans = normalize_customer_ids(customers, loans)
    loans, payments = normalize_loan_ids(loans, payments)

    merged = loans.merge(
        customers,
        on="CustomerID_norm",
        how="left",
        suffixes=("_loan", "_cust"),
    )
    merged = merged.merge(
        payments,
        on="LoanID_norm",
        how="left",
        suffixes=("", "_pay"),
    )

    # Required single dataframe columns
    required_cols = [
        "CustomerName",
        "City",
        "LoanType",
        "LoanAmount",
        "CreditScore",
        "Salary",
        "LoanStatus",
        "EMIAmount",
        "PaymentStatus",
    ]
    for col in required_cols:
        if col not in merged.columns:
            merged[col] = float("nan")

    final_df = merged.copy()

    # Part 4 - Create New Columns
    final_df["MonthlyIncome"] = final_df["Salary"] / 12
    final_df["DebtToIncomeRatio"] = (final_df["LoanAmount"] / final_df["Salary"]).where(
        final_df["Salary"].fillna(0) != 0,
        float("nan"),
    )
    final_df["EMIDue"] = final_df["EMIAmount"] - final_df["AmountPaid"]
    final_df["PaymentCompletionPct"] = ((final_df["AmountPaid"] / final_df["EMIAmount"]) * 100).where(
        final_df["EMIAmount"].fillna(0) != 0,
        float("nan"),
    )

    # Part 5 - NumPy Tasks
    loan_amounts = final_df["LoanAmount"].dropna()
    numpy_metrics = {
        "AverageLoanAmount": float(loan_amounts.mean()) if not loan_amounts.empty else float("nan"),
        "MedianLoanAmount": float(loan_amounts.median()) if not loan_amounts.empty else float("nan"),
        "MaximumLoanAmount": float(loan_amounts.max()) if not loan_amounts.empty else float("nan"),
        "MinimumLoanAmount": float(loan_amounts.min()) if not loan_amounts.empty else float("nan"),
        "StandardDeviation": float(loan_amounts.std(ddof=0)) if not loan_amounts.empty else float("nan"),
        "Variance": float(loan_amounts.var(ddof=0)) if not loan_amounts.empty else float("nan"),
        "P25LoanAmount": float(loan_amounts.quantile(0.25)) if not loan_amounts.empty else float("nan"),
        "P75LoanAmount": float(loan_amounts.quantile(0.75)) if not loan_amounts.empty else float("nan"),
    }

    # Part 6 - Pandas Analysis
    top10_highest_loan_customers = final_df.nlargest(10, "LoanAmount")[["CustomerName", "LoanAmount", "LoanType"]]
    top10_salary_customers = final_df.nlargest(10, "Salary")[["CustomerName", "Salary", "City"]]
    low_credit_customers = final_df[final_df["CreditScore"] < 650][["CustomerName", "CreditScore", "LoanAmount"]]
    high_loan_customers = final_df[final_df["LoanAmount"] > 2_000_000][["CustomerName", "LoanAmount", "LoanStatus"]]
    pending_payments = final_df[final_df["PaymentStatus"] == "Pending"][
        ["LoanID", "CustomerName", "EMIAmount", "AmountPaid", "PaymentStatus"]
    ]
    fully_paid_loans = final_df[final_df["PaymentStatus"] == "Paid"][
        ["LoanID", "CustomerName", "LoanAmount", "PaymentStatus"]
    ]

    # Part 7 - GroupBy
    city_summary = final_df.groupby("City", dropna=False).agg(
        NumberOfCustomers=("CustomerID_norm", "nunique"),
        AverageSalary=("Salary", "mean"),
        TotalLoanAmount=("LoanAmount", "sum"),
    ).reset_index()

    loan_type_summary = final_df.groupby("LoanType", dropna=False).agg(
        NumberOfLoans=("LoanID", "count"),
        AverageLoanAmount=("LoanAmount", "mean"),
        TotalLoanAmount=("LoanAmount", "sum"),
    ).reset_index()

    loan_status_summary = final_df.groupby("LoanStatus", dropna=False).agg(
        NumberOfLoans=("LoanID", "count")
    ).reset_index()

    payment_status_summary = final_df.groupby("PaymentStatus", dropna=False).agg(
        Count=("LoanID", "count"),
        TotalAmountPaid=("AmountPaid", "sum"),
    ).reset_index()

    # Part 8 - Business Rules
    final_df["Flag_HighLoan"] = final_df["LoanAmount"] > 3_000_000
    final_df["Flag_LowCredit"] = final_df["CreditScore"] < 650
    final_df["Flag_LowSalary"] = final_df["Salary"] < 30_000
    final_df["Flag_HighDTI"] = final_df["DebtToIncomeRatio"] > 5
    final_df["Flag_HighEMIDue"] = final_df["EMIDue"] > 10_000
    final_df["Flag_PendingPayment"] = final_df["PaymentStatus"] == "Pending"
    final_df["Flag_RejectedLoan"] = final_df["LoanStatus"] == "Rejected"

    # Part 9 - Finance Metrics
    total_loan_portfolio = final_df["LoanAmount"].sum(skipna=True)
    total_amount_collected = final_df["AmountPaid"].sum(skipna=True)
    outstanding_amount = (final_df["LoanAmount"] - final_df["AmountPaid"]).sum(skipna=True)

    loan_recovery_pct = (
        (total_amount_collected / total_loan_portfolio) * 100 if total_loan_portfolio else float("nan")
    )

    pending_loans_count = final_df[final_df["PaymentStatus"] == "Pending"]["LoanID"].count()
    total_loans_count = final_df["LoanID"].count()
    default_pct = ((pending_loans_count / total_loans_count) * 100) if total_loans_count else float("nan")

    finance_metrics = pd.DataFrame(
        {
            "Metric": [
                "Total Loan Portfolio",
                "Total Amount Collected",
                "Outstanding Amount",
                "Loan Recovery %",
                "Default %",
                "Average EMI",
                "Average Credit Score",
            ],
            "Value": [
                total_loan_portfolio,
                total_amount_collected,
                outstanding_amount,
                loan_recovery_pct,
                default_pct,
                final_df["EMIAmount"].mean(skipna=True),
                final_df["CreditScore"].mean(skipna=True),
            ],
        }
    )

    # Part 10 - Export Reports
    loan_summary = pd.concat(
        [
            pd.DataFrame([numpy_metrics]),
            finance_metrics.set_index("Metric").T.reset_index(drop=True),
        ],
        axis=1,
    )

    safe_to_excel(loan_summary, str(OUTPUT_DIR / "LoanSummary.xlsx"))
    safe_to_excel(final_df, str(OUTPUT_DIR / "CustomerLoanReport.xlsx"))
    pending_payments.to_csv(OUTPUT_DIR / "PendingPayments.csv", index=False)
    print(f"Exported: {OUTPUT_DIR / 'PendingPayments.csv'}")

    # Expected Outputs
    print("\nTop 10 Loan Customers:")
    print(top10_highest_loan_customers.to_string(index=False))

    print("\nCustomers with Low Credit Score:")
    print(low_credit_customers.to_string(index=False))

    print("\nPending Loan Payments:")
    print(pending_payments.to_string(index=False))

    print("\nCity-wise Loan Summary:")
    print(city_summary.to_string(index=False))

    print("\nLoan Type Summary:")
    print(loan_type_summary.to_string(index=False))

    print("\nLoan Recovery Report:")
    print(finance_metrics.to_string(index=False))

    # Additional requested analysis outputs
    print("\nTop 10 Customers by Salary:")
    print(top10_salary_customers.to_string(index=False))

    print("\nCustomers with Loan Amount > 20 Lakhs:")
    print(high_loan_customers.to_string(index=False))

    print("\nFully Paid Loans:")
    print(fully_paid_loans.to_string(index=False))

    print("\nLoan Status Summary:")
    print(loan_status_summary.to_string(index=False))

    print("\nPayment Status Summary:")
    print(payment_status_summary.to_string(index=False))


if __name__ == "__main__":
    main()
