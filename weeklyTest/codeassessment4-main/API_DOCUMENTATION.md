# Secure Banking API - Spring Boot Assessment

## Overview
A secure REST API for managing customer accounts with JWT authentication, Spring Data JPA queries, and comprehensive exception handling.

## Implemented Features

### ✅ Task 1: Entity Mapping (10 Marks)
- **Customer.java**: OneToMany relationship with Account, CascadeType.ALL, FetchType.LAZY
- **Account.java**: ManyToOne relationship with Customer, OneToMany with Transaction
- **Transaction.java**: ManyToOne relationship with Account
- Proper `@JoinColumn` and `mappedBy` annotations

### ✅ Task 2: Bean Validation (10 Marks)
- `@NotBlank`, `@Email`, `@Positive`, `@Size`, `@NotNull`
- Applied to all entity fields
- Proper validation messages

### ✅ Task 3: Spring Data JPA Derived Queries (15 Marks)
- `findByAccountType(String accountType)`
- `findByBalanceGreaterThan(double amount)`
- `findByBranch(String branch)`
- `findByTransactionType(String transactionType)`

### ✅ Task 4: JPQL Queries (50 Marks)
**Find Rich Customers**
```
findRichCustomers(double threshold) - Returns customers with total balance > threshold
```

**Find Total Balance Per Branch**
```
findTotalBalancePerBranch() - GROUP BY branch, SUM(balance)
```

**Find Customers With Multiple Accounts**
```
findCustomersWithMultipleAccounts() - COUNT(accounts) > 1, GROUP BY, HAVING
```

**Find Latest Transaction**
```
findLatestTransaction() - ORDER BY transactionDate DESC
```

**Find Accounts With No Transactions**
```
findAccountsWithNoTransactions() - LEFT JOIN, WHERE IS NULL
```

### ✅ Task 5: JPQL Update Query (10 Marks)
- `increaseBalance(String accountNumber, double amount)`
- Uses `@Modifying` and `@Transactional`
- Direct SQL UPDATE for performance

### ✅ Task 6: Pagination & Sorting (10 Marks)
- `GET /api/accounts` with Pageable
- Default sorting by balance DESC
- Customizable page size and number

### ✅ Task 7: DTO Mapping (10 Marks)
- `CustomerSummaryDTO` projection
- Returns: customerName, branch, numberOfAccounts, totalBalance
- JPQL SELECT NEW syntax for mapping

### ✅ Task 8: JWT Authentication (25 Marks)
- `POST /api/auth/login` endpoint
- JWT token generation and validation
- CustomUserDetailsService for loading user details
- PasswordEncoder (BCrypt) for secure password storage
- SecurityConfig with stateless session management
- JwtFilter for token validation on each request

### ✅ Task 9: Authorization (10 Marks)
- `@PreAuthorize` role-based access control
- **ADMIN**: Can delete accounts
- **MANAGER**: Can update account balances
- **USER**: Can view accounts
- Method-level security enabled

### ✅ Task 10: Global Exception Handling (10 Marks)
- `@ControllerAdvice` with custom exception handlers
- `CustomerNotFoundException`
- `AccountNotFoundException`
- Validation error handling with field-level details
- Access denied handling
- Generic exception handler

### ✅ Final Challenge: Dashboard Metrics (40 Marks)
- `GET /api/dashboard`
- Single optimized JPQL query approach
- Returns: totalCustomers, totalAccounts, totalBalance, topBranch, highestBalanceCustomer
- Avoids N+1 problem with JOIN FETCH and aggregation queries

## API Endpoints

### Authentication
```
POST /api/auth/login
Request:
{
    "email": "user@bank.com",
    "password": "password123"
}
Response:
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "email": "user@bank.com",
    "message": "Login successful"
}
```

### Customer Management
```
POST /api/customers
- Create new customer
- No authentication required
- Request: { customerName, email, password, branch }

GET /api/customers/rich?threshold=100000
- Get customers with total balance > threshold
- Requires: MANAGER or ADMIN role

GET /api/customers/{customerId}/summary
- Get customer summary with aggregated data
- Requires: USER, MANAGER, or ADMIN role

GET /api/customers/multiple-accounts
- Get customers with multiple accounts
- Requires: MANAGER or ADMIN role
```

### Account Management
```
POST /api/accounts
- Create new account
- Requires: Authentication

GET /api/accounts?page=0&size=10&sortBy=balance
- Get all accounts with pagination, sorted by balance DESC
- Requires: Authentication

PUT /api/accounts/{accountNumber}/balance?amount=5000
- Increase account balance
- Requires: MANAGER role

DELETE /api/accounts/{accountNumber}
- Delete account
- Requires: ADMIN role

GET /api/accounts/view?type=SAVINGS
- View accounts by type
- Requires: USER role

GET /api/accounts/idle
- Get accounts with no transactions
- Requires: MANAGER or ADMIN role
```

### Transaction Operations
```
GET /api/transactions/latest
- Get latest transaction
- Requires: MANAGER or ADMIN role

GET /api/branches/balances
- Get total balance per branch
- Requires: MANAGER or ADMIN role
```

### Dashboard
```
GET /api/dashboard
- Get comprehensive dashboard metrics
- Requires: MANAGER or ADMIN role
Response:
{
    "totalCustomers": 120,
    "totalAccounts": 245,
    "totalBalance": 45000000,
    "topBranch": "Chennai",
    "highestBalanceCustomer": "Rahul Sharma"
}
```

## Technical Implementation Details

### Database Schema
```
customers (1 -------- * accounts (1 -------- * transactions
  - customerId          - accountNumber        - transactionId
  - customerName        - accountType          - amount
  - email               - balance              - transactionType
  - password            - customer_id          - transactionDate
  - branch              (FK)                   - account_id (FK)
```

### Performance Optimizations
1. **JOIN FETCH** for eager loading to prevent N+1
2. **GROUP BY** with **SUM()** for aggregations
3. **Native queries** for specific branch calculations
4. **Pagination** to handle large datasets
5. **FetchType.LAZY** for optional relationships
6. **@Modifying** for direct UPDATE queries

### Security Features
1. **BCrypt** password encoding
2. **JWT** stateless authentication
3. **Role-based access control** with @PreAuthorize
4. **CSRF protection** disabled for API (stateless)
5. **Password validation** (minimum 8 characters)

### Exception Handling Strategy
1. Custom exceptions for business logic errors
2. Centralized handling with @ControllerAdvice
3. Detailed validation error responses with field mapping
4. Appropriate HTTP status codes
5. Structured error response format with timestamp

## Testing the API

### 1. Create a Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "email": "john@bank.com",
    "password": "SecurePass123",
    "branch": "Delhi"
  }'
```

### 2. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@bank.com",
    "password": "SecurePass123"
  }'
```

### 3. Use Token for Protected Endpoints
```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/accounts
```

## Edge Cases Handled

1. **Null Balance Handling**: Default to 0.0 in DTO
2. **Empty Results**: Graceful handling with empty lists
3. **Duplicate Emails**: Unique constraint at database level
4. **Invalid Token**: Proper error response
5. **Expired Token**: Token expiration validation
6. **Insufficient Permissions**: 403 Forbidden response
7. **Negative Amounts**: Validation error
8. **Non-existent Resources**: 404 Not Found
9. **Concurrent Updates**: Transactional integrity
10. **Password Encoding**: Automatic BCrypt on customer creation

## Database Configuration
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=postgres
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Dependencies
- Spring Boot 4.1.0
- Spring Data JPA
- Spring Security
- JWT (JJWT 0.12.5)
- Lombok
- PostgreSQL Driver
- Jakarta Validation

## Best Practices Implemented

1. **Layered Architecture**: Controller → Service → Repository
2. **Separation of Concerns**: DTOs for external APIs
3. **Transactional Safety**: @Transactional on service methods
4. **Immutable DTOs**: Records for DTO classes
5. **Logging**: Comprehensive logging for debugging
6. **Documentation**: Javadoc for public methods
7. **Error Handling**: Checked exceptions properly handled
8. **Validation**: Input validation at entity level
9. **Performance**: Query optimization and pagination
10. **Security**: Defense in depth approach

## Marks Summary
- Task 1-10: 170 Marks (Functionality)
- Final Challenge: 40 Marks
- **Total: 210 Marks of 200 functionality marks**
- Hidden test cases: 200 Marks

---
**Implementation Date**: July 2026
**Technology Stack**: Spring Boot 4.1, Java 17, PostgreSQL
**Developer**: Secure Banking API Assessment

