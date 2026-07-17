# Case Study 2: Mutual Fund Performance Analytics (Medium Challenge)

## Difficulty
Medium

## Estimated Time
90-120 minutes

## Skills Covered
- Python Functions
- File Handling
- Exception Handling
- NumPy
- Pandas
- Data Cleaning
- GroupBy
- Aggregation
- Merge
- Basic Financial Calculations

## Business Scenario
ABC Asset Management Company (AMC) manages multiple mutual funds across India.

The management wants a Python application that analyzes fund performance, investor portfolios, and investment returns to help recommend the best-performing funds.

The company provides the following datasets:
- funds.csv
- investors.csv
- transactions.csv
- nav_history.csv

## Part 1 - Read Data
Read all CSV files using Pandas:
- funds.csv
- investors.csv
- transactions.csv
- nav_history.csv

## Part 2 - Data Cleaning
Perform the following:
- Remove duplicate rows
- Check missing values
- Fill missing NAV using Forward Fill
- Replace missing InvestorType with "Retail"
- Remove rows having negative NAV
- Convert Date columns into datetime format

## Part 3 - Merge Data
Merge all four datasets into one DataFrame.

Required columns:
- Investor Name
- Fund Name
- Category
- AMC
- State
- Units Purchased
- Purchase NAV
- Latest NAV

## Part 4 - Create New Columns
Calculate:

Investment Amount:
$$
	ext{Investment Amount} = \text{Units Purchased} \times \text{Purchase NAV}
$$

Current Value:
$$
	ext{Current Value} = \text{Units Purchased} \times \text{Latest NAV}
$$

Profit:
$$
	ext{Profit} = \text{Current Value} - \text{Investment Amount}
$$

ROI %:
$$
	ext{ROI \%} = \left(\frac{\text{Current Value} - \text{Investment Amount}}{\text{Investment Amount}}\right) \times 100
$$

## Part 5 - NumPy Tasks
Using NumPy, calculate:
- Average NAV
- Maximum NAV
- Minimum NAV
- Variance of NAV
- Standard Deviation of NAV
- Rolling Average (window = 5)

## Part 6 - Pandas Analysis
Display:
- Top 5 investors based on investment amount
- Top 5 profitable funds
- Worst performing fund
- Highest NAV fund
- Lowest NAV fund

## Part 7 - GroupBy
### Group by Category
Find:
- Average ROI
- Average NAV
- Total Investment

### Group by AMC
Find:
- Number of Funds
- Average NAV
- Total Investment

### Group by State
Find:
- Number of Investors
- Total Investment
- Average ROI

### Group by Investor Type
Find:
- Total Investment
- Average Profit

## Part 8 - Detect Issues
Find:
- Duplicate NAV records
- Negative NAV
- Future dates
- Missing Fund IDs
- Missing Investor IDs
- Invalid Purchase NAV (< 0)

## Part 9 - Finance Metrics
Calculate:

ROI:
$$
	ext{ROI} = \left(\frac{\text{Current Value} - \text{Investment Amount}}{\text{Investment Amount}}\right) \times 100
$$

Absolute Return:
$$
	ext{Absolute Return} = \text{Current Value} - \text{Investment Amount}
$$

Annual Return:
- Assume holding period is 1 year

Volatility:
- Using $\text{np.std(NAV)}$

Sharpe Ratio:
- Assume Risk Free Rate = 6%
- Formula:
$$
	ext{Sharpe Ratio} = \frac{\text{Return} - \text{Risk Free Rate}}{\text{Volatility}}
$$

## Part 10 - Export Reports
Generate:
- TopFunds.xlsx
- InvestorSummary.xlsx
- CategorySummary.csv

## Expected Outputs
### Top Performing Funds
- Highest ROI
- Highest Profit
- Highest NAV

### Worst Performing Fund
- Lowest ROI

### Insights
- State-wise Investment
- AMC-wise Investment
- Category-wise ROI
