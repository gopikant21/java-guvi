# JWT Authentication Implementation Guide

## Overview
This project has been updated with JWT (JSON Web Token) authentication using Spring Boot 3.x with Spring Security 6 and JJWT 0.12.5.

## Project Structure Changes

### New Files Created:
1. **JwtService** (`services/JwtService.java`)
   - Generates JWT tokens
   - Extracts claims from tokens
   - Validates tokens
   - Uses JJWT 0.12+ with fluent builders

2. **JwtAuthFilter** (`config/JwtAuthFilter.java`)
   - Intercepts HTTP requests
   - Extracts JWT from Authorization header
   - Validates tokens and loads user context
   - Extends OncePerRequestFilter

3. **SecurityConfig** (`config/SecurityConfig.java`)
   - Configures Spring Security 6 with SecurityFilterChain bean
   - Sets up stateless session management
   - Registers JWT filter
   - Configures in-memory user for testing
   - Permits /auth/login and /api/customers/register endpoints

4. **AuthController** (`controller/AuthController.java`)
   - POST /auth/login - Login endpoint
   - Authenticates user and returns JWT token

5. **AuthRequest** (`dto/AuthRequest.java`)
   - Java 21 Record for login requests
   - Contains username and password fields

### Modified Files:
1. **pom.xml** - Already had JJWT 0.12.5 dependencies
2. **application.properties** - Added JWT configuration properties
3. **CustomerController** - Removed @PreAuthorize annotation from register endpoint

## Dependencies
All necessary dependencies are already in pom.xml:
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
```

## Authentication Flow

### Step 1: Login (Get Token)
**Endpoint:** `POST http://localhost:8080/auth/login`

**Request Body:**
```json
{
    "username": "admin",
    "password": "password"
}
```

**Response:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDExMDA2NiwiZXhwIjoxNzI0MTEzNjY2fQ.GVfVr...
```

### Step 2: Access Protected Resources
**Example Endpoint:** `GET http://localhost:8080/api/customers`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyNDExMDA2NiwiZXhwIjoxNzI0MTEzNjY2fQ.GVfVr...
```

## Key Features

1. **Stateless Authentication**
   - No session creation
   - JWT tokens are self-contained
   - Scalable for microservices

2. **Secure Token Generation**
   - HMAC-SHA 256 signing
   - 256-bit secret key
   - 1-hour token expiration

3. **Filter-Based Validation**
   - JWT extraction from Authorization header
   - Token validation on each request
   - User context loading via UserDetailsService

4. **Modern Spring Security 6 Architecture**
   - No deprecated WebSecurityConfigurerAdapter
   - SecurityFilterChain bean configuration
   - Method-level security with @PreAuthorize

## Configuration

### application.properties
```properties
jwt.secret.key=your-super-secret-key-that-is-at-least-32-characters-long!
jwt.expiration.time=3600000
```

### In-Memory Test User
- **Username:** admin
- **Password:** password
- **Roles:** Empty (can be extended)

## Security Rules

- `/auth/login` - **Publicly Accessible** (POST only)
- `/api/customers/register` - **Publicly Accessible** (POST only)
- All other endpoints - **Require Authentication** (Valid JWT token)

## Testing Endpoints

### 1. Register a Customer (No Auth Required)
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```
Save the returned token.

### 3. Access Protected Endpoint
```bash
curl -X GET http://localhost:8080/api/customers \
  -H "Authorization: Bearer <YOUR_TOKEN_HERE>"
```

## Common HTTP Responses

- **200 OK** - Request successful with valid JWT
- **401 Unauthorized** - Missing or invalid JWT token
- **403 Forbidden** - Insufficient permissions
- **404 Not Found** - Endpoint not found
- **400 Bad Request** - Invalid request data

## Token Structure

JWT tokens consist of three parts separated by dots:
1. **Header:** Algorithm and token type
2. **Payload:** Claims (username, issued time, expiration)
3. **Signature:** HMAC-SHA256 signature

## Important Notes

1. **Secret Key:** Change the hardcoded secret in JwtService.java to a secure value in production
2. **Token Expiration:** Currently set to 1 hour (3600000ms)
3. **User Details:** Update UserDetailsService in SecurityConfig for production use with database
4. **Roles & Authorities:** Currently empty; implement role-based access control as needed

## Customization

### Add Custom Claims
Modify `JwtService.generateToken()`:
```java
Map<String, Object> claims = new HashMap<>();
claims.put("role", userRole);
return Jwts.builder()
    .claims(claims)
    // ... rest of builder
```

### Change Token Expiration
Update `EXPIRATION_TIME` in JwtService:
```java
private static final long EXPIRATION_TIME = 1000 * 60 * 24; // 24 hours
```

### Add More Protected Endpoints
Update `SecurityConfig.authorizeHttpRequests()`:
```java
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/user/**").hasRole("USER")
```

## Troubleshooting

### Token Expired Error
- Generate a new token using the login endpoint

### Invalid Token Error
- Ensure token is prefixed with "Bearer " in Authorization header
- Check that token hasn't been modified

### 403 Forbidden
- Verify JWT is in Authorization header with "Bearer " prefix
- Check token hasn't expired

## Next Steps

1. Deploy to production environment
2. Use environment variables for secret key
3. Implement database-based UserDetailsService
4. Add role-based access control
5. Implement token refresh mechanism
6. Add logout functionality (token blacklist)

