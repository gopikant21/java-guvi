# Deployment & Setup Guide

## Prerequisites
- Java 17+ (JDK)
- Maven 3.8+
- PostgreSQL 12+
- Postman or curl (for API testing)

## Step 1: Database Setup

### PostgreSQL Configuration
```sql
-- Create database
CREATE DATABASE yourdb;

-- Connect to the database
\c yourdb

-- Tables will be auto-created by Hibernate (spring.jpa.hibernate.ddl-auto=update)
```

### Connection Details
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=postgres
spring.datasource.password=admin
```

## Step 2: Build the Application

```bash
# Navigate to project directory
cd codeassessment4-main

# Clean and build
mvn clean build

# Or just compile
mvn clean compile

# Package as JAR
mvn clean package -DskipTests
```

## Step 3: Run the Application

### Option 1: Run with Maven
```bash
mvn spring-boot:run
```

### Option 2: Run JAR
```bash
java -jar target/assessment4-0.0.1-SNAPSHOT.jar
```

### Option 3: IDE Execution
- Open the project in IntelliJ/Eclipse
- Right-click `Assessment4Application.java`
- Select "Run"

### Application Startup
```
Expected output:
✓ Database initialized with sample data
✓ Created 3 customers, 4 accounts, and 4 transactions
Application started on http://localhost:8080
```

## Step 4: Testing the API

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
```

### 2. Login and Get JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "rahul@bank.com",
    "password": "SecurePass123"
  }'

# Response:
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "email": "rahul@bank.com",
    "message": "Login successful"
}
```

### 3. Create an Account (Requires Authentication)
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "accountNumber": "ACC123",
    "accountType": "SAVINGS",
    "balance": 50000.0,
    "customer": { "customerId": 1 }
  }'
```

### 4. Get All Accounts (Paginated)
```bash
curl -X GET "http://localhost:8080/api/accounts?page=0&size=10&sortBy=balance" \
  -H "Authorization: Bearer <token>"
```

### 5. Get Rich Customers
```bash
curl -X GET "http://localhost:8080/api/customers/rich?threshold=100000" \
  -H "Authorization: Bearer <token>"
```

### 6. Get Dashboard Metrics
```bash
curl -X GET http://localhost:8080/api/dashboard \
  -H "Authorization: Bearer <token>"
```

## Step 5: Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BankingApiIntegrationTests

# Run with coverage
mvn clean test jacoco:report
```

## Step 6: Enable Detailed Logging

Update `application.properties`:
```properties
logging.level.org.northernarc.assessment4=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Troubleshooting

### Issue: Connection Refused to PostgreSQL
```
Solution:
1. Check if PostgreSQL is running: sudo systemctl status postgresql
2. Verify connection details in application.properties
3. Ensure database exists: psql -l
```

### Issue: Port 8080 Already in Use
```
Solution:
# Change port in application.properties
server.port=8081

# Or kill process using port 8080
lsof -i :8080
kill -9 <PID>
```

### Issue: JWT Token Validation Fails
```
Solution:
1. Check token format: Bearer <token>
2. Ensure Authorization header is present
3. Verify token hasn't expired (10 hours)
4. Check SECRET_KEY in JwtUtil.java
```

### Issue: Role-Based Access Denied
```
Solution:
1. Ensure user has correct role
2. Check @PreAuthorize annotation
3. Verify roles in CustomUserDetailsService
4. Use /api/auth/login first to get token
```

### Issue: Validation Errors
```
Solution:
1. Check field constraints (@NotBlank, @Email, etc.)
2. Verify input data format
3. Check error response for field-specific messages
4. Minimum password length: 8 characters
5. Email must be valid format
```

## Performance Tuning

### Enable Query Logging
```properties
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=DEBUG
```

### Connection Pool Configuration
```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

### JPA/Hibernate Optimization
```properties
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

## Production Deployment

### Docker Build
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/assessment4-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t banking-api:1.0 .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/yourdb banking-api:1.0
```

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/bank_prod
export SPRING_DATASOURCE_USERNAME=prod_user
export SPRING_DATASOURCE_PASSWORD=<secure-password>
export SERVER_PORT=8080
export JWT_SECRET_KEY=<production-secret-key>
```

### Database Backup
```bash
# Backup
pg_dump yourdb > backup.sql

# Restore
psql -d yourdb < backup.sql
```

## Monitoring

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Add Spring Boot Actuator
Add to pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Update application.properties:
```properties
management.endpoints.web.exposure.include=health,metrics,info
management.endpoint.health.show-details=always
```

## Security Considerations

1. **Change JWT Secret Key**: Update SECRET_KEY in JwtUtil.java
2. **Use HTTPS in Production**: Configure SSL/TLS
3. **Secure Database Credentials**: Use environment variables
4. **Enable CORS if needed**: Add @CrossOrigin to controllers
5. **Rate Limiting**: Consider implementing rate limiting
6. **Input Validation**: All endpoints validate input
7. **SQL Injection Prevention**: Using parameterized queries (JPQL/HQL)
8. **CSRF Protection**: Enabled for form-based endpoints

## Maintenance Tasks

### Regular Backups
```bash
# Daily backup
0 2 * * * pg_dump yourdb | gzip > /backups/db_$(date +\%Y\%m\%d).sql.gz
```

### Log Rotation
```properties
# Logback configuration
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Database Optimization
```sql
-- Vacuum and analyze
VACUUM ANALYZE customers;
VACUUM ANALYZE accounts;
VACUUM ANALYZE transactions;

-- Create indexes
CREATE INDEX idx_customer_branch ON customers(branch);
CREATE INDEX idx_account_type ON accounts(account_type);
CREATE INDEX idx_transaction_date ON transactions(transaction_date);
```

---
**Last Updated**: July 2026
**Version**: 1.0.0-RELEASE

