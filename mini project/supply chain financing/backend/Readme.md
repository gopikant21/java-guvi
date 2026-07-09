```md
# Mini Project: Supply Chain Financing - Loan Lifecycle API

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security (JWT Authentication)
- Role-Based Access Control (RBAC)
- PostgreSQL
- Spring Data JPA
- Maven
- Lombok

---

# Project Goal

Build a simple backend application for a Supply Chain Financing (SCF) system where:

- Customers can apply for loans.
- Admin reviews and approves/rejects loans.
- Approved loans can be disbursed.
- Customers repay the loan.
- Admin tracks the complete loan lifecycle.

This project focuses only on REST APIs.

---

# Roles

## ADMIN

- Manage customers
- View all loans
- Approve/Reject loans
- Disburse loans
- View repayments
- Dashboard

---

## CUSTOMER

- Register/Login
- View profile
- Apply for loan
- View own loans
- Repay loan
- View repayment history

---

# Loan Lifecycle

```

Customer Registers
↓
Login
↓
Apply Loan
↓
Loan Status = PENDING
↓
Admin Reviews
↓
APPROVED / REJECTED
↓
If Approved
↓
DISBURSED
↓
Customer Repays
↓
PARTIALLY_PAID
↓
FULLY_PAID
↓
LOAN CLOSED

```

---

# Database Tables

## User

```

id
name
email
password
role (ADMIN/CUSTOMER)
phone
created_at

```

---

## Loan

```

id
loan_number
customer_id
amount
interest_rate
tenure_months
purpose
status
approved_by
approved_date
disbursed_date
remaining_amount
created_at

```

Loan Status

```

PENDING
APPROVED
REJECTED
DISBURSED
PARTIALLY_PAID
FULLY_PAID
CLOSED

```

---

## Repayment

```

id
loan_id
amount
payment_date
payment_mode
remarks

```

---

# Authentication APIs

---

## Register

```

POST /api/auth/register

````

Request

```json
{
  "name": "John",
  "email": "john@gmail.com",
  "password": "123456",
  "phone": "9999999999"
}
````

Functionality

* Register customer
* Encrypt password
* Default role = CUSTOMER

---

## Login

```
POST /api/auth/login
```

Request

```json
{
  "email": "john@gmail.com",
  "password": "123456"
}
```

Response

```json
{
  "token": "JWT_TOKEN"
}
```

---

# Customer APIs

Require Role: CUSTOMER

---

## Get Profile

```
GET /api/customer/profile
```

Returns logged-in customer details.

---

## Update Profile

```
PUT /api/customer/profile
```

Update

* Name
* Phone

---

## Apply Loan

```
POST /api/customer/loans
```

Request

```json
{
  "amount": 500000,
  "interestRate": 12,
  "tenureMonths": 24,
  "purpose": "Inventory Purchase"
}
```

Functionality

* Create loan
* Status = PENDING

---

## View My Loans

```
GET /api/customer/loans
```

Returns all loans of logged-in customer.

---

## View Loan Details

```
GET /api/customer/loans/{loanId}
```

Returns

* Loan info
* Status
* Remaining amount

---

## Repay Loan

```
POST /api/customer/loans/{loanId}/repay
```

Request

```json
{
  "amount": 10000,
  "paymentMode": "UPI"
}
```

Functionality

* Create repayment
* Update remaining amount

If remaining amount > 0

```
PARTIALLY_PAID
```

If remaining amount == 0

```
FULLY_PAID
```

---

## Repayment History

```
GET /api/customer/loans/{loanId}/repayments
```

Returns repayment history.

---

# Admin APIs

Require Role: ADMIN

---

## Dashboard

```
GET /api/admin/dashboard
```

Return

```
Total Customers

Total Loans

Pending Loans

Approved Loans

Rejected Loans

Disbursed Loans

Closed Loans

Total Amount Disbursed
```

---

## Get All Customers

```
GET /api/admin/customers
```

---

## Get Customer Details

```
GET /api/admin/customers/{id}
```

Includes

* Customer
* Loans

---

## Get All Loans

```
GET /api/admin/loans
```

Optional Filters

```
status

customerId
```

Example

```
GET /api/admin/loans?status=PENDING
```

---

## Loan Details

```
GET /api/admin/loans/{loanId}
```

Includes

* Customer
* Loan
* Repayments

---

## Approve Loan

```
PUT /api/admin/loans/{loanId}/approve
```

Functionality

* Status → APPROVED
* Save approver
* Save approval date

---

## Reject Loan

```
PUT /api/admin/loans/{loanId}/reject
```

Request

```json
{
  "reason": "Low Credit Score"
}
```

Functionality

* Status → REJECTED

---

## Disburse Loan

```
PUT /api/admin/loans/{loanId}/disburse
```

Functionality

* Status → DISBURSED
* Save disbursement date

---

## View Loan Repayments

```
GET /api/admin/loans/{loanId}/repayments
```

Returns all repayments.

---

# RBAC Matrix

| API               | CUSTOMER | ADMIN |
| ----------------- | -------- | ----- |
| Register          | ✅        | ❌     |
| Login             | ✅        | ✅     |
| Profile           | ✅ Own    | ❌     |
| Apply Loan        | ✅        | ❌     |
| View Own Loans    | ✅        | ❌     |
| Repay Loan        | ✅        | ❌     |
| Repayment History | ✅ Own    | ❌     |
| Dashboard         | ❌        | ✅     |
| Customers         | ❌        | ✅     |
| All Loans         | ❌        | ✅     |
| Approve Loan      | ❌        | ✅     |
| Reject Loan       | ❌        | ✅     |
| Disburse Loan     | ❌        | ✅     |
| Loan Repayments   | ❌        | ✅     |

---

# Suggested Package Structure

```
src/main/java
│
├── config
│
├── security
│
├── controller
│     ├── AuthController
│     ├── CustomerController
│     └── AdminController
│
├── service
│     ├── AuthService
│     ├── CustomerService
│     └── AdminService
│
├── repository
│
├── entity
│     ├── User
│     ├── Loan
│     └── Repayment
│
├── dto
│     ├── request
│     └── response
│
├── mapper
│
├── exception
│
└── util
```

---

# Optional Enhancements

* Swagger/OpenAPI documentation
* Global Exception Handling (`@ControllerAdvice`)
* Bean Validation (`@Valid`)
* Pagination & Sorting
* Search loans by amount/date/status
* Audit fields (`createdBy`, `updatedBy`)
* Soft delete for users
* Unit tests using JUnit 5 and Mockito
* Integration tests with Spring Boot Test
* Docker Compose for PostgreSQL
* Role-based method security using `@PreAuthorize`

---

# Learning Outcomes

By completing this project, you will learn:

* Spring Boot REST API development
* JWT Authentication
* Role-Based Access Control (RBAC)
* Spring Security
* JPA Relationships (`@OneToMany`, `@ManyToOne`)
* Entity-DTO mapping
* PostgreSQL integration
* Loan lifecycle management
* Clean layered architecture
* Basic enterprise backend design

```
```
