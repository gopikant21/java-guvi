# Case Study 1: Credit Risk & Loan Portfolio Analysis

## Difficulty
Hard

## Business Scenario
A bank has 2 million loan records. The Risk Management team wants to identify high-risk customers and calculate portfolio risk metrics.

## Input Files
- customers.csv
- loans.csv
- credit_score.csv

## Requirements

### Python
- Read multiple CSV files
- Exception handling for corrupted files
- Functions
- OOP (Loan class)

### NumPy
Calculate:
- Mean Loan Amount
- Median Salary
- Percentile Interest Rate
- Correlation between Salary and Loan Amount
- Standard Deviation

### Pandas
Merge:
- customers
- loans
- credit_score

Find:
- Top 20 risky customers
- Customers having:
	- CreditScore < 650
	- Salary < 60000
	- Loan > 10 Lakhs
	- DefaultFlag = 1

### Missing Data
Replace:
- Salary -> Median
- CreditScore -> Mean
- Interest Rate -> Previous Value

### Outliers
Remove:
- LoanAmount > 99 percentile

### Finance Metrics
Calculate:
- Debt-to-Income Ratio
- Loan Utilization
- Default %
- NPA %
- Average EMI
- Expected Loss

### Automation
Generate:
- risk_report.xlsx
- high_risk_customers.csv
- summary.json