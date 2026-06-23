# Loan System - Spring Data JPA API

This project exposes all `LendingAnalytics` functions as REST APIs backed by Spring Data JPA.

## Stack
- Java 17
- Spring Boot
- Spring Data JPA
- H2 (default local runtime DB)
- PostgreSQL driver (available if you want external DB)

## Run
```powershell
.\mvnw.cmd spring-boot:run
```

Base URL: `http://localhost:8080/api/v1/loans`

## API Endpoints
- `POST /load`
- `GET /top-credit-profiles?n=10`
- `GET /average-loan-amount-by-type`
- `GET /highest-loan-application`
- `GET /lenders-with-multiple-loan-types`
- `GET /group-applications-by-lender`
- `GET /suspicious-applications`
- `GET /loan-type-wise-top-applicant-by-lender`

## Sample Load Request
```json
{
  "records": [
    "A101|Rahul Sharma|HDFC|Personal Loan|500000|780",
    "A102|Priya Verma|ICICI|Home Loan|4500000|820"
  ]
}
```

## Test
```powershell
.\mvnw.cmd test
```

