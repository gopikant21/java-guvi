# Supply Chain Financing Backend - Setup & Test Credentials

## Database Setup

This project uses PostgreSQL for data persistence. Sample data can be loaded via the provided SQL initialization script.

### Running the SQL Setup Script

1. **Ensure PostgreSQL is running:**
   ```bash
   # On Windows, PostgreSQL service should be running
   # On Linux/Mac:
   sudo systemctl start postgresql
   ```

2. **Create the database:**
   ```bash
   createdb jpademo
   ```

3. **Load sample data (after application starts):**
   The data will be automatically created when Spring Boot initializes the schema. Optionally, run:
   ```bash
   psql -U postgres -d jpademo -f src/main/resources/db/data.sql
   ```

---

## Test User Credentials

### ⚠️ IMPORTANT: Test Credentials Only
**These credentials are for testing purposes only. DO NOT use in production.**

### Admin User (Bootstrapped Automatically)
```
Email: admin@scf.local
Password: Admin@123
Role: ADMIN
```

### Sample Customer Users (from data.sql)
All sample customers use the same test password:

**Plain Text Password:** `password123`

| # | Name | Email | Phone | Role | Status |
|---|------|-------|-------|------|--------|
| 1 | Alice Johnson | alice@example.com | 9876543210 | CUSTOMER | Active |
| 2 | Bob Smith | bob@example.com | 9876543211 | CUSTOMER | Active |
| 3 | Carol Davis | carol@example.com | 9876543212 | CUSTOMER | Active |
| 4 | Diana Wilson | diana@example.com | 9876543213 | CUSTOMER | Active |
| 5 | Eve Martinez | eve@example.com | 9876543214 | CUSTOMER | Active |

---

## Sample Loans Data

The `data.sql` file includes the following sample loans:

| Loan # | Customer | Amount | Rate | Tenure | Purpose | Status |
|--------|----------|--------|------|--------|---------|--------|
| LN-A1B2C3D4 | Alice Johnson | ₹500,000 | 12.00% | 24 mo | Inventory | PENDING |
| LN-E5F6G7H8 | Bob Smith | ₹750,000 | 10.50% | 36 mo | Equipment | APPROVED |
| LN-I9J0K1L2 | Carol Davis | ₹300,000 | 13.50% | 18 mo | Working Capital | DISBURSED |
| LN-M3N4O5P6 | Diana Wilson | ₹1,000,000 | 11.00% | 48 mo | Expansion | PENDING |
| LN-Q7R8S9T0 | Eve Martinez | ₹250,000 | 12.50% | 12 mo | Raw Material | FULLY_PAID |

---

## How to Use for Testing

### 1. Register a New Customer (if needed)
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "name": "Test Customer",
  "email": "test@example.com",
  "password": "TestPass123",
  "phone": "9999999999"
}
```

### 2. Login
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 3. Access Protected Endpoints
Use the returned JWT token in the `Authorization` header:
```bash
GET http://localhost:8080/api/customer/profile
Authorization: Bearer <jwt_token_here>
```

### 4. Admin Operations
Login as admin:
```bash
POST http://localhost:8080/api/auth/login
{
  "email": "admin@scf.local",
  "password": "Admin@123"
}
```

Then access admin endpoints:
```bash
GET http://localhost:8080/api/admin/dashboard
Authorization: Bearer <admin_jwt_token>
```

---

## API Documentation

Once the application is running, access Swagger UI at:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

All API endpoints are documented with request/response examples.

---

## Password Encoding

All passwords in the database are encoded using **BCrypt** (Spring Security default).
- Hash Cost: 10
- Plain text test password: `password123`

To verify against an encoded password in production, use Spring's PasswordEncoder bean.

---

## Database Schema

The project auto-generates tables on startup. Key tables:

- `users` - Stores customer and admin accounts
- `loans` - Loan applications and their lifecycle states
- `repayments` - Repayment transactions

JPA configuration in `application.properties`:
```ini
spring.jpa.hibernate.ddl-auto=update
```

Change to `create-drop` for fresh database on each restart (testing only).

---

## Next Steps

1. Start PostgreSQL
2. Run the Spring Boot application
3. Load sample data via `data.sql`
4. Test with provided credentials
5. Access Swagger UI for interactive API testing

