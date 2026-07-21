# Assessment Case Study: Mutual Fund Portfolio Performance and Risk Analysis

## Business Scenario
A leading Asset Management Company (AMC) manages over 5 million investor transactions across multiple mutual fund schemes. The Fund Management team wants to:

- Analyze portfolio performance
- Identify high-risk investors
- Evaluate fund returns
- Generate automated investment reports

## Input Files

- investors.csv
- funds.csv
- transactions.csv
- nav_history.csv

## Requirements

### Python

- [ ] Read multiple CSV files
- [ ] Handle missing or corrupted files using exception handling
- [ ] Create reusable functions
- [ ] Implement OOP using a `FundPortfolio` class
- [ ] Use logging for report generation
- [ ] Generate reports automatically

### NumPy

Calculate:

- [ ] Mean investment amount
- [ ] Median investor income
- [ ] Standard deviation of NAV
- [ ] Percentile fund returns (90th and 95th)
- [ ] Correlation between investor income and investment amount
- [ ] Average daily NAV

### Pandas

Merge:

- [ ] investors
- [ ] transactions
- [ ] funds
- [ ] nav_history

Identify:

- [ ] Top 20 investors based on portfolio value
- [ ] Investors with investment greater than INR 10 lakhs
- [ ] Investors with high-risk profile
- [ ] Investors with more than 10 transactions
- [ ] Investors with annual income greater than INR 15 lakhs

### Fund Analysis

Find:

- [ ] Best-performing fund
- [ ] Worst-performing fund
- [ ] Highest expense ratio
- [ ] Highest AUM (Assets Under Management)
- [ ] Most popular fund

### Missing Data Handling

Replace:

- [ ] Annual income with median
- [ ] Expense ratio with mean
- [ ] NAV with previous day NAV
- [ ] Risk profile with "Moderate"

### Outlier Detection

Remove:

- [ ] Investment amount above 99th percentile
- [ ] NAV changes beyond 3 standard deviations

### Finance Metrics

Calculate:

- [ ] Total portfolio value
- [ ] Portfolio return percentage
- [ ] CAGR (Compound Annual Growth Rate)
- [ ] Absolute return
- [ ] Annualized return
- [ ] Portfolio diversification score
- [ ] Average holding period
- [ ] Expense ratio impact
- [ ] Sharpe ratio (simplified)
- [ ] Category-wise investment percentage
- [ ] Fund allocation percentage
- [ ] Investor-wise profit and loss

### Data Visualization

Generate charts:

- [ ] Portfolio allocation (pie chart)
- [ ] Fund-wise investment (bar chart)
- [ ] Monthly investment trend (line chart)
- [ ] Category-wise returns (bar chart)
- [ ] NAV movement (line chart)
- [ ] Top 10 investors (horizontal bar chart)

## Mini Project

### Automated Mutual Fund Performance Dashboard

The script should:

- [ ] Read all CSV files
- [ ] Clean missing values
- [ ] Remove duplicate transactions
- [ ] Calculate portfolio metrics
- [ ] Rank funds
- [ ] Identify high-value investors
- [ ] Create charts
- [ ] Export reports
- [ ] Log execution status