# Testing & Validation Checklist

## Pre-Testing Setup

- [ ] PostgreSQL database created and running
- [ ] `spring.datasource.url` configured correctly
- [ ] `spring.datasource.username` and `password` set
- [ ] Maven `clean install` successful
- [ ] Application starts without errors
- [ ] Sample data initialized on startup
- [ ] No compilation errors

---

## Task 1: Entity Mapping Verification

### Customer Entity
- [ ] Customer has `customerId` as @Id with GenerationType.IDENTITY
- [ ] `@OneToMany` relationship with Account is properly mapped
- [ ] `mappedBy = "customer"` is set correctly
- [ ] `cascade = CascadeType.ALL` is applied
- [ ] `fetch = FetchType.LAZY` is configured
- [ ] All fields have proper @Column annotations
- [ ] Table name is "customers"

### Account Entity
- [ ] Account has `accountNumber` as @Id
- [ ] `@ManyToOne` relationship with Customer is configured
- [ ] `@JoinColumn(name = "customer_id")` is present
- [ ] `@OneToMany` relationship with Transaction exists
- [ ] `cascade = CascadeType.ALL` for transactions
- [ ] `fetch = FetchType.LAZY` for transactions
- [ ] Table name is "accounts"

### Transaction Entity
- [ ] Transaction has `transactionId` as @Id with auto-generation
- [ ] `@ManyToOne` relationship with Account exists
- [ ] `@JoinColumn(name = "account_id")` is present
- [ ] All required fields are present
- [ ] Table name is "transactions"

### Cascading Behavior
- [ ] Deleting customer cascades to accounts
- [ ] Deleting account cascades to transactions
- [ ] Creating account saves customer changes

---

## Task 2: Validation Testing

### Customer Validation
```bash
# Test: Invalid - blank name
POST /api/customers
{ "customerName": "", "email": "test@bank.com", "password": "Pass123", "branch": "Branch" }
Expected: 400 Bad Request with field error
- [ ] Returns validation error

# Test: Invalid - non-email format
POST /api/customers
{ "customerName": "User", "email": "invalid-email", "password": "Pass123", "branch": "Branch" }
Expected: 400 Bad Request
- [ ] Returns email validation error

# Test: Invalid - short password (< 8 chars)
POST /api/customers
{ "customerName": "User", "email": "test@bank.com", "password": "short", "branch": "Branch" }
Expected: 400 Bad Request
- [ ] Returns password length validation error

# Test: Valid customer
POST /api/customers
{ "customerName": "John Doe", "email": "john@bank.com", "password": "SecurePass123", "branch": "Delhi" }
Expected: 201 Created
- [ ] Customer created successfully
```

### Account Validation
```bash
# Test: Negative balance
POST /api/accounts
{ "accountNumber": "ACC1", "accountType": "SAVINGS", "balance": -5000, "customer": { "customerId": 1 } }
Expected: 400 Bad Request
- [ ] Returns balance validation error

# Test: Null balance
POST /api/accounts
{ "accountNumber": "ACC1", "accountType": "SAVINGS", "balance": null, "customer": { "customerId": 1 } }
Expected: 400 Bad Request
- [ ] Returns null validation error
```

---

## Task 3: Derived Query Methods Testing

### Derived Query Tests
```bash
# Test: findByAccountType
GET /api/accounts/view?type=SAVINGS
Expected: 200 OK with list of SAVINGS accounts
- [ ] Returns correct account type
- [ ] Only SAVINGS accounts returned

# Test: findByBranch
Query: customerRepository.findByBranch("Chennai")
Expected: List of customers from Chennai branch
- [ ] Branch name matches

# Test: findByBalanceGreaterThan
Query: accountRepository.findByBalanceGreaterThan(50000)
Expected: Only accounts with balance > 50000
- [ ] All returned accounts have balance > 50000

# Test: findByTransactionType
Query: transactionRepository.findByTransactionType("DEPOSIT")
Expected: Only DEPOSIT transactions
- [ ] All transactions are DEPOSIT type
```

---

## Task 4: JPQL Query Testing

### Rich Customers Query
```bash
GET /api/customers/rich?threshold=100000
Expected: Customers with total balance > 100000
- [ ] Returns customers meeting threshold
- [ ] Total balance calculated correctly
- [ ] No N+1 problem (check logs)
```

### Total Balance Per Branch
```bash
GET /api/branches/balances
Expected: { "Chennai": 450000, "Mumbai": 320000, ... }
- [ ] All branches included
- [ ] Balances aggregated correctly
- [ ] Ordered by balance DESC
```

### Customers With Multiple Accounts
```bash
GET /api/customers/multiple-accounts
Expected: Only customers with 2+ accounts
- [ ] COUNT(accounts) > 1 filter applied
- [ ] Ordered by account count DESC
- [ ] No duplicates in result
```

### Latest Transaction
```bash
GET /api/transactions/latest
Expected: { "transactionId": 4, "amount": 15000, "transactionDate": "2026-07-01", ... }
- [ ] Latest transaction returned
- [ ] Sorted by date DESC
- [ ] Only one result returned
```

### Accounts With No Transactions
```bash
GET /api/accounts/idle
Expected: Accounts without any transactions
- [ ] LEFT JOIN used (check SQL)
- [ ] Only idle accounts returned
- [ ] No null pointers
```

---

## Task 5: Update Query Testing

```bash
PUT /api/accounts/ACC001/balance?amount=5000
Authorization: Bearer <MANAGER_TOKEN>
Expected: 204 No Content
- [ ] Balance increased by 5000
- [ ] Database updated
- [ ] No duplicate updates

# Test: Negative amount validation
PUT /api/accounts/ACC001/balance?amount=-1000
Expected: 400 Bad Request
- [ ] Negative amount rejected

# Test: Non-existent account
PUT /api/accounts/NONEXISTENT/balance?amount=1000
Expected: 404 Not Found
- [ ] Error message indicates account not found
```

---

## Task 6: Pagination Testing

```bash
# Test: Default pagination
GET /api/accounts
Expected: First 10 accounts sorted by balance DESC
- [ ] Returns Page object
- [ ] totalElements count correct
- [ ] Sorted by balance descending

# Test: Custom page size
GET /api/accounts?page=0&size=5
Expected: 5 accounts per page
- [ ] Page size respected
- [ ] Total elements accurate

# Test: Second page
GET /api/accounts?page=1&size=10
Expected: Next 10 accounts
- [ ] Correct offset applied
- [ ] No overlapping results

# Test: Custom sort
GET /api/accounts?page=0&size=10&sortBy=accountNumber
Expected: Sorted by account number DESC
- [ ] Sort applied correctly
```

---

## Task 7: DTO Mapping Testing

```bash
GET /api/customers/1/summary
Expected: { "customerName": "John", "branch": "Delhi", "numberOfAccounts": 2, "totalBalance": 150000 }
- [ ] No full customer entity returned
- [ ] Aggregations correct
- [ ] All fields populated
- [ ] No lazy loading issues

# Test: Non-existent customer
GET /api/customers/9999/summary
Expected: 404 Not Found
- [ ] Error message indicates customer not found
```

---

## Task 8: JWT Authentication Testing

### Login Flow
```bash
# Step 1: Create user
POST /api/customers
{ "customerName": "Auth Test", "email": "authtest@bank.com", "password": "SecurePass123", "branch": "Delhi" }
Expected: 201 Created
- [ ] Customer created

# Step 2: Login
POST /api/auth/login
{ "email": "authtest@bank.com", "password": "SecurePass123" }
Expected: 200 OK with JWT token
Response: { "token": "eyJ...", "email": "authtest@bank.com", "message": "Login successful" }
- [ ] Token returned
- [ ] Format is valid JWT
- [ ] Email in response

# Step 3: Use token for protected endpoint
GET /api/accounts
Authorization: Bearer <TOKEN>
Expected: 200 OK with account list
- [ ] Token accepted
- [ ] Data returned

# Step 4: Token expiration
Wait 10+ hours (or modify expiration in JwtUtil)
GET /api/accounts
Authorization: Bearer <EXPIRED_TOKEN>
Expected: 401 Unauthorized
- [ ] Expired token rejected

# Step 5: Invalid credentials
POST /api/auth/login
{ "email": "authtest@bank.com", "password": "WrongPassword" }
Expected: 401 Unauthorized
- [ ] Invalid credentials rejected

# Step 6: Invalid token format
GET /api/accounts
Authorization: Bearer invalid-token-format
Expected: 401 Unauthorized or error
- [ ] Invalid token format rejected
```

---

## Task 9: Authorization Testing

### Admin Role Tests
```bash
# Setup: Create admin user (modify CustomUserDetailsService to assign ADMIN role)
# Test: Admin can delete account
DELETE /api/accounts/ACC001
Authorization: Bearer <ADMIN_TOKEN>
Expected: 204 No Content
- [ ] Account deleted

# Test: Non-admin cannot delete
DELETE /api/accounts/ACC002
Authorization: Bearer <USER_TOKEN>
Expected: 403 Forbidden
- [ ] Access denied message shown
```

### Manager Role Tests
```bash
# Test: Manager can update balance
PUT /api/accounts/ACC001/balance?amount=5000
Authorization: Bearer <MANAGER_TOKEN>
Expected: 204 No Content
- [ ] Balance updated

# Test: Non-manager cannot update
PUT /api/accounts/ACC001/balance?amount=5000
Authorization: Bearer <USER_TOKEN>
Expected: 403 Forbidden
- [ ] Access denied
```

### User Role Tests
```bash
# Test: User can view accounts
GET /api/accounts/view?type=SAVINGS
Authorization: Bearer <USER_TOKEN>
Expected: 200 OK with account list
- [ ] Can view accounts

# Test: User cannot delete
DELETE /api/accounts/ACC001
Authorization: Bearer <USER_TOKEN>
Expected: 403 Forbidden
- [ ] Cannot delete without ADMIN
```

---

## Task 10: Exception Handling Testing

### CustomerNotFoundException
```bash
GET /api/customers/9999/summary
Expected: 404 Not Found
Response: { "status": 404, "message": "Customer not found with ID: 9999", "timestamp": "..." }
- [ ] Correct status code
- [ ] Proper error message
- [ ] Timestamp included
```

### AccountNotFoundException
```bash
DELETE /api/accounts/NONEXISTENT
Authorization: Bearer <ADMIN_TOKEN>
Expected: 404 Not Found
Response: { "status": 404, "message": "Account not found with number: NONEXISTENT", "timestamp": "..." }
- [ ] Correct status code
- [ ] Proper error message
```

### Validation Errors
```bash
POST /api/customers
{ "customerName": "" }
Expected: 400 Bad Request
Response: { "status": 400, "message": "Validation failed", "errors": { "customerName": "Customer name cannot be blank", ... }, "timestamp": "..." }
- [ ] Field errors included
- [ ] Descriptive messages
```

### Access Denied
```bash
DELETE /api/accounts/ACC001
Authorization: Bearer <USER_TOKEN>
Expected: 403 Forbidden
Response: { "status": 403, "message": "Access Denied: ..." }
- [ ] Proper status code
- [ ] Clear error message
```

---

## Final Challenge: Dashboard Metrics

```bash
GET /api/dashboard
Authorization: Bearer <MANAGER_TOKEN>
Expected: { 
    "totalCustomers": 3,
    "totalAccounts": 4,
    "totalBalance": 425000,
    "topBranch": "Chennai",
    "highestBalanceCustomer": "Customer with highest balance"
}

Verification:
- [ ] Total customers count correct
- [ ] Total accounts count correct
- [ ] Total balance sum correct
- [ ] Top branch identified correctly
- [ ] Highest balance customer identified
- [ ] Single or minimum queries used (check logs)
- [ ] No N+1 problem
- [ ] Response time < 100ms

# Performance test: Multiple calls
- [ ] No repeated queries
- [ ] Consistent results
- [ ] No memory leaks
```

---

## Integration & End-to-End Testing

### Complete User Journey
```
1. [ ] Create Customer
   POST /api/customers
   
2. [ ] Login
   POST /api/auth/login
   
3. [ ] Create Account
   POST /api/accounts
   
4. [ ] Add Transaction (if endpoint exists)
   (Verify via transaction queries)
   
5. [ ] View Account Summary
   GET /api/accounts
   
6. [ ] Get Customer Summary
   GET /api/customers/{id}/summary
   
7. [ ] Query Rich Customers
   GET /api/customers/rich?threshold=50000
   
8. [ ] Check Branch Balances
   GET /api/branches/balances
   
9. [ ] View Dashboard
   GET /api/dashboard
   
10. [ ] Test Authorization
    (Update balance, delete account, etc.)
```

---

## Security Testing

- [ ] JWT secret key is properly configured
- [ ] Passwords are BCrypt encoded (not plaintext)
- [ ] No sensitive data in logs
- [ ] CSRF disabled for API
- [ ] SQL injection prevented (parameterized queries)
- [ ] XSS prevention via Spring Security
- [ ] Authentication header required for protected endpoints
- [ ] Token validation working

---

## Performance Testing

- [ ] No N+1 queries (check logs)
- [ ] Pagination works with large datasets
- [ ] Dashboard query completes in < 100ms
- [ ] JOIN FETCH prevents lazy loading issues
- [ ] Update queries direct (not SELECT then UPDATE)
- [ ] Indexes on foreign keys
- [ ] Connection pooling configured

---

## Database Consistency

- [ ] Foreign key constraints enforced
- [ ] Cascade delete works correctly
- [ ] No orphaned records
- [ ] Unique constraints (email, account number)
- [ ] Not-null constraints respected
- [ ] Data integrity maintained

---

## Cross-Browser/Client Testing

- [ ] Curl requests work
- [ ] Postman collections work
- [ ] JavaScript fetch API works
- [ ] Mobile client compatible
- [ ] Content-Type headers correct
- [ ] Responses in valid JSON

---

## Final Verification

- [ ] All 10 tasks completed
- [ ] Final challenge implemented
- [ ] All unit tests pass: `mvn test`
- [ ] No compilation warnings
- [ ] No runtime errors in logs
- [ ] API documentation complete
- [ ] README updated
- [ ] Code follows best practices
- [ ] No hardcoded secrets in code
- [ ] Application starts without warnings

---

## Sign-Off

- [ ] Code review completed
- [ ] All tests passed
- [ ] Performance acceptable
- [ ] Security verified
- [ ] Documentation complete
- [ ] Ready for deployment

---

**Test Date**: ________________
**Tested By**: ________________
**Status**: ☐ PASS ☐ FAIL
**Notes**: ________________________________________

---

