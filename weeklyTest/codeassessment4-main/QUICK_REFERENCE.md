# Quick API Reference Guide

## 🔧 Setup & Configuration

```bash
# 1. Create PostgreSQL Database
createdb yourdb

# 2. Build Project
cd codeassessment4-main
mvn clean build

# 3. Start Application
mvn spring-boot:run
# OR
java -jar target/assessment4-0.0.1-SNAPSHOT.jar

# Application runs on: http://localhost:8080
```

---

## 🔐 Authentication Flow

### 1. Create a Customer
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Rahul Sharma",
    "email": "rahul@bank.com",
    "password": "SecurePass123",
    "branch": "Chennai"
  }'

# Response: 201 Created
# {
#   "customerId": 1,
#   "customerName": "Rahul Sharma",
#   "email": "rahul@bank.com",
#   "branch": "Chennai",
#   "accounts": []
# }
```

### 2. Login to Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rahul@bank.com",
    "password": "SecurePass123"
  }'

# Response: 200 OK
# {
#   "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWh1bEBiYW5rLmNvbSIsImlhdCI6MTcyMDA2MzIwMCwiZXhwIjoxNzIwMDk3MjAwLCJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dfQ.xxxxx",
#   "email": "rahul@bank.com",
#   "message": "Login successful"
# }

# Save the token for further requests
export TOKEN="eyJhbGciOiJIUzI1NiJ9...."
```

---

## 👤 Customer Management

### Create Customer (No Auth Required)
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Priya Patel",
    "email": "priya@bank.com",
    "password": "SecurePass456",
    "branch": "Mumbai"
  }'
```

### Get Customer Summary (Auth Required)
```bash
curl -X GET http://localhost:8080/api/customers/1/summary \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "customerName": "Rahul Sharma",
#   "branch": "Chennai",
#   "numberOfAccounts": 2,
#   "totalBalance": 150000
# }
```

### Get Rich Customers (Manager+ Required)
```bash
curl -X GET "http://localhost:8080/api/customers/rich?threshold=50000" \
  -H "Authorization: Bearer $TOKEN"

# Returns customers with total balance > 50000
```

### Get Customers With Multiple Accounts (Manager+ Required)
```bash
curl -X GET http://localhost:8080/api/customers/multiple-accounts \
  -H "Authorization: Bearer $TOKEN"

# Returns customers with 2+ accounts
```

---

## 💰 Account Management

### Create Account (Auth Required)
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "accountNumber": "ACC001",
    "accountType": "SAVINGS",
    "balance": 50000.0,
    "customer": { "customerId": 1 }
  }'

# Response: 201 Created
```

### Get All Accounts (Paginated & Sorted)
```bash
# Default: page 0, size 10, sorted by balance DESC
curl -X GET http://localhost:8080/api/accounts \
  -H "Authorization: Bearer $TOKEN"

# Custom pagination
curl -X GET "http://localhost:8080/api/accounts?page=0&size=5&sortBy=accountNumber" \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "content": [ ... ],
#   "pageable": { ... },
#   "totalElements": 10,
#   "totalPages": 1,
#   "size": 10,
#   "number": 0
# }
```

### View Accounts by Type (User+ Required)
```bash
curl -X GET "http://localhost:8080/api/accounts/view?type=SAVINGS" \
  -H "Authorization: Bearer $TOKEN"

# Returns all SAVINGS accounts
```

### Get Idle Accounts (No Transactions) (Manager+ Required)
```bash
curl -X GET http://localhost:8080/api/accounts/idle \
  -H "Authorization: Bearer $TOKEN"

# Returns accounts with no transactions
```

### Update Account Balance (Manager+ Required)
```bash
curl -X PUT "http://localhost:8080/api/accounts/ACC001/balance?amount=5000" \
  -H "Authorization: Bearer $TOKEN"

# Response: 204 No Content
# Balance increased by 5000
```

### Delete Account (Admin Only)
```bash
curl -X DELETE http://localhost:8080/api/accounts/ACC001 \
  -H "Authorization: Bearer $TOKEN"

# Response: 204 No Content
# Note: Requires ADMIN role
```

---

## 📊 Branch & Balance Queries

### Get Total Balance Per Branch (Manager+ Required)
```bash
curl -X GET http://localhost:8080/api/branches/balances \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "Chennai": 450000,
#   "Mumbai": 320000,
#   "Bangalore": 200000
# }
```

---

## 💳 Transaction Queries

### Get Transactions by Type
```bash
curl -X GET "http://localhost:8080/api/transactions?type=DEPOSIT" \
  -H "Authorization: Bearer $TOKEN"

# Returns all DEPOSIT transactions
```

### Get Latest Transaction (Manager+ Required)
```bash
curl -X GET http://localhost:8080/api/transactions/latest \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "transactionId": 10,
#   "amount": 15000,
#   "transactionType": "TRANSFER",
#   "transactionDate": "2026-07-01",
#   "account": { ... }
# }
```

---

## 📈 Dashboard & Metrics

### Get Dashboard Metrics (Manager+ Required)
```bash
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer $TOKEN"

# Response:
# {
#   "totalCustomers": 120,
#   "totalAccounts": 245,
#   "totalBalance": 45000000,
#   "topBranch": "Chennai",
#   "highestBalanceCustomer": "Rahul Sharma"
# }
```

---

## ⚠️ Error Responses

### 401 Unauthorized (Missing/Invalid Token)
```bash
curl -X GET http://localhost:8080/api/accounts

# Response: 401 Unauthorized
# {
#   "status": 401,
#   "message": "Unauthorized",
#   "timestamp": "2026-07-01T10:30:45"
# }
```

### 403 Forbidden (Insufficient Permissions)
```bash
# As USER, trying ADMIN operation
curl -X DELETE http://localhost:8080/api/accounts/ACC001 \
  -H "Authorization: Bearer $USER_TOKEN"

# Response: 403 Forbidden
# {
#   "status": 403,
#   "message": "Access Denied: ...",
#   "timestamp": "2026-07-01T10:30:45"
# }
```

### 404 Not Found
```bash
curl -X GET http://localhost:8080/api/customers/9999/summary \
  -H "Authorization: Bearer $TOKEN"

# Response: 404 Not Found
# {
#   "status": 404,
#   "message": "Customer not found with ID: 9999",
#   "timestamp": "2026-07-01T10:30:45"
# }
```

### 400 Bad Request (Validation Error)
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "",
    "email": "invalid-email",
    "password": "short",
    "branch": ""
  }'

# Response: 400 Bad Request
# {
#   "status": 400,
#   "message": "Validation failed",
#   "errors": {
#     "customerName": "Customer name cannot be blank",
#     "email": "Email should be valid",
#     "password": "Password must be at least 8 characters",
#     "branch": "Branch cannot be blank"
#   },
#   "timestamp": "2026-07-01T10:30:45"
# }
```

---

## 🔑 Authorization Levels

| Endpoint | USER | MANAGER | ADMIN |
|----------|------|---------|-------|
| POST /customers | ✓ | ✓ | ✓ |
| POST /accounts | ✓ | ✓ | ✓ |
| GET /accounts | ✓ | ✓ | ✓ |
| PUT /accounts/{id}/balance | ✗ | ✓ | ✓ |
| DELETE /accounts/{id} | ✗ | ✗ | ✓ |
| GET /customers/rich | ✗ | ✓ | ✓ |
| GET /branches/balances | ✗ | ✓ | ✓ |
| GET /dashboard | ✗ | ✓ | ✓ |

---

## 📋 Sample Data (Auto-Initialized)

### Customers
1. **Rahul Sharma** (Chennai) - Email: rahul.sharma@bank.com
2. **Priya Patel** (Mumbai) - Email: priya.patel@bank.com
3. **Amit Kumar** (Bangalore) - Email: amit.kumar@bank.com

All use password: `password123`

### Accounts
- ACC001: SAVINGS, Balance: 50,000 (Rahul)
- ACC002: CURRENT, Balance: 100,000 (Rahul)
- ACC003: SAVINGS, Balance: 75,000 (Priya)
- ACC004: CURRENT, Balance: 200,000 (Amit)

### Transactions
- Multiple transactions across accounts

---

## 🧪 Testing Commands

### Full Test Sequence
```bash
#!/bin/bash

# Set variables
EMAIL="rahul.sharma@bank.com"
PASSWORD="password123"
BASE_URL="http://localhost:8080/api"

# 1. Login
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token: $TOKEN"

# 2. Get Accounts
echo "Getting Accounts..."
curl -s -X GET $BASE_URL/accounts \
  -H "Authorization: Bearer $TOKEN" | python -m json.tool

# 3. Get Dashboard
echo "Getting Dashboard..."
curl -s -X GET $BASE_URL/dashboard \
  -H "Authorization: Bearer $TOKEN" | python -m json.tool

# 4. Get Rich Customers
echo "Getting Rich Customers..."
curl -s -X GET "$BASE_URL/customers/rich?threshold=50000" \
  -H "Authorization: Bearer $TOKEN" | python -m json.tool

# 5. Get Branch Balances
echo "Getting Branch Balances..."
curl -s -X GET $BASE_URL/branches/balances \
  -H "Authorization: Bearer $TOKEN" | python -m json.tool
```

---

## 📝 Request/Response Examples

### Example 1: Complete User Flow
```bash
# Step 1: Create Customer
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "email": "john@bank.com",
    "password": "MySecurePass123",
    "branch": "Delhi"
  }'
# ✓ 201 Created

# Step 2: Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@bank.com",
    "password": "MySecurePass123"
  }'
# ✓ 200 OK with token

# Step 3: Create Account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "accountNumber": "ACC123",
    "accountType": "SAVINGS",
    "balance": 100000,
    "customer": { "customerId": 4 }
  }'
# ✓ 201 Created

# Step 4: View Accounts
curl -X GET http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <TOKEN>"
# ✓ 200 OK with paginated results
```

---

## 🚀 Performance Tips

```bash
# For large datasets, use pagination
curl -X GET "http://localhost:8080/api/accounts?page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# Check query performance (with logging enabled)
# Look for "Query took X ms" in logs

# Avoid fetching all data at once
# Instead use page=X&size=10
```

---

## 🔍 Debugging

### Enable Debug Logging
Edit `application.properties`:
```properties
logging.level.org.northernarc.assessment4=DEBUG
logging.level.org.springframework.security=DEBUG
spring.jpa.show-sql=true
```

### Check Database Directly
```bash
psql -U postgres -d yourdb

# View customers
SELECT * FROM customers;

# View accounts
SELECT * FROM accounts;

# View transactions
SELECT * FROM transactions;
```

---

## 📱 Tools for Testing

### Recommended Tools
- **Curl** - Command line HTTP client
- **Postman** - GUI HTTP client with collections
- **Insomnia** - User-friendly REST client
- **Thunder Client** - VS Code extension

### Postman Import
```json
{
  "info": { "name": "Banking API", "version": 1 },
  "baseUrl": "http://localhost:8080/api",
  "endpoints": [
    { "name": "Login", "method": "POST", "url": "/auth/login" },
    { "name": "Get Accounts", "method": "GET", "url": "/accounts" },
    { "name": "Get Dashboard", "method": "GET", "url": "/dashboard" }
  ]
}
```

---

## ✅ Health Check

```bash
# Verify application is running
curl http://localhost:8080/actuator/health

# Response:
# { "status": "UP" }
```

---

**Last Updated**: July 2026
**API Version**: 1.0.0
**Endpoint**: http://localhost:8080

