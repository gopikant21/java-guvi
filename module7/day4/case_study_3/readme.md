# Case Study: Loan Processing & Loan Repayment Analytics

## Overview
- **Challenge Level:** Medium
- **Estimated Time:** 90–120 minutes
- **Domain:** Banking / Financial Services

## Business Scenario
ABCBank receives hundreds of loan applications every day. The Loan Processing Department wants to automate the loan approval process and analyze loan repayment performance.

The bank maintains the following datasets:
- `customers.csv`
- `loan_applications.csv`
- `loan_payments.csv`

Management needs insights into:
- Eligible and rejected loan applications
- Customer repayment performance
- Defaulted loans
- Loan recovery percentage
- EMI payment status
- Branch-wise and city-wise loan statistics

## Input Files
- `customers.csv`
- `loan_applications.csv`
- `loan_payments.csv`

## Part 1: Read Data
Read all CSV files using Pandas:
- `customers.csv`
- `loan_applications.csv`
- `loan_payments.csv`

## Part 2: Data Cleaning
Perform the following:
- Remove duplicate records
- Remove duplicate Loan IDs
- Check missing values
- Replace missing Salary with median Salary
- Replace missing Credit Score with mean Credit Score
- Convert `ApplicationDate` and `PaymentDate` to datetime
- Remove negative Loan Amounts
- Remove invalid EMI Amounts
- Remove future payment dates

## Part 3: Merge Datasets
Merge all datasets using:
- `CustomerID`
- `LoanID`

Create a single dataframe containing:
- `CustomerName`
- `City`
- `LoanType`
- `LoanAmount`
- `CreditScore`
- `Salary`
- `LoanStatus`
- `EMIAmount`
- `PaymentStatus`

## Part 4: Create New Columns
- **Monthly Income**
	- Formula: `Salary / 12`
- **Debt-to-Income Ratio**
	- Formula: `Loan Amount / Salary`
- **EMI Due**
	- Formula: `EMIAmount - AmountPaid`
- **PaymentCompletion %**
	- Formula: `(AmountPaid / EMIAmount) * 100`

## Part 5: NumPy Tasks
Using NumPy, calculate:
- Average Loan Amount
- Median Loan Amount
- Maximum Loan Amount
- Minimum Loan Amount
- Standard Deviation
- Variance
- 25th percentile Loan Amount
- 75th percentile Loan Amount

## Part 6: Pandas Analysis
Find:
- Top 10 highest loan customers
- Top 10 customers by salary
- Customers with Credit Score below 650
- Customers with Loan Amount greater than ₹20 Lakhs
- Loans with pending payments
- Fully paid loans

## Part 7: GroupBy Analysis
### Group by City
Find:
- Number of customers
- Average salary
- Total loan amount

### Group by Loan Type
Find:
- Number of loans
- Average loan amount
- Total loan amount

### Group by Loan Status
Find:
- Approved loans
- Pending loans
- Rejected loans

### Group by PaymentStatus
Find:
- Paid
- Partial
- Pending

Also compute:
- Count and total amount paid

## Part 8: Business Rules
Flag loans with any of the following conditions:
- Loan Amount greater than ₹30 Lakhs
- Credit Score below 650
- Salary less than ₹30,000
- Debt-to-Income Ratio greater than 5
- EMI Due greater than ₹10,000
- PaymentStatus = `Pending`
- LoanStatus = `Rejected`

## Part 9: Finance Metrics
Calculate:
- **Total Loan Portfolio**
	- Formula: sum of Loan Amount
- **Total Amount Collected**
	- Formula: sum of AmountPaid
- **Outstanding Amount**
	- Formula: `Loan Amount - AmountPaid`
- **Loan Recovery %**
	- Formula: `(AmountCollected / Total Loan Amount) * 100`
- **Default %**
	- Formula: `(Pending Loans / Total Loans) * 100`
- **Average EMI**
	- Formula: average EMI Amount
- **Average Credit Score**
	- Formula: average Credit Score

## Part 10: Export Reports
Generate:
- `LoanSummary.xlsx`
- `CustomerLoanReport.xlsx`
- `PendingPayments.csv`

## Expected Outputs
Display:
- Top 10 loan customers
- Customers with low credit score
- Pending loan payments
- City-wise loan summary
- Loan type summary
- Loan recovery report