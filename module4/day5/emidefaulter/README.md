# Secure EMI Defaulter Management API

Spring Boot API for EMI tracking, defaulter detection, penalty generation, JWT authentication, and role-based authorization.

## Implemented Highlights

- Entity mapping with `@OneToMany` / `@ManyToOne`, `CascadeType.ALL`, `FetchType.LAZY`
- Bean validation on `Customer`, `Loan`, `EmiPayment`, and `Penalty`
- Derived and JPQL repository queries for reports and analytics
- JPQL bulk update for active personal-loan interest updates
- Pageable loan listing endpoint (`GET /api/loans`) with default `emiAmount DESC`
- DTO projections for report endpoints and dashboard
- JWT auth flow (`POST /api/login`) with Spring Security
- Method-level role authorization using `@PreAuthorize`
- Global exception handling with standard JSON error payload
- Midnight scheduler for missed EMI marking, penalty generation, and loan defaulting

## Key Endpoints

- `POST /login`
- `GET /loans`
- `GET /reports/high-risk-customers`
- `GET /reports/outstanding-city-wise`
- `GET /reports/multiple-loan-types`
- `GET /reports/top-defaulters`
- `GET /reports/customer-loan-summary`
- `GET /dashboard`

## Roles

- `ADMIN`: delete customer, delete loan
- `RECOVERY_MANAGER`: generate penalties, update loan status, increase interest rates
- `AUDITOR`: report/dashboard access
- `CUSTOMER`: own loans and own EMI history

## Configuration

Set values in `src/main/resources/application.properties` or environment overrides:

- `jwt.secret` (must be at least 32 bytes)
- `jwt.expiration-ms`
- `spring.datasource.*` (for PostgreSQL runtime)

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

## Test

```powershell
.\mvnw.cmd test
```


