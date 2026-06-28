# Quick Start Guide

## 🚀 Get Started in 5 Minutes

### Step 1: Prerequisites
```bash
# Verify Java 17+
java -version

# Verify PostgreSQL is running
# Create database if needed
CREATE DATABASE jpademo;
```

### Step 2: Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jpademo
spring.datasource.username=postgres
spring.datasource.password=12345
```

### Step 3: Build & Run
```bash
cd C:\Users\gopi.kant\Java\ narc\module4\day4\libraryjwt

# Build
mvn clean compile

# Run
mvn spring-boot:run
```

**Server starts:** `http://localhost:8080/api-service`

### Step 4: Test Login
```bash
curl -X POST http://localhost:8080/api-service/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "password123"
  }'
```

Copy the `token` from response.

### Step 5: Test API
```bash
# Replace TOKEN with your JWT token
curl -X GET "http://localhost:8080/api-service/api/books" \
  -H "Authorization: Bearer TOKEN"
```

## 📚 Sample Test Credentials

| User | Email | Password | Role |
|------|-------|----------|------|
| Admin | admin@example.com | password123 | ADMIN |
| Manager | rajesh@example.com | password123 | MANAGER |
| User | amit@example.com | password123 | USER |

## 🔗 Important Endpoints

| Method | Endpoint | Role | Purpose |
|--------|----------|------|---------|
| POST | /api/auth/login | - | Get JWT token |
| GET | /api/books | USER+ | List books |
| GET | /api/members/{id}/summary | - | Member details |
| GET | /api/dashboard | MANAGER+ | Analytics |
| POST | /api/fines/record/{...} | MANAGER+ | Record payment |

## 📖 Documentation Files

- **README.md** - Full project documentation
- **TESTING_GUIDE.md** - Comprehensive testing guide
- **IMPLEMENTATION_SUMMARY.md** - Detailed task breakdown
- **Library_Management_API.postman_collection.json** - Postman tests

## 🛠️ Common Commands

### Build without tests
```bash
mvn clean package -DskipTests
```

### Run with specific profile
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Reset database
1. Stop the application
2. Drop and recreate database
3. Restart application (sample data auto-loads)

## ⚡ Key Features

✅ JWT Authentication
✅ Role-Based Access Control (ADMIN, MANAGER, USER)
✅ Advanced JPQL Queries
✅ Pagination & Sorting
✅ Global Exception Handling
✅ DTO Projections
✅ Analytics Dashboard
✅ Data Validation

## 🔍 Logging

View detailed logs:
```properties
logging.level.org.example.libraryjwt=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

## 📊 Architecture

- **Backend:** Spring Boot 4.1.0
- **Database:** PostgreSQL
- **Security:** JWT + Spring Security
- **ORM:** JPA/Hibernate
- **Build:** Maven

## 🆘 Troubleshooting

### Issue: Connection refused
**Solution:** Start PostgreSQL service

### Issue: Port 8080 in use
**Solution:** Change in `application.properties`:
```properties
server.port=8081
```

### Issue: Token expired
**Solution:** Login again to get new token

### Issue: 403 Forbidden
**Solution:** Check user role matches endpoint requirements

## 📞 Support

For detailed information, refer to:
- IMPLEMENTATION_SUMMARY.md - All tasks explained
- TESTING_GUIDE.md - Complete test scenarios
- README.md - API endpoint details

---

**Ready to code?** Start the application and test the endpoints! 🎉

