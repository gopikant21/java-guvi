# Complete Testing Guide - Library Management API

## Prerequisites

1. **PostgreSQL Database**
   - Create database: `CREATE DATABASE jpademo;`
   - Verify connection on localhost:5432

2. **Java 17+** installed
3. **Maven 3.8+** installed
4. **Postman** for API testing (optional)

## Setup Steps

### 1. Clone/Extract Project
```bash
cd C:\Users\gopi.kant\Java\ narc\module4\day4\libraryjwt
```

### 2. Update Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jpademo
spring.datasource.username=postgres
spring.datasource.password=12345
```

### 3. Build Project
```bash
mvn clean package -DskipTests
```

### 4. Run Application
```bash
mvn spring-boot:run
```

Server starts on: `http://localhost:8080/api-service`

## Default Test Users

| Role    | Email                 | Password    | Permissions                      |
|---------|----------------------|-------------|----------------------------------|
| ADMIN   | admin@example.com    | password123 | All operations                   |
| MANAGER | rajesh@example.com   | password123 | Books, issues, fines management  |
| USER    | amit@example.com     | password123 | View books, issue history        |

## Test Workflow

### Phase 1: Authentication

#### 1.1 Login as Admin
```bash
curl -X POST http://localhost:8080/api-service/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "memberId": 4,
  "memberName": "Admin User",
  "email": "admin@example.com",
  "role": "ADMIN"
}
```

**Save the token** for subsequent requests.

#### 1.2 Register New Member
```bash
curl -X POST http://localhost:8080/api-service/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "memberName": "Test User",
    "email": "test@example.com",
    "password": "newpass123",
    "membershipBranch": "Test Branch"
  }'
```

### Phase 2: Book Management Tests

#### 2.1 Get All Books (Paginated)
```bash
curl -X GET "http://localhost:8080/api-service/api/books?page=0&size=5&sort=dailyFineRate,desc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** 5 books sorted by fine rate (descending)

#### 2.2 Filter Books by Type
```bash
curl -X GET "http://localhost:8080/api-service/api/books/type/ACADEMIC" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Only ACADEMIC books returned

#### 2.3 Find Books with Higher Fine Rate
```bash
curl -X GET "http://localhost:8080/api-service/api/books/fine-rate/5.0" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Books with dailyFineRate > 5.0

#### 2.4 Get Books with No Fine History
```bash
curl -X GET "http://localhost:8080/api-service/api/books/no-fines" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Books without any fine transactions

#### 2.5 Add New Book (Admin only)
```bash
curl -X POST http://localhost:8080/api-service/api/books \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-0060850524",
    "title": "The Catcher in the Rye",
    "bookType": "FICTION",
    "dailyFineRate": 2.50
  }'
```

**Expected:** 201 Created with book details

#### 2.6 Increase Fine Rates (Manager/Admin only)
```bash
curl -X PUT "http://localhost:8080/api-service/api/books/fine-rates/increase/ACADEMIC" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** All ACADEMIC books' fine rates increased by 10%

### Phase 3: Member Management Tests

#### 3.1 Get Member Summary
```bash
curl -X GET http://localhost:8080/api-service/api/members/1/summary \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected Response:**
```json
{
  "memberId": 1,
  "memberName": "Amit Patel",
  "membershipBranch": "Central Library",
  "numberOfBorrowedBooks": 4,
  "totalFinesPaid": 105.0
}
```

#### 3.2 Get Members by Branch
```bash
curl -X GET "http://localhost:8080/api-service/api/members/branch/Central Library" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Members from specified branch

#### 3.3 Find Avid Readers (2+ checkouts)
```bash
curl -X GET http://localhost:8080/api-service/api/members/avid-readers/2 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Members with > 2 issue records

#### 3.4 Find Members with Multiple Genres
```bash
curl -X GET http://localhost:8080/api-service/api/members/multi-genre/2 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Members borrowing 2+ different book types

### Phase 4: Issue Management Tests

#### 4.1 Issue Book to Member
```bash
curl -X POST http://localhost:8080/api-service/api/issues/issue/1/978-0134685991 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** IssueRecord created with ISSUED status

#### 4.2 Return Book
```bash
curl -X PUT http://localhost:8080/api-service/api/issues/return/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** IssueRecord updated with RETURNED status and returnDate

#### 4.3 Get Member's Issue History
```bash
curl -X GET http://localhost:8080/api-service/api/issues/member/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** All issue records for member

### Phase 5: Fine Management Tests

#### 5.1 Record Fine Payment
```bash
curl -X POST "http://localhost:8080/api-service/api/fines/record/1/978-0134685991/50/CARD" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** FineTransaction created with amount and paymentType

#### 5.2 Get Fines by Payment Type
```bash
curl -X GET http://localhost:8080/api-service/api/fines/payment-type/CARD \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** All fines paid via CARD

#### 5.3 Get Latest Fine Payment
```bash
curl -X GET http://localhost:8080/api-service/api/fines/latest \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected:** Most recent FineTransaction

#### 5.4 Get Total Fines Per Branch
```bash
curl -X GET http://localhost:8080/api-service/api/fines/per-branch \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected Response:**
```json
[
  ["Central Library", 150.0],
  ["North Branch", 45.0],
  ["Admin Office", null]
]
```

### Phase 6: Analytics & Dashboard

#### 6.1 Get Dashboard Analytics
```bash
curl -X GET http://localhost:8080/api-service/api/dashboard \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Expected Response (Final Challenge):**
```json
{
  "totalMembers": 4,
  "totalBooks": 5,
  "totalFinesCollected": 150.0,
  "topBranch": "Central Library",
  "highestFinePayingMember": "Amit Patel"
}
```

## Authorization Tests

### Test Role-Based Access Control

#### Admin Can Delete Books
```bash
curl -X DELETE http://localhost:8080/api-service/api/books/978-0134685991 \
  -H "Authorization: Bearer ADMIN_TOKEN"
```
**Expected:** 200 OK (only for ADMIN)

#### Manager Can Update Fine Rates
```bash
curl -X PUT "http://localhost:8080/api-service/api/books/fine-rates/increase/FICTION" \
  -H "Authorization: Bearer MANAGER_TOKEN"
```
**Expected:** 200 OK (for MANAGER/ADMIN)

#### User Can View Books
```bash
curl -X GET http://localhost:8080/api-service/api/books \
  -H "Authorization: Bearer USER_TOKEN"
```
**Expected:** 200 OK (all roles)

## Error Handling Tests

### Test 1: Missing JWT Token
```bash
curl -X GET http://localhost:8080/api-service/api/books
```
**Expected:** 403 Forbidden (Unauthorized)

### Test 2: Invalid Member ID
```bash
curl -X GET http://localhost:8080/api-service/api/members/9999/summary \
  -H "Authorization: Bearer YOUR_TOKEN"
```
**Expected:** 404 Not Found with error message

### Test 3: Invalid Book ISBN
```bash
curl -X GET http://localhost:8080/api-service/api/books/invalid-isbn \
  -H "Authorization: Bearer YOUR_TOKEN"
```
**Expected:** 404 Not Found with error message

### Test 4: Validation Error
```bash
curl -X POST http://localhost:8080/api-service/api/books \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-0060850524",
    "title": "",
    "bookType": "FICTION",
    "dailyFineRate": -5
  }'
```
**Expected:** 400 Bad Request with validation errors

## Pagination & Sorting Tests

### Test Different Pagination Parameters
```bash
# Page 1, 5 items per page
curl -X GET "http://localhost:8080/api-service/api/books?page=1&size=5" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Sorted by title ascending
curl -X GET "http://localhost:8080/api-service/api/books?sort=title,asc" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Multiple sort criteria
curl -X GET "http://localhost:8080/api-service/api/books?sort=bookType,asc&sort=dailyFineRate,desc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Performance & N+1 Query Tests

### Monitor SQL Queries
Check logs for:
1. Dashboard should execute exactly 5 queries
2. No N+1 queries in list endpoints
3. Efficient JOIN queries in analytics

Example from logs:
```
SELECT COUNT(m) FROM Member m
SELECT COUNT(b) FROM Book b
SELECT SUM(ft.amount) FROM FineTransaction ft
SELECT m.membershipBranch, SUM(ft.amount) FROM Member m ...
SELECT member_id, member_name, SUM(amount) FROM fine_transactions ...
```

## Integration Test Checklist

- [x] Task 1: Entity relationships validated
- [x] Task 2: Validation working on all entities
- [x] Task 3: Derived queries returning expected results
- [x] Task 4: JPQL queries returning correct data
- [x] Task 5: Batch update query working
- [x] Task 6: Pagination and sorting operational
- [x] Task 7: DTOs properly mapping fields
- [x] Task 8: JWT authentication working
- [x] Task 9: Role-based authorization enforced
- [x] Task 10: Exception handling standardized
- [x] Final Challenge: Dashboard optimized

## Load Testing

### Simulate Multiple Requests
```bash
# Test concurrent requests
for i in {1..10}; do
  curl -X GET "http://localhost:8080/api-service/api/books?page=$((i%5))" \
    -H "Authorization: Bearer YOUR_TOKEN" &
done
```

## Cleanup & Reset

### Reset Database
1. Stop application
2. Drop and recreate database:
```bash
DROP DATABASE jpademo;
CREATE DATABASE jpademo;
```
3. Restart application (new sample data will be created)

## Troubleshooting

### Issue: Connection refused
**Solution:** Ensure PostgreSQL is running on localhost:5432

### Issue: Token validation fails
**Solution:** Token may be expired or JWT secret key mismatch

### Issue: 403 Forbidden on protected endpoint
**Solution:** Check user role matches required role

### Issue: 401 Unauthorized
**Solution:** Provide valid JWT token in Authorization header

## Logging

Enable debug logging for detailed query tracking:
```properties
logging.level.org.example.libraryjwt=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## Summary

This comprehensive testing guide covers:
1. ✅ All 10 main tasks with test cases
2. ✅ Final Challenge (Dashboard) validation
3. ✅ Authorization & authentication flows
4. ✅ Error scenarios and handling
5. ✅ Pagination and sorting verification
6. ✅ Performance optimization checks
7. ✅ Integration testing checklist

