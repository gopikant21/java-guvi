# Product JWT API Documentation

## Table of Contents
1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Authorization](#authorization)
4. [Enums](#enums)
5. [Error Handling](#error-handling)
6. [API Endpoints](#api-endpoints)
   - [Authentication Endpoints](#authentication-endpoints)
   - [Product Endpoints](#product-endpoints)
   - [Customer Endpoints](#customer-endpoints)
   - [Order Endpoints](#order-endpoints)
7. [Data Models](#data-models)
8. [Status Codes](#status-codes)

---

## Overview

The Product JWT API is a Spring Boot REST API that provides comprehensive functionality for managing products, customers, and orders with JWT-based authentication and role-based authorization.

**Base URL:** `http://localhost:8080`

**API Version:** 1.0

**Authentication Method:** JWT (JSON Web Tokens) with Bearer scheme

---

## Authentication

### JWT Token Flow

1. **Login** - Send credentials to `/auth/login` endpoint
2. **Receive JWT Token** - Get JWT token in response (includes `sub` and `roles` claims)
3. **Include Token** - Add token to Authorization header for subsequent requests
4. **Token Format** - `Authorization: Bearer <token>`

### Test Credentials

- **Admin User:** `admin` / `password` (`ROLE_ADMIN`, `ROLE_USER`)
- **Regular User:** `user` / `password` (`ROLE_USER`)

### Important Notes

- JWT tokens are stateless and stored client-side only
- Tokens are validated on each request using the JWT Auth Filter
- JWT role claims are stored as `roles` (example: `["ROLE_ADMIN","ROLE_USER"]`)
- The token must be included in the `Authorization` header as `Bearer <token>`
- The session creation policy is **STATELESS** - no server-side sessions are maintained

---

## Enums

The API uses the following enums for type-safe operations:

### ProductCategory

Represents product categories in the system. Used for categorization, filtering, and bulk operations.

```java
public enum ProductCategory {
    ELECTRONICS,              // Electronics & Gadgets
    CLOTHING,                // Apparel & Fashion
    BOOKS,                   // Books & Media
    HOME_AND_KITCHEN,        // Home & Kitchen items
    SPORTS_AND_OUTDOORS,     // Sports & Outdoor gear
    TOYS_AND_GAMES,          // Toys & Games
    BEAUTY_AND_PERSONAL_CARE,// Beauty & Personal Care
    GROCERIES,               // Groceries & Food
    OTHER                    // Other categories
}
```

**Usage in Requests/Responses:**
```json
{
  "category": "ELECTRONICS"
}
```

**Accepted `ProductCategory` values:**
`ELECTRONICS`, `CLOTHING`, `BOOKS`, `HOME_AND_KITCHEN`, `SPORTS_AND_OUTDOORS`, `TOYS_AND_GAMES`, `BEAUTY_AND_PERSONAL_CARE`, `GROCERIES`, `OTHER`

---

### OrderStatus

Represents the lifecycle status of an order. Defines valid transitions between states.

```java
public enum OrderStatus {
    PENDING,      // Order created but not yet processed
    PROCESSING,   // Order is being processed
    SHIPPED,      // Order has been shipped
    DELIVERED,    // Order has been delivered
    CANCELLED     // Order has been cancelled
}
```

**Valid State Transitions:**
- `PENDING` → `PROCESSING` or `CANCELLED`
- `PROCESSING` → `SHIPPED` or `CANCELLED`
- `SHIPPED` → `DELIVERED`
- `DELIVERED` → (no transitions)
- `CANCELLED` → (no transitions)

**Usage in Requests/Responses:**
```json
{
  "status": "PROCESSING"
}
```

**Accepted `OrderStatus` values:**
`PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`

---

### UserRole

Represents user roles in the system for role-based access control.

```java
public enum UserRole {
    ADMIN,  // Full system access - Create, Read, Update, Delete, Bulk operations
    USER    // Limited access - Read operations, Create/Update own resources
}
```

**Authorization Enforcement:**
- `ADMIN`: Access to endpoints marked with `@PreAuthorize("hasRole('ADMIN')")` and shared endpoints marked with `@PreAuthorize("hasAnyRole('USER','ADMIN')")`
- `USER`: Access to shared endpoints marked with `@PreAuthorize("hasAnyRole('USER','ADMIN')")`

---

## Authorization

### Role-Based Access Control

The API uses role-based authorization with the following roles:

| Role | Access Level | Permissions |
|------|--------------|-------------|
| **ADMIN** | Administrator | Full access - Create, Read, Update, Delete operations, View all data, Bulk operations |
| **USER** | Regular User | Limited access - Read operations, Create/Update own resources, Limited data visibility |

### Access Control Rules

- **Public Endpoints** (No Authentication Required):
  - `POST /auth/login` - User login
  - `POST /api/customers/register` - Customer registration
  - `/swagger-ui/**`, `/swagger-ui.html`, `/v3/api-docs/**` - API documentation

- **Authenticated Endpoints** (All other endpoints require valid JWT token)

### Authorization Enforcement

Authorization is enforced using Spring Security's `@PreAuthorize` annotation on controller methods:

```java
@PreAuthorize("hasRole('ADMIN')")    // Only ADMIN users can access
@PreAuthorize("hasAnyRole('USER','ADMIN')") // USER and ADMIN can access
```

---

## Error Handling

### Global Exception Handler

The API implements centralized exception handling via `GlobalExceptionHandler`. All errors follow a consistent response format with appropriate HTTP status codes.

### Standard Error Response Format

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

### Custom Exceptions & Status Codes

#### 400 Bad Request

**1. ValidationException**
- Thrown when request contains invalid or malformed data
- Includes optional `field` information

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error for field 'price': Price must be greater than zero",
  "field": "price"
}
```

**2. IllegalArgumentException**
- Thrown for invalid parameter values

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid value provided for parameter"
}
```

#### 401 Unauthorized

**1. BadCredentialsException**
- Thrown when login credentials are incorrect

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

**2. UsernameNotFoundException**
- Thrown when user doesn't exist in the system

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

**3. UnauthorizedException**
- Thrown when user attempts to access resource without proper authorization

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "User 'customer001' is not authorized to access products endpoint"
}
```

**4. AuthenticationException**
- Generic authentication failures (invalid tokens, expired tokens, etc.)

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized: Missing or invalid authentication token"
}
```

#### 402 Payment Required

**PaymentProcessingException**
- Thrown when payment processing fails
- Includes `orderId` for reference

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 402,
  "error": "Payment Required",
  "message": "Payment processing failed for Order ID 12345: Credit card declined",
  "orderId": "12345"
}
```

#### 403 Forbidden

**ForbiddenException**
- Thrown when user is authenticated but lacks required role/permission

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied. User 'customer001' requires role 'ROLE_ADMIN' to access product creation"
}
```

#### 404 Not Found

**ResourceNotFoundException**
- Thrown when a requested resource doesn't exist

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

#### 409 Conflict

**1. InvalidOrderException**
- Thrown when order operations fail due to invalid state

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot process order: Order is already delivered"
}
```

**2. InsufficientStockException**
- Thrown when product stock is insufficient for the requested quantity
- Includes detailed stock information

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 409,
  "error": "Insufficient Stock",
  "message": "Insufficient stock for product ID 5. Requested: 10, Available: 3",
  "productId": 5,
  "requestedQuantity": 10,
  "availableStock": 3
}
```

**3. DuplicateResourceException**
- Thrown when attempting to create a resource that already exists

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Customer with email 'john@example.com' already exists",
  "resourceType": "Customer",
  "fieldName": "email"
}
```

**4. ConflictException**
- Thrown when an operation conflicts with current system state

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Conflict: Order - Cannot modify order after shipment",
  "resourceType": "Order"
}
```

#### 503 Service Unavailable

**ServiceUnavailableException**
- Thrown when external service is unavailable

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service 'PaymentGateway' is currently unavailable: Connection timeout",
  "serviceName": "PaymentGateway"
}
```

#### 500 Internal Server Error

**Generic Exception**
- All unhandled exceptions result in a generic 500 error

```json
{
  "timestamp": "2026-07-10T15:30:45.123456",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

### HTTP Status Code Summary

| Code | Error | When Thrown |
|------|-------|------------|
| 200 | OK | Successful request |
| 400 | Bad Request | ValidationException, IllegalArgumentException |
| 401 | Unauthorized | BadCredentialsException, UsernameNotFoundException, UnauthorizedException, AuthenticationException |
| 402 | Payment Required | PaymentProcessingException |
| 403 | Forbidden | ForbiddenException |
| 404 | Not Found | ResourceNotFoundException |
| 409 | Conflict | InvalidOrderException, InsufficientStockException, DuplicateResourceException, ConflictException |
| 503 | Service Unavailable | ServiceUnavailableException |
| 500 | Internal Server Error | All unhandled exceptions |

### Exception Hierarchy

```
Throwable
├── Exception
│   ├── RuntimeException
│   │   ├── ResourceNotFoundException
│   │   ├── InvalidOrderException
│   │   ├── ValidationException
│   │   ├── InsufficientStockException
│   │   ├── DuplicateResourceException
│   │   ├── UnauthorizedException
│   │   ├── ForbiddenException
│   │   ├── ConflictException
│   │   ├── PaymentProcessingException
│   │   ├── ServiceUnavailableException
│   │   └── IllegalArgumentException
│   └── Spring Security Exceptions
│       ├── BadCredentialsException
│       ├── UsernameNotFoundException
│       └── AuthenticationException
```

---

## API Endpoints

### Authentication Endpoints

#### 1. User Login

**Endpoint:** `POST /auth/login`

**Authorization:** ✅ Public (No JWT required)

**Description:** Authenticates a user and returns a JWT token.

**Request Body:**
```json
{
  "username": "admin",
  "password": "password"
}
```

**Request DTO:** `AuthRequest`
```java
public record AuthRequest(String username, String password) {}
```

**Response (HTTP 200):**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sInN1YiI6ImFkbWluIiwiaWF0IjoxNjIzMDQwMDAwLCJleHAiOjE2MjMwNDM2MDB9.signature...
```

**Response Type:** String (JWT Token)

**Decoded JWT Payload Example (admin login):**
```json
{
  "roles": ["ROLE_ADMIN", "ROLE_USER"],
  "sub": "admin",
  "iat": 1623040000,
  "exp": 1623043600
}
```

**Errors:**
- `401 Unauthorized` - Invalid username or password
- `500 Internal Server Error` - Server error during token generation

**Example cURL:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'
```

---

### Product Endpoints

#### 1. Create Product

**Endpoint:** `POST /api/products`

**Authorization:** ✅ ADMIN only

**Description:** Creates a new product.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Laptop",
  "price": 999.99,
  "category": "ELECTRONICS",
  "brand": "Dell",
  "stocks": 50
}
```

**Request DTO:** `ProductRequestDto`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private Double price;
    
    @NotNull(message = "Category is required")
    private ProductCategory category;
    
    private String brand;
    
    @Min(value = 0, message = "Stocks cannot be negative")
    private Integer stocks;
}
```

**Response (HTTP 201 - Created):**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "category": "ELECTRONICS",
  "brand": "Dell",
  "stocks": 50
}
```

**Response DTO:** `ProductResponseDto`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private Double price;
    private ProductCategory category;
    private String brand;
    private int stocks;
}
```

**Errors:**
- `400 Bad Request` - Invalid input (validation failed)
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }'
```

---

#### 2. Get All Products

**Endpoint:** `GET /api/products`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all products.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  },
  {
    "id": 2,
    "name": "Mouse",
    "price": 29.99,
    "category": "ELECTRONICS",
    "brand": "Logitech",
    "stocks": 100
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <token>"
```

---

#### 3. Get Product by ID

**Endpoint:** `GET /api/products/{id}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves a specific product by ID.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Product ID

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "category": "ELECTRONICS",
  "brand": "Dell",
  "stocks": 50
}
```

**Response Type:** `ProductResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Product with given ID doesn't exist
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 4. Update Product

**Endpoint:** `PUT /api/products/{id}`

**Authorization:** ✅ ADMIN only

**Description:** Updates an existing product.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Product ID

**Request Body:**
```json
{
  "name": "Gaming Laptop",
  "price": 1299.99,
  "category": "ELECTRONICS",
  "brand": "Dell",
  "stocks": 45
}
```

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "Gaming Laptop",
  "price": 1299.99,
  "category": "ELECTRONICS",
  "brand": "Dell",
  "stocks": 45
}
```

**Response Type:** `ProductResponseDto`

**Errors:**
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "price": 1299.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 45
  }'
```

---

#### 5. Delete Product

**Endpoint:** `DELETE /api/products/{id}`

**Authorization:** ✅ ADMIN only

**Description:** Deletes a product.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Product ID

**Response (HTTP 200 - OK):**
```
Product deleted successfully
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 6. Get Products by Category

**Endpoint:** `GET /api/products/category/{category}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all products in a specific category.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `category` (ProductCategory) - Product category enum value (for example: `ELECTRONICS`)

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/category/ELECTRONICS \
  -H "Authorization: Bearer <token>"
```

---

#### 7. Get Products by Brand

**Endpoint:** `GET /api/products/brand/{brand}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all products of a specific brand.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `brand` (String) - Product brand name

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/brand/Dell \
  -H "Authorization: Bearer <token>"
```

---

#### 8. Get Products by Price Range

**Endpoint:** `GET /api/products/price`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves products within a specific price range.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `minPrice` (Double, required) - Minimum price
- `maxPrice` (Double, required) - Maximum price

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET "http://localhost:8080/api/products/price?minPrice=100&maxPrice=1000" \
  -H "Authorization: Bearer <token>"
```

---

#### 9. Search Products by Name

**Endpoint:** `GET /api/products/search`

**Authorization:** ✅ USER or ADMIN

**Description:** Searches products by name (partial matching).

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `name` (String, required) - Search keyword

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET "http://localhost:8080/api/products/search?name=Laptop" \
  -H "Authorization: Bearer <token>"
```

---

#### 10. Get Available Products

**Endpoint:** `GET /api/products/available`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all products with stock > 0.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/available \
  -H "Authorization: Bearer <token>"
```

---

#### 11. Get Available Products by Category

**Endpoint:** `GET /api/products/available/category/{category}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves available products in a specific category.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `category` (ProductCategory) - Product category enum value (for example: `ELECTRONICS`)

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "price": 999.99,
    "category": "ELECTRONICS",
    "brand": "Dell",
    "stocks": 50
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/available/category/ELECTRONICS \
  -H "Authorization: Bearer <token>"
```

---

#### 12. Reduce Stock

**Endpoint:** `PUT /api/products/{id}/reduce-stock`

**Authorization:** ✅ ADMIN only

**Description:** Reduces the stock of a product by the specified quantity.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Product ID

**Query Parameters:**
- `quantity` (Integer, required) - Quantity to reduce

**Response (HTTP 200 - OK):**
```
Stock reduced by 5
```

**Response Type:** String

**Errors:**
- `400 Bad Request` - Invalid quantity or insufficient stock
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/products/1/reduce-stock?quantity=5" \
  -H "Authorization: Bearer <token>"
```

---

#### 13. Increase Stock

**Endpoint:** `PUT /api/products/{id}/increase-stock`

**Authorization:** ✅ ADMIN only

**Description:** Increases the stock of a product by the specified quantity.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Product ID

**Query Parameters:**
- `quantity` (Integer, required) - Quantity to increase

**Response (HTTP 200 - OK):**
```
Stock increased by 10
```

**Response Type:** String

**Errors:**
- `400 Bad Request` - Invalid quantity
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/products/1/increase-stock?quantity=10" \
  -H "Authorization: Bearer <token>"
```

---

#### 14. Get Low Stock Products

**Endpoint:** `GET /api/products/low-stock/{threshold}`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves products with stock below the specified threshold.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `threshold` (Integer) - Stock threshold value

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 2,
    "name": "Mouse",
    "price": 29.99,
    "category": "ELECTRONICS",
    "brand": "Logitech",
    "stocks": 3
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/low-stock/10 \
  -H "Authorization: Bearer <token>"
```

---

#### 15. Get Low Stock Products by Category

**Endpoint:** `GET /api/products/low-stock/category/{category}/{threshold}`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves products in a category with stock below threshold.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `category` (ProductCategory) - Product category (enum value like ELECTRONICS)
- `threshold` (Integer) - Stock threshold value

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 2,
    "name": "Mouse",
    "price": 29.99,
    "category": "ELECTRONICS",
    "brand": "Logitech",
    "stocks": 3
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/low-stock/category/ELECTRONICS/10 \
  -H "Authorization: Bearer <token>"
```

---

#### 16. Get Product Count by Category

**Endpoint:** `GET /api/products/count/category/{category}`

**Authorization:** ✅ ADMIN only

**Description:** Gets the total count of products in a specific category.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `category` (ProductCategory) - Product category enum value (for example: `ELECTRONICS`)

**Response (HTTP 200 - OK):**
```
5
```

**Response Type:** Long

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/count/category/ELECTRONICS \
  -H "Authorization: Bearer <token>"
```

---

#### 17. Get Unordered Products

**Endpoint:** `GET /api/products/unordered`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves products that have never been ordered.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 3,
    "name": "Keyboard",
    "price": 79.99,
    "category": "ELECTRONICS",
    "brand": "Mechanical",
    "stocks": 20
  }
]
```

**Response Type:** `List<ProductResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/products/unordered \
  -H "Authorization: Bearer <token>"
```

---

#### 18. Update Price by Percentage for Category

**Endpoint:** `PUT /api/products/bulk-update/price-by-percentage`

**Authorization:** ✅ ADMIN only

**Description:** Updates prices for all products in a category by a percentage.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Query Parameters:**
- `category` (ProductCategory, required) - Product category enum value (for example: `ELECTRONICS`)
- `percentage` (Double, required) - Percentage to increase/decrease

**Response (HTTP 200 - OK):**
```
Product prices updated by 10.0% for category: ELECTRONICS. Records affected: 5
```

**Response Type:** String

**Errors:**
- `400 Bad Request` - Invalid percentage value
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/products/bulk-update/price-by-percentage?category=ELECTRONICS&percentage=10" \
  -H "Authorization: Bearer <token>"
```

---

#### 19. Clear Stock by Category

**Endpoint:** `PUT /api/products/bulk-update/clear-stock/{category}`

**Authorization:** ✅ ADMIN only

**Description:** Sets stock to 0 for all products in a category.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `category` (ProductCategory) - Product category enum value (for example: `ELECTRONICS`)

**Response (HTTP 200 - OK):**
```
Stock cleared for category: ELECTRONICS. Records affected: 5
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT http://localhost:8080/api/products/bulk-update/clear-stock/ELECTRONICS \
  -H "Authorization: Bearer <token>"
```

---

#### 20. Restock Product

**Endpoint:** `PUT /api/products/{id}/restock`

**Authorization:** ✅ ADMIN only

**Description:** Increases stock for a product (restock operation).

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Product ID

**Query Parameters:**
- `quantity` (Integer, required) - Quantity to add

**Response (HTTP 200 - OK):**
```
Product restocked with 50 units. Records affected: 1
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/products/1/restock?quantity=50" \
  -H "Authorization: Bearer <token>"
```

---

### Customer Endpoints

#### 1. Register Customer

**Endpoint:** `POST /api/customers/register`

**Authorization:** ✅ Public (No JWT required)

**Description:** Registers a new customer.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "phone": "555-1234"
}
```

**Request DTO:** `CustomerRequestDto`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDto {
    @NotBlank(message = "Customer name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String address;
    private String phone;
}
```

**Response (HTTP 201 - Created):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "phone": "555-1234"
}
```

**Response DTO:** `CustomerResponseDto`

**Errors:**
- `400 Bad Request` - Invalid input (validation failed)
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St, City",
    "phone": "555-1234"
  }'
```

---

#### 2. Get All Customers

**Endpoint:** `GET /api/customers`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves all customers.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St, City",
    "phone": "555-1234"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "address": "456 Oak Ave, Town",
    "phone": "555-5678"
  }
]
```

**Response Type:** `List<CustomerResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers \
  -H "Authorization: Bearer <token>"
```

---

#### 3. Get Customer by ID

**Endpoint:** `GET /api/customers/{id}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves a specific customer by ID.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "phone": "555-1234"
}
```

**Response Type:** `CustomerResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 4. Get Customer by Email

**Endpoint:** `GET /api/customers/email/{email}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves a customer by email address.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `email` (String) - Customer email address

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "phone": "555-1234"
}
```

**Response Type:** `CustomerResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/email/john@example.com \
  -H "Authorization: Bearer <token>"
```

---

#### 5. Get Customer by Phone

**Endpoint:** `GET /api/customers/phone/{phone}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves a customer by phone number.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `phone` (String) - Customer phone number

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "address": "123 Main St, City",
  "phone": "555-1234"
}
```

**Response Type:** `CustomerResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/phone/555-1234 \
  -H "Authorization: Bearer <token>"
```

---

#### 6. Update Customer

**Endpoint:** `PUT /api/customers/{id}`

**Authorization:** ✅ USER or ADMIN

**Description:** Updates an existing customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Path Parameters:**
- `id` (Long) - Customer ID

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "address": "789 Elm St, Metro",
  "phone": "555-9999"
}
```

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com",
  "address": "789 Elm St, Metro",
  "phone": "555-9999"
}
```

**Response Type:** `CustomerResponseDto`

**Errors:**
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Updated",
    "email": "john.updated@example.com",
    "address": "789 Elm St, Metro",
    "phone": "555-9999"
  }'
```

---

#### 7. Delete Customer

**Endpoint:** `DELETE /api/customers/{id}`

**Authorization:** ✅ ADMIN only

**Description:** Deletes a customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```
Customer deleted successfully
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X DELETE http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 8. Get Customers Without Orders

**Endpoint:** `GET /api/customers/without-orders`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves customers who have no orders.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 3,
    "name": "New Customer",
    "email": "new@example.com",
    "address": "999 New St",
    "phone": "555-0000"
  }
]
```

**Response Type:** `List<CustomerResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/without-orders \
  -H "Authorization: Bearer <token>"
```

---

#### 9. Find Customers by Email Domain

**Endpoint:** `GET /api/customers/by-domain/{domain}`

**Authorization:** ✅ ADMIN only

**Description:** Finds customers by email domain.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `domain` (String) - Email domain (e.g., "example.com")

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St, City",
    "phone": "555-1234"
  }
]
```

**Response Type:** `List<CustomerResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/by-domain/example.com \
  -H "Authorization: Bearer <token>"
```

---

#### 10. Get Top Customers by Order Count

**Endpoint:** `GET /api/customers/top-customers`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves customers with the most orders.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St, City",
    "phone": "555-1234"
  }
]
```

**Response Type:** `List<CustomerResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/top-customers \
  -H "Authorization: Bearer <token>"
```

---

#### 11. Get Customers with Minimum Orders

**Endpoint:** `GET /api/customers/with-min-orders/{minOrders}`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves customers with at least the specified number of orders.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `minOrders` (Long) - Minimum number of orders

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "address": "123 Main St, City",
    "phone": "555-1234"
  }
]
```

**Response Type:** `List<CustomerResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/customers/with-min-orders/2 \
  -H "Authorization: Bearer <token>"
```

---

#### 12. Update Customer Address

**Endpoint:** `PUT /api/customers/{id}/update-address`

**Authorization:** ✅ USER or ADMIN

**Description:** Updates a customer's address.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Customer ID

**Query Parameters:**
- `address` (String, required) - New address

**Response (HTTP 200 - OK):**
```
Customer address updated. Records affected: 1
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/customers/1/update-address?address=999%20New%20Street" \
  -H "Authorization: Bearer <token>"
```

---

#### 13. Update Customer Contact Information

**Endpoint:** `PUT /api/customers/{id}/update-contact`

**Authorization:** ✅ USER or ADMIN

**Description:** Updates a customer's contact information (phone and/or address).

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `id` (Long) - Customer ID

**Query Parameters:**
- `phone` (String, optional) - New phone number
- `address` (String, optional) - New address

**Response (HTTP 200 - OK):**
```
Customer contact info updated. Records affected: 1
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/customers/1/update-contact?phone=555-9999&address=999%20New%20Street" \
  -H "Authorization: Bearer <token>"
```

---

### Order Endpoints

#### 1. Create Order for Customer

**Endpoint:** `POST /api/orders/customer/{customerId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Creates a new empty order for a customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 201 - Created):**
```json
{
  "orderId": 1,
  "customerId": 1,
  "status": "PENDING",
  "orderDate": "2026-07-10T15:30:45.123456",
  "items": [],
  "total": 0.0
}
```

**Response DTO:** `OrderResponseDto`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private Double total;
}
```

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/orders/customer/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 2. Get All Orders

**Endpoint:** `GET /api/orders`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves all orders.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "orderId": 1,
    "customerId": 1,
    "status": "PROCESSING",
    "orderDate": "2026-07-09T10:00:00.000000",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Laptop",
        "productPrice": 999.99,
        "quantity": 1,
        "lineTotal": 999.99
      }
    ],
    "total": 999.99
  }
]
```

**Response Type:** `List<OrderResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>"
```

---

#### 3. Get Order by ID

**Endpoint:** `GET /api/orders/{orderId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves a specific order by ID.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (HTTP 200 - OK):**
```json
{
  "orderId": 1,
  "customerId": 1,
  "status": "PROCESSING",
  "orderDate": "2026-07-09T10:00:00.000000",
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "Laptop",
      "productPrice": 999.99,
      "quantity": 1,
      "lineTotal": 999.99
    }
  ],
  "total": 999.99
}
```

**Response Type:** `OrderResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 4. Get Orders for a Customer

**Endpoint:** `GET /api/orders/customer/{customerId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all orders for a specific customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```json
[
  {
    "orderId": 1,
    "customerId": 1,
    "status": "PROCESSING",
    "orderDate": "2026-07-09T10:00:00.000000",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Laptop",
        "productPrice": 999.99,
        "quantity": 1,
        "lineTotal": 999.99
      }
    ],
    "total": 999.99
  }
]
```

**Response Type:** `List<OrderResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/customer/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 5. Get Customer Orders Sorted by Newest

**Endpoint:** `GET /api/orders/customer/{customerId}/newest`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves orders for a customer sorted by newest first.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```json
[
  {
    "orderId": 2,
    "customerId": 1,
    "status": "PENDING",
    "orderDate": "2026-07-10T14:00:00.000000",
    "items": [],
    "total": 0.0
  },
  {
    "orderId": 1,
    "customerId": 1,
    "status": "PROCESSING",
    "orderDate": "2026-07-09T10:00:00.000000",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Laptop",
        "productPrice": 999.99,
        "quantity": 1,
        "lineTotal": 999.99
      }
    ],
    "total": 999.99
  }
]
```

**Response Type:** `List<OrderResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/customer/1/newest \
  -H "Authorization: Bearer <token>"
```

---

#### 6. Add Item to Order

**Endpoint:** `POST /api/orders/{orderId}/items`

**Authorization:** ✅ USER or ADMIN

**Description:** Adds an item to an existing order.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
Content-Type: application/json
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Request Body:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

**Request DTO:** `OrderItemRequestDto`
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    @NotNull(message = "Product id is required")
    private Long productId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
```

**Response (HTTP 201 - Created):**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Laptop",
  "productPrice": 999.99,
  "quantity": 2,
  "lineTotal": 1999.98
}
```

**Response DTO:** `OrderItemResponseDto`

**Errors:**
- `400 Bad Request` - Invalid input or insufficient stock
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order or product not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X POST http://localhost:8080/api/orders/1/items \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

---

#### 7. Get Items in an Order

**Endpoint:** `GET /api/orders/{orderId}/items`

**Authorization:** ✅ USER or ADMIN

**Description:** Retrieves all items in a specific order.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (HTTP 200 - OK):**
```json
[
  {
    "id": 1,
    "productId": 1,
    "productName": "Laptop",
    "productPrice": 999.99,
    "quantity": 2,
    "lineTotal": 1999.98
  },
  {
    "id": 2,
    "productId": 2,
    "productName": "Mouse",
    "productPrice": 29.99,
    "quantity": 1,
    "lineTotal": 29.99
  }
]
```

**Response Type:** `List<OrderItemResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/1/items \
  -H "Authorization: Bearer <token>"
```

---

#### 8. Update Order Item Quantity

**Endpoint:** `PUT /api/orders/items/{orderItemId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Updates the quantity of an item in an order.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderItemId` (Long) - Order Item ID

**Query Parameters:**
- `quantity` (Integer, required) - New quantity

**Response (HTTP 200 - OK):**
```json
{
  "id": 1,
  "productId": 1,
  "productName": "Laptop",
  "productPrice": 999.99,
  "quantity": 3,
  "lineTotal": 2999.97
}
```

**Response Type:** `OrderItemResponseDto`

**Errors:**
- `400 Bad Request` - Invalid quantity
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order item not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X PUT "http://localhost:8080/api/orders/items/1?quantity=3" \
  -H "Authorization: Bearer <token>"
```

---

#### 9. Remove Item from Order

**Endpoint:** `DELETE /api/orders/items/{orderItemId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Removes an item from an order.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderItemId` (Long) - Order Item ID

**Response (HTTP 200 - OK):**
```
Item removed from order
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order item not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X DELETE http://localhost:8080/api/orders/items/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 10. Get Order Total

**Endpoint:** `GET /api/orders/{orderId}/total`

**Authorization:** ✅ USER or ADMIN

**Description:** Gets the total amount for an order.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (HTTP 200 - OK):**
```json
{
  "orderId": 1,
  "total": 2029.97
}
```

**Response DTO:** `OrderTotalResponseDto`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/1/total \
  -H "Authorization: Bearer <token>"
```

---

#### 11. Cancel Order

**Endpoint:** `DELETE /api/orders/{orderId}`

**Authorization:** ✅ USER or ADMIN

**Description:** Cancels an order and restores product stock.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `orderId` (Long) - Order ID

**Response (HTTP 200 - OK):**
```
Order cancelled and stock restored
```

**Response Type:** String

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `404 Not Found` - Order not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X DELETE http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <token>"
```

---

#### 12. Get Total Spent by Customer

**Endpoint:** `GET /api/orders/customer/{customerId}/total-spent`

**Authorization:** ✅ ADMIN only

**Description:** Gets the total amount spent by a customer across all orders.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```
2999.95
```

**Response Type:** Double

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/customer/1/total-spent \
  -H "Authorization: Bearer <token>"
```

---

#### 13. Get High-Value Orders

**Endpoint:** `GET /api/orders/high-value/{threshold}`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves orders with total amount above the specified threshold.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `threshold` (Double) - Minimum order total

**Response (HTTP 200 - OK):**
```json
[
  {
    "orderId": 1,
    "customerId": 1,
    "status": "PROCESSING",
    "orderDate": "2026-07-09T10:00:00.000000",
    "items": [
      {
        "id": 1,
        "productId": 1,
        "productName": "Laptop",
        "productPrice": 999.99,
        "quantity": 2,
        "lineTotal": 1999.98
      }
    ],
    "total": 1999.98
  }
]
```

**Response Type:** `List<OrderResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/high-value/1000 \
  -H "Authorization: Bearer <token>"
```

---

#### 14. Count Orders by Customer

**Endpoint:** `GET /api/orders/customer/{customerId}/count`

**Authorization:** ✅ ADMIN only

**Description:** Gets the total number of orders for a customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```
5
```

**Response Type:** Long

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/customer/1/count \
  -H "Authorization: Bearer <token>"
```

---

#### 15. Get Average Order Item Value by Customer

**Endpoint:** `GET /api/orders/customer/{customerId}/avg-item-value`

**Authorization:** ✅ ADMIN only

**Description:** Gets the average value of items ordered by a customer.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Path Parameters:**
- `customerId` (Long) - Customer ID

**Response (HTTP 200 - OK):**
```
599.99
```

**Response Type:** Double

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `404 Not Found` - Customer not found
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/customer/1/avg-item-value \
  -H "Authorization: Bearer <token>"
```

---

#### 16. Get Empty Orders

**Endpoint:** `GET /api/orders/empty`

**Authorization:** ✅ ADMIN only

**Description:** Retrieves all orders that have no items.

**Request Headers:**
```
Authorization: Bearer <jwt_token>
```

**Response (HTTP 200 - OK):**
```json
[
  {
    "orderId": 5,
    "customerId": 2,
    "status": "PENDING",
    "orderDate": "2026-07-10T12:00:00.000000",
    "items": [],
    "total": 0.0
  }
]
```

**Response Type:** `List<OrderResponseDto>`

**Errors:**
- `401 Unauthorized` - Missing or invalid JWT token
- `403 Forbidden` - User role is not ADMIN
- `500 Internal Server Error` - Server error

**Example cURL:**
```bash
curl -X GET http://localhost:8080/api/orders/empty \
  -H "Authorization: Bearer <token>"
```

---

## Data Models

### Enums

#### ProductCategory
```java
public enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    BOOKS,
    HOME_AND_KITCHEN,
    SPORTS_AND_OUTDOORS,
    TOYS_AND_GAMES,
    BEAUTY_AND_PERSONAL_CARE,
    GROCERIES,
    OTHER
}
```

#### OrderStatus
```java
public enum OrderStatus {
    PENDING,      // Order created but not yet processed
    PROCESSING,   // Order is being processed
    SHIPPED,      // Order has been shipped
    DELIVERED,    // Order has been delivered
    CANCELLED     // Order has been cancelled
}
```

#### UserRole
```java
public enum UserRole {
    ADMIN,  // Full system access
    USER    // Limited access
}
```

---

### Records

#### AuthRequest
```java
public record AuthRequest(String username, String password) {}
```

#### ApiResponse<T>
```java
public record ApiResponse<T>(
    LocalDateTime timestamp,
    int status,
    String message,
    T data
) {}
```

#### OrderTotalResponseDto
```java
public record OrderTotalResponseDto(Long orderId, Double total) {}
```

---

### Data Transfer Objects (DTOs)

### ProductRequestDto
```java
public class ProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private Double price;
    
    @NotNull(message = "Category is required")
    private ProductCategory category;
    private String brand;
    
    @Min(value = 0, message = "Stocks cannot be negative")
    private Integer stocks;
}
```

### ProductResponseDto
```java
public class ProductResponseDto {
    private Long id;
    private String name;
    private Double price;
    private ProductCategory category;
    private String brand;
    private int stocks;
}
```

### CustomerRequestDto
```java
public class CustomerRequestDto {
    @NotBlank(message = "Customer name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    private String address;
    private String phone;
}
```

### CustomerResponseDto
```java
public class CustomerResponseDto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
}
```

### OrderItemRequestDto
```java
public class OrderItemRequestDto {
    @NotNull(message = "Product id is required")
    private Long productId;
    
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
```

### OrderItemResponseDto
```java
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Double productPrice;
    private int quantity;
    private Double lineTotal;
}
```

### OrderResponseDto
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDto> items = new ArrayList<>();
    private Double total;
}
```

### OrderTotalResponseDto
```java
public record OrderTotalResponseDto(Long orderId, Double total) {}
```

---

## Status Codes

| Code | Status | Description |
|------|--------|-------------|
| **200** | OK | Request successful, returning data |
| **201** | Created | Resource created successfully |
| **400** | Bad Request | Invalid request parameters or validation failed |
| **401** | Unauthorized | Missing or invalid JWT token |
| **403** | Forbidden | User role doesn't have permission for this operation |
| **404** | Not Found | Requested resource not found |
| **500** | Internal Server Error | Server-side error occurred |

---

## Common Workflows

### Workflow 1: User Login and View Products

1. **POST /auth/login** - Authenticate with credentials
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"admin","password":"password"}'
   ```
   Response: JWT token

2. **GET /api/products** - Get all products
   ```bash
   curl -X GET http://localhost:8080/api/products \
     -H "Authorization: Bearer <token>"
   ```

### Workflow 2: Register Customer and Place Order

1. **POST /api/customers/register** - Register new customer (no auth required)
   ```bash
   curl -X POST http://localhost:8080/api/customers/register \
     -H "Content-Type: application/json" \
     -d '{
       "name": "John Doe",
       "email": "john@example.com",
       "address": "123 Main St",
       "phone": "555-1234"
     }'
   ```
   Response: Customer ID (e.g., 1)

2. **POST /auth/login** - Login to get JWT token

3. **POST /api/orders/customer/1** - Create order
   ```bash
   curl -X POST http://localhost:8080/api/orders/customer/1 \
     -H "Authorization: Bearer <token>"
   ```
   Response: Order ID (e.g., 1)

4. **POST /api/orders/1/items** - Add item to order
   ```bash
   curl -X POST http://localhost:8080/api/orders/1/items \
     -H "Authorization: Bearer <token>" \
     -H "Content-Type: application/json" \
     -d '{
       "productId": 1,
       "quantity": 2
     }'
   ```

5. **GET /api/orders/1/total** - Get order total
   ```bash
   curl -X GET http://localhost:8080/api/orders/1/total \
     -H "Authorization: Bearer <token>"
   ```

### Workflow 3: Admin Bulk Operations

1. **POST /auth/login** - Login as admin

2. **PUT /api/products/bulk-update/price-by-percentage** - Increase prices
   ```bash
   curl -X PUT "http://localhost:8080/api/products/bulk-update/price-by-percentage?category=ELECTRONICS&percentage=10" \
     -H "Authorization: Bearer <token>"
   ```

3. **GET /api/products/low-stock/10** - Check low stock items
   ```bash
   curl -X GET http://localhost:8080/api/products/low-stock/10 \
     -H "Authorization: Bearer <token>"
   ```

4. **PUT /api/products/1/restock** - Restock items
   ```bash
   curl -X PUT "http://localhost:8080/api/products/1/restock?quantity=50" \
     -H "Authorization: Bearer <token>"
   ```

---

## Quick Reference - Status Codes & Error Scenarios

### Common Error Scenarios & Responses

#### 1. Bad Login Credentials (401 Unauthorized)

**Request:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrongpassword"}'
```

**Response (401):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password"
}
```

#### 2. Product Out of Stock (409 Conflict)

**Request:**
```bash
curl -X POST http://localhost:8080/api/orders/1/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"productId":5,"quantity":100}'
```

**Response (409):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 409,
  "error": "Insufficient Stock",
  "message": "Insufficient stock for product ID 5. Requested: 100, Available: 10",
  "productId": 5,
  "requestedQuantity": 100,
  "availableStock": 10
}
```

#### 3. Invalid Product Data (400 Bad Request)

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Laptop","price":-100,"category":"ELECTRONICS"}'
```

**Response (400):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation error for field 'price': Price must be greater than zero",
  "field": "price"
}
```

#### 4. Access Denied - Missing Admin Role (403 Forbidden)

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Laptop","price":999.99,"category":"ELECTRONICS","stocks":50}'
```

**Response (403):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied. User 'customer001' requires role 'ROLE_ADMIN' to access product creation"
}
```

#### 5. Product Not Found (404 Not Found)

**Request:**
```bash
curl -X GET http://localhost:8080/api/products/999 \
  -H "Authorization: Bearer <token>"
```

**Response (404):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found with id: 999"
}
```

#### 6. Duplicate Email (409 Conflict)

**Request:**
```bash
curl -X POST http://localhost:8080/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@example.com","phone":"123456","address":"123 St"}'
```

**Response (409):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Customer with email 'john@example.com' already exists",
  "resourceType": "Customer",
  "fieldName": "email"
}
```

#### 7. Missing or Invalid JWT Token (401 Unauthorized)

**Request:**
```bash
curl -X GET http://localhost:8080/api/products
# No Authorization header provided
```

**Response (401):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 401,
  "error": "Unauthorized",
  "message": "Unauthorized: Missing or invalid authentication token"
}
```

#### 8. Invalid Order State (409 Conflict)

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <token>"
```

**Response (409):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Cannot modify order after delivery"
}
```

#### 9. Payment Processing Failed (402 Payment Required)

**Request:**
```bash
curl -X POST http://localhost:8080/api/orders/1/payment \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"method":"card","cardNumber":"1234567890123456"}'
```

**Response (402):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 402,
  "error": "Payment Required",
  "message": "Payment processing failed for Order ID 1: Credit card declined",
  "orderId": "1"
}
```

#### 10. External Service Unavailable (503 Service Unavailable)

**Response (503):**
```json
{
  "timestamp": "2026-07-10T14:30:45.123456",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Service 'PaymentGateway' is currently unavailable: Connection timeout",
  "serviceName": "PaymentGateway"
}
```

---

## Complete Exception Reference

### Exception by HTTP Status Code

**400 Bad Request**
- `ValidationException` - Request data validation failed (includes field info)
- `IllegalArgumentException` - Invalid parameter values

**401 Unauthorized**
- `BadCredentialsException` - Wrong login credentials
- `UsernameNotFoundException` - User account doesn't exist
- `UnauthorizedException` - User lacks authorization to access resource
- `AuthenticationException` - General authentication failure (invalid/expired token)

**402 Payment Required**
- `PaymentProcessingException` - Payment processing failed (includes orderId and reason)

**403 Forbidden**
- `ForbiddenException` - User authenticated but lacks required role/permission

**404 Not Found**
- `ResourceNotFoundException` - Requested resource doesn't exist

**409 Conflict**
- `InvalidOrderException` - Order operation violates business logic
- `InsufficientStockException` - Product stock insufficient (includes inventory details)
- `DuplicateResourceException` - Resource with unique field already exists
- `ConflictException` - Operation conflicts with current system state

**503 Service Unavailable**
- `ServiceUnavailableException` - External service is unavailable/unreachable

**500 Internal Server Error**
- Generic exception handler for all unhandled exceptions

---

## Exception Response Fields Reference

| Exception | Additional Fields |
|-----------|-------------------|
| ValidationException | `field` - The field that failed validation |
| InsufficientStockException | `productId`, `requestedQuantity`, `availableStock` |
| DuplicateResourceException | `resourceType`, `fieldName` |
| PaymentProcessingException | `orderId` |
| ForbiddenException | None (info in message) |
| ServiceUnavailableException | `serviceName` |
| ConflictException | `resourceType` |
| All Others | `timestamp`, `status`, `error`, `message` only |

---

## Testing Exception Responses

### Test Bad Credentials
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nonexistent","password":"password"}' \
  -w "\nStatus: %{http_code}\n"
# Expected: 401
```

### Test Missing Token
```bash
curl -X GET http://localhost:8080/api/products \
  -w "\nStatus: %{http_code}\n"
# Expected: 401
```

### Test Invalid Data
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Test","price":-50}' \
  -w "\nStatus: %{http_code}\n"
# Expected: 400
```

### Test Not Found
```bash
curl -X GET http://localhost:8080/api/products/999 \
  -H "Authorization: Bearer <token>" \
  -w "\nStatus: %{http_code}\n"
# Expected: 404
```

### Test Insufficient Stock
```bash
curl -X POST http://localhost:8080/api/orders/1/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"productId":1,"quantity":10000}' \
  -w "\nStatus: %{http_code}\n"
# Expected: 409
```

### Test Forbidden (No Admin Role)
```bash
# Login as regular user
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"customer001","password":"Cust@001"}'

# Try to create product (requires ROLE_ADMIN)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"name":"Test","price":100}' \
  -w "\nStatus: %{http_code}\n"
# Expected: 403
```

---

## HTTP Status Code Summary Table

| Code | Error | Triggers | Example |
|------|-------|----------|---------|
| 200 | OK | Success | Login successful, resource created |
| 400 | Bad Request | Invalid input | Price = -50, Empty name |
| 401 | Unauthorized | Auth failure | Wrong password, Missing JWT |
| 402 | Payment Required | Payment failed | Card declined |
| 403 | Forbidden | No permission | USER trying to POST product |
| 404 | Not Found | Resource missing | GET /products/999 |
| 409 | Conflict | Business logic | Low stock, Duplicate email |
| 503 | Service Unavailable | External service down | Payment gateway offline |
| 500 | Internal Server Error | Unhandled exception | Database crash |

---

## Error Handling Best Practices

### For API Consumers

1. **Check HTTP Status Code First**
   - 4xx = Client error (check request)
   - 5xx = Server error (retry or contact support)

2. **Read the Error Message**
   - Clear, human-readable messages
   - No internal stack traces exposed

3. **Use Additional Fields if Present**
   - `field` for validation errors
   - `productId`, `availableStock` for inventory errors
   - `serviceName` for service issues

4. **Handle Specific Status Codes**
   - 401: Redirect to login
   - 403: Show "Access Denied" message
   - 404: Show "Resource Not Found"
   - 409: Show "Conflict" with details
   - 5xx: Retry or show error message

### For Developers

1. **Throw Specific Exceptions**
   - Use appropriate exception for each scenario
   - Include context (IDs, values, etc.)

2. **Test All Exception Paths**
   - Unit tests for service layer
   - Integration tests for API layer

3. **Log Appropriately**
   - 4xx: Log at WARN level
   - 5xx: Log at ERROR level with stack trace

4. **Document in API Spec**
   - List possible exceptions for each endpoint
   - Document any custom fields

---

## Notes

- All timestamps are in ISO 8601 format
- All monetary values are in decimal format with up to 2 decimal places
- Empty arrays are returned as `[]` when no items are found
- All date-time responses include timezone information
- Password security: Passwords are hashed using BCrypt algorithm
- Token expiration: Check your JWT configuration for token validity period
- CORS: Configure if accessing API from different domain
- Exception Handling: Centralized via `GlobalExceptionHandler` with consistent JSON response format
- All errors include timestamp for debugging and audit purposes

---

**Last Updated:** July 10, 2026
**Version:** 1.0
**Exception Handling:** Comprehensive with 14+ custom exceptions and proper HTTP status codes

 
