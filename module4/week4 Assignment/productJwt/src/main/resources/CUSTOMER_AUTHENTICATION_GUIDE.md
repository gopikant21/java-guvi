# Customer Authentication Integration

## Summary of Changes

The application now supports **two authentication sources**:

1. **Database-backed authentication** (primary): Customers from `customer_auth` table
2. **In-memory test users** (fallback): For development/testing

## Login Credentials

### Database Customers (60 total)

All seeded customer credentials follow this pattern:

| Customer ID | Username | Decoded Password | Role |
|-------------|----------|------------------|------|
| 1 | customer001 | Cust@001 | ROLE_ADMIN |
| 2 | customer002 | Cust@002 | ROLE_ADMIN |
| 3 | customer003 | Cust@003 | ROLE_USER |
| ... | ... | ... | ... |
| 60 | customer060 | Cust@060 | ROLE_USER |

**Full list:** See `src/main/resources/sql/README_SEEDED_CUSTOMER_PASSWORDS.md`

### In-Memory Test Users (Fallback)

If a user is not found in the database, the system falls back to these hardcoded users:

| Username | Password | Role |
|----------|----------|------|
| admin | password | ROLE_ADMIN |
| user | password | ROLE_USER |

## How It Works

1. **Login Request** → User sends credentials to `/auth/login`
2. **AuthenticationManager** → Routes through `DaoAuthenticationProvider`
3. **UserDetailsService** → Tries to load user:
   - First: Queries `customer_auth` table for database user
   - If not found: Checks in-memory test user list
   - If not found: Throws `UsernameNotFoundException` → 401 Unauthorized
4. **Password Validation** → BCrypt comparison (database) or BCrypt encode (in-memory)
5. **JWT Token** → Generated with user's roles from `customer_auth.role`

## API Endpoints & Access

### Public (No Auth)
- `POST /auth/login` - Login and get JWT token
- `POST /api/customers/register` - Register new customer (currently not integrated with customer_auth)

### ADMIN-only endpoints
- `POST /api/products` - Create product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/customers` - Get all customers
- `DELETE /api/customers/{id}` - Delete customer
- And more... (see `API_DOC.md`)

### USER + ADMIN endpoints
- `GET /api/products` - List all products
- `POST /api/orders/customer/{customerId}` - Create order
- `GET /api/orders/{orderId}` - Get order details
- And more...

## Test Locally

### Option 1: Use database customer

```bash
# Login as admin customer
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"customer001","password":"Cust@001"}'

# Response: JWT token string
# Example: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# Use token to access protected endpoint
TOKEN="<paste_jwt_token_here>"
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN"
```

### Option 2: Use in-memory test user (fallback)

```bash
# Login as admin
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Response: JWT token

# Access protected endpoint
TOKEN="<paste_jwt_token_here>"
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer $TOKEN"
```

## Files Changed/Created

### New Files
- `src/main/java/org/example/productjwt/model/CustomerAuth.java` - JPA entity mapping `customer_auth` table
- `src/main/java/org/example/productjwt/repository/CustomerAuthRepository.java` - Repository for `CustomerAuth`
- `src/main/java/org/example/productjwt/services/CustomerUserDetailsService.java` - Loads users from database
- `src/main/resources/sql/seed_full_ecommerce_data.sql` - Seeds all tables including `customer_auth`
- `src/main/resources/sql/README_SEEDED_CUSTOMER_PASSWORDS.md` - Customer password mapping reference

### Modified Files
- `src/main/java/org/example/productjwt/config/SecurityConfig.java` - Integrated `CustomerUserDetailsService` with fallback to in-memory users

## Database Schema

```sql
CREATE TABLE customer_auth (
    auth_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL UNIQUE REFERENCES customer(id) ON DELETE CASCADE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,  -- BCrypt encoded via pgcrypto
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

## Query Examples

### Check seeded customer auth

```sql
-- View all customer auth records
SELECT auth_id, customer_id, username, role, is_active FROM customer_auth LIMIT 5;

-- Verify passwords are BCrypt hashed (starts with $2a$)
SELECT username, password_hash FROM customer_auth WHERE customer_id = 1;

-- Count admins
SELECT COUNT(*) FROM customer_auth WHERE role = 'ROLE_ADMIN';
```

### Decode JWT payload

Use an online JWT decoder (https://jwt.io) or parse locally:

```bash
# Extract payload (middle part) from token and base64 decode
# Example decoded payload:
# {
#   "roles": ["ROLE_ADMIN", "ROLE_USER"],
#   "sub": "customer001",
#   "iat": 1623040000,
#   "exp": 1623043600
# }
```

## Next Steps (Optional)

1. **Create a dedicated auth API** that returns customer info + JWT (already done in `/auth/login`)
2. **Add customer registration** that creates records in both `customer` and `customer_auth` tables
3. **Implement role hierarchy** in `@PreAuthorize` annotations (already done via `hasAnyRole`)
4. **Add audit logging** for authentication attempts in `customer_auth.last_login_at`

