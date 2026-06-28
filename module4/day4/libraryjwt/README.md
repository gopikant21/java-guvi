# Secure Library Management API - Spring Boot

A comprehensive Spring Boot application demonstrating secure REST API development with JWT authentication, JPA/JPQL queries, role-based access control, and advanced analytics.

## Project Overview

This project implements a complete library management system with the following features:

### Core Features

1. **Entity Mapping (Task 1)**
   - Properly configured JPA relationships using `@OneToMany`, `@ManyToOne`
   - Cascade types and orphan removal for data integrity
   - Safe initialization of collections to prevent NullPointerException

2. **Validation (Task 2)**
   - Bean Validation annotations (`@NotBlank`, `@Email`, `@Positive`, `@PositiveOrZero`, `@Size`, `@NotNull`)
   - Applied across all entity models for data consistency

3. **Spring Data JPA Derived Queries (Task 3)**
   - `findByBookType(String bookType)`
   - `findByMembershipBranch(String branch)`
   - `findByPaymentType(String paymentType)`
   - `findByDailyFineRateGreaterThan(double amount)`

4. **JPQL Queries (Task 4)**
   - Find Avid Readers: Members with checkout history > target count
   - Total Fines Per Branch: Aggregated fine collection by branch using GROUP BY and SUM()
   - Members with Multi-Genre Books: COUNT(DISTINCT) with GROUP BY and HAVING
   - Latest Fine Payment: ORDER BY DESC with LIMIT
   - Books with No Fine History: LEFT JOIN strategy

5. **JPQL Update Query (Task 5)**
   - `increaseDailyFineRates()`: Batch update for book categories using @Modifying
   - Transactional support for atomic operations

6. **Pagination & Sorting (Task 6)**
   - GET `/api/books`: Dynamic Pageable with default sort by `dailyFineRate DESC`
   - Spring Data Pagination support

7. **DTO Projection Mapping (Task 7)**
   - `MemberSummaryDTO`: Custom projection with calculated fields
   - Prevents exposure of sensitive domain entities to clients

8. **JWT Authentication (Task 8)**
   - `JwtUtil`: Token generation and validation
   - `JwtFilter`: Per-request token validation
   - `CustomUserDetailsService`: User details lookup
   - `SecurityConfig`: Spring Security configuration
   - POST `/api/auth/login`: Authentication endpoint (permitAll)
   - POST `/api/auth/register`: New member registration

9. **Role-Based Authorization (Task 9)**
   - ADMIN: Can add and delete books
   - MANAGER: Can update fine rates, manage issues
   - USER: Can view catalog and issue history
   - `@PreAuthorize` annotations on all endpoints

10. **Global Exception Handling (Task 10)**
    - `GlobalExceptionHandler`: Unified @ControllerAdvice
    - Handles `MemberNotFoundException`, `BookNotFoundException`, `ValidationException`
    - Standardized error response format

11. **Dashboard Analytics (Final Challenge)**
    - GET `/api/dashboard`: Unified analytics in single execution
    - Optimized queries to prevent N+1 query problem
    - Returns: totalMembers, totalBooks, totalFinesCollected, topBranch, highestFinePayingMember

## Database Schema

```
Member (1) ──────────── (∞) IssueRecord ──────────── (1) Book
   │                                                      │
   └──────────────────────── (∞) FineTransaction ─────────┘
```

### Entities

- **Member**: Library members with roles (USER, MANAGER, ADMIN)
- **Book**: Library books with categories (FICTION, ACADEMIC, REFERENCE)
- **IssueRecord**: Book circulation history with status tracking
- **FineTransaction**: Fine payment records with payment methods

## API Endpoints

### Authentication
- `POST /api/auth/login` - Login and get JWT token
- `POST /api/auth/register` - Register new member

### Books (Requires Authentication)
- `GET /api/books` - List books with pagination & sorting
- `GET /api/books/type/{bookType}` - Filter by book type
- `GET /api/books/fine-rate/{amount}` - Find books with higher fines
- `GET /api/books/no-fines` - Books with no fine history
- `POST /api/books` - Add book (ADMIN only)
- `DELETE /api/books/{isbn}` - Delete book (ADMIN only)
- `PUT /api/books/fine-rates/increase/{bookType}` - Increase fines (MANAGER/ADMIN)

### Members
- `GET /api/members/{memberId}/summary` - Member summary DTO
- `GET /api/members/branch/{branch}` - Members by branch
- `GET /api/members/avid-readers/{targetCount}` - Avid readers
- `GET /api/members/multi-genre/{genreCount}` - Multi-genre borrowers

### Issues
- `POST /api/issues/issue/{memberId}/{isbn}` - Issue book
- `PUT /api/issues/return/{issueId}` - Return book
- `GET /api/issues/member/{memberId}` - Member's issue history

### Fines
- `POST /api/fines/record/{memberId}/{isbn}/{amount}/{paymentType}` - Record payment
- `GET /api/fines/payment-type/{paymentType}` - Fines by payment type
- `GET /api/fines/latest` - Latest payment
- `GET /api/fines/per-branch` - Fines per branch

### Analytics
- `GET /api/dashboard` - Dashboard overview (MANAGER/ADMIN only)

## Technologies Used

- **Java 17**
- **Spring Boot 4.1.0**
- **Spring Data JPA**
- **Spring Security with JWT**
- **PostgreSQL**
- **Lombok**
- **Jakarta Bean Validation**
- **JJWT (JSON Web Token Library)**

## Setup Instructions

### Prerequisites
- Java 17+
- PostgreSQL 12+
- Maven 3.8+

### Configuration

1. **Create PostgreSQL Database**
```sql
CREATE DATABASE jpademo;
```

2. **Update application.properties**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jpademo
spring.datasource.username=postgres
spring.datasource.password=12345
jwt.secret.key=your-super-secret-key-that-is-at-least-32-characters-long-for-hs512!
```

3. **Build the Project**
```bash
mvn clean package
```

4. **Run the Application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api-service`

## Sample Test Credentials

**Admin User:**
- Email: admin@example.com
- Password: password123
- Role: ADMIN

**Manager User:**
- Email: rajesh@example.com
- Password: password123
- Role: MANAGER

**Regular User:**
- Email: amit@example.com
- Password: password123
- Role: USER

## Testing Workflow

### 1. Login
```bash
curl -X POST http://localhost:8080/api-service/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

### 2. Use Token in Requests
```bash
curl -X GET http://localhost:8080/api-service/api/books \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### 3. Access Dashboard
```bash
curl -X GET http://localhost:8080/api-service/api/dashboard \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

## Key Implementation Details

### N+1 Query Prevention
- Service layer methods use repository JPQL queries
- Eager loading where necessary, lazy loading for large relationships
- Batch operations for multiple updates

### Security Best Practices
- Passwords encrypted using BCrypt
- JWT tokens with expiration (1 hour default)
- Role-based authorization on every endpoint
- Protected sensitive operations with @PreAuthorize

### Code Quality
- Comprehensive logging throughout
- Exception handling at global level
- DTO mapping for API responses
- Proper HTTP status codes
- Validation on all inputs

### Database Optimization
- Proper indexing via PK/FK relationships
- Cascade types configured appropriately
- Orphan removal for data consistency
- Query optimization with fetch strategies

## Performance Considerations

1. **Pagination**: Books endpoint supports pagination to limit data transfer
2. **Sorting**: Default sort by dailyFineRate for relevant ordering
3. **Lazy Loading**: Many-to-One relationships use lazy fetch
4. **Batch Updates**: Modifying queries for bulk operations
5. **DTO Projection**: Custom queries only return needed fields

## Error Handling

All exceptions are handled globally with standardized responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Member Not Found",
  "message": "Member not found with ID: 999"
}
```

## Coding Standards Applied

- ✅ Proper naming conventions
- ✅ Comprehensive JavaDoc comments
- ✅ Consistent code formatting
- ✅ Single Responsibility Principle
- ✅ DRY (Don't Repeat Yourself)
- ✅ Proper exception handling
- ✅ Security best practices
- ✅ Database optimization
- ✅ Clean architecture

## Testing Endpoints

A Postman collection is available in the project documentation for testing all endpoints.

## Future Enhancements

- Email notifications for overdue books
- Member subscription levels
- Advanced analytics and reporting
- Book recommendation engine
- Mobile app integration
- Caching layer for frequently accessed data

## License

This project is part of a Spring Boot assessment demonstrating enterprise-level development practices.

## Contact

For questions or issues, please refer to the project documentation or contact the development team.

