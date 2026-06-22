# NBFC Fintech Module: Invoice Discounting

This module adds a **non-loan NBFC/fintech use case** to your Spring Boot JPA project.

## What it models

Invoice discounting for MSME sellers:
- seller uploads approved invoices from anchor corporates
- NBFC funds discounted invoice value
- platform tracks maturity, repayment, delinquency, and portfolio analytics

## Core classes

- `src/main/java/org/example/jpademo/model/InvoiceDiscountingDeal.java`
- `src/main/java/org/example/jpademo/repository/InvoiceDiscountingRepository.java`
- `src/main/java/org/example/jpademo/service/InvoiceDiscountingService.java`
- `src/main/java/org/example/jpademo/service/InvoiceDiscountingServiceImpl.java`
- `src/main/java/org/example/jpademo/controller/InvoiceDiscountingRestController.java`

## Main API base

`/api/invoice-discounting`

## Example operations

- `POST /api/invoice-discounting` create deal
- `POST /api/invoice-discounting/{id}/fund?fundedOn=2026-06-19`
- `POST /api/invoice-discounting/{id}/repay?repaidOn=2026-07-05`
- `POST /api/invoice-discounting/mark-overdue?referenceDate=2026-07-20`
- `GET /api/invoice-discounting/analytics/exposure`
- `GET /api/invoice-discounting/analytics/expected-gross-yield`

## Deal status suggestions

Use these statuses for consistency:
- `CREATED`
- `FUNDED`
- `OVERDUE`
- `REPAID`

## Run

```powershell
Set-Location "C:\Users\gopi.kant\Java narc\module3\day3\jpaDemo"
.\mvnw.cmd spring-boot:run
```

