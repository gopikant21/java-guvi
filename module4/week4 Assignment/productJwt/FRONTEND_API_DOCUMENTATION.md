# Frontend API Documentation (HTML + JavaScript)

This guide explains how to connect a plain HTML/JS frontend to the `productJwt` backend.

## 1) Base Setup

- Base URL: `http://localhost:8080`
- API docs (Swagger UI): `http://localhost:8080/swagger-ui.html`
- Auth type: JWT Bearer token
- Public endpoints:
  - `POST /auth/login`
  - `POST /api/customers/register`

## 2) Authentication Flow

### Login

`POST /auth/login`

Request body:

```json
{
  "username": "admin",
  "password": "password"
}
```

Success response body is a plain JWT string (not wrapped in JSON).

Example use in JS:

```js
const BASE_URL = "http://localhost:8080";

async function login(username, password) {
  const res = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password })
  });

  if (!res.ok) {
    const msg = await res.text();
    throw new Error(`Login failed: ${msg}`);
  }

  const token = await res.text();
  localStorage.setItem("jwtToken", token);
  return token;
}
```

### Send JWT in protected requests

```js
function authHeaders() {
  const token = localStorage.getItem("jwtToken");
  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };
}
```

## 3) Generic API Helper

```js
async function apiRequest(path, options = {}) {
  const token = localStorage.getItem("jwtToken");
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  };

  const res = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (res.status === 401) {
    throw new Error("Unauthorized. Please login again.");
  }
  if (res.status === 403) {
    throw new Error("Forbidden. Role permission denied.");
  }
  if (!res.ok) {
    throw new Error(await res.text());
  }

  const contentType = res.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return res.json();
  }
  return res.text();
}
```

## 4) Request/Response DTO Shapes

### AuthRequest

```json
{
  "username": "string",
  "password": "string"
}
```

### CustomerRequestDto

```json
{
  "name": "string",
  "email": "string",
  "address": "string",
  "phone": "string"
}
```

### CustomerResponseDto

```json
{
  "id": 1,
  "name": "string",
  "email": "string",
  "address": "string",
  "phone": "string"
}
```

### ProductRequestDto

```json
{
  "name": "string",
  "price": 999.99,
  "category": "string",
  "brand": "string",
  "stocks": 10
}
```

### ProductResponseDto

```json
{
  "id": 1,
  "name": "string",
  "price": 999.99,
  "category": "string",
  "brand": "string",
  "stocks": 10
}
```

### OrderItemRequestDto

```json
{
  "productId": 1,
  "quantity": 2
}
```

### OrderItemResponseDto

```json
{
  "id": 1,
  "productId": 1,
  "productName": "string",
  "productPrice": 999.99,
  "quantity": 2,
  "lineTotal": 1999.98
}
```

### OrderResponseDto

```json
{
  "orderId": 1,
  "customerId": 1,
  "items": [],
  "total": 0.0
}
```

### OrderTotalResponseDto

```json
{
  "orderId": 1,
  "total": 1999.98
}
```

## 5) Customer Endpoints (`/api/customers`)

- `POST /register` (public)
- `GET /` (ADMIN)
- `GET /{id}` (USER)
- `GET /email/{email}` (USER)
- `GET /phone/{phone}` (USER)
- `PUT /{id}` (USER)
- `DELETE /{id}` (ADMIN)
- `GET /without-orders` (ADMIN)
- `GET /by-domain/{domain}` (ADMIN)
- `GET /top-customers` (ADMIN)
- `GET /with-min-orders/{minOrders}` (ADMIN)
- `PUT /{id}/update-address?address=...` (USER)
- `PUT /{id}/update-contact?phone=...&address=...` (USER)

## 6) Product Endpoints (`/api/products`)

- `POST /` (ADMIN)
- `GET /` (USER)
- `GET /{id}` (USER)
- `PUT /{id}` (ADMIN)
- `DELETE /{id}` (ADMIN)
- `GET /category/{category}` (USER)
- `GET /brand/{brand}` (USER)
- `GET /price?minPrice=...&maxPrice=...` (USER)
- `GET /search?name=...` (USER)
- `GET /available` (USER)
- `GET /available/category/{category}` (USER)
- `PUT /{id}/reduce-stock?quantity=...` (ADMIN)
- `PUT /{id}/increase-stock?quantity=...` (ADMIN)
- `GET /low-stock/{threshold}` (ADMIN)
- `GET /low-stock/category/{category}/{threshold}` (ADMIN)
- `GET /count/category/{category}` (ADMIN)
- `GET /unordered` (ADMIN)
- `PUT /bulk-update/price-by-percentage?category=...&percentage=...` (ADMIN)
- `PUT /bulk-update/clear-stock/{category}` (ADMIN)
- `PUT /{id}/restock?quantity=...` (ADMIN)

## 7) Order Endpoints (`/api/orders`)

- `POST /customer/{customerId}` (USER)
- `GET /` (ADMIN)
- `GET /{orderId}` (USER)
- `GET /customer/{customerId}` (USER)
- `GET /customer/{customerId}/newest` (USER)
- `POST /{orderId}/items` (USER)
- `GET /{orderId}/items` (USER)
- `PUT /items/{orderItemId}?quantity=...` (USER)
- `DELETE /items/{orderItemId}` (USER)
- `GET /{orderId}/total` (USER)
- `DELETE /{orderId}` (USER)
- `GET /customer/{customerId}/total-spent` (ADMIN)
- `GET /high-value/{threshold}` (ADMIN)
- `GET /customer/{customerId}/count` (ADMIN)
- `GET /customer/{customerId}/avg-item-value` (ADMIN)
- `GET /empty` (ADMIN)

## 8) Frontend Examples

### Register Customer (public)

```js
async function registerCustomer(customer) {
  return apiRequest("/api/customers/register", {
    method: "POST",
    body: JSON.stringify(customer)
  });
}
```

### Get Products (USER role)

```js
async function getProducts() {
  return apiRequest("/api/products", { method: "GET" });
}
```

### Create Product (ADMIN role)

```js
async function createProduct(product) {
  return apiRequest("/api/products", {
    method: "POST",
    body: JSON.stringify(product)
  });
}
```

### Create Order + Add Item

```js
async function createOrder(customerId) {
  return apiRequest(`/api/orders/customer/${customerId}`, { method: "POST" });
}

async function addItem(orderId, productId, quantity) {
  return apiRequest(`/api/orders/${orderId}/items`, {
    method: "POST",
    body: JSON.stringify({ productId, quantity })
  });
}
```

## 9) Browser Integration Notes

- If your frontend is served from a different origin (example: `http://127.0.0.1:5500`), CORS must be enabled in backend config.
- If frontend is served by Spring static resources under same host/port, CORS issues usually do not occur.
- JWT currently expires based on backend configuration (`JwtService` uses configured expiration value).

## 10) Suggested First Frontend Flow

1. Login via `/auth/login` and save token in `localStorage`.
2. Call `GET /api/products` to render product list.
3. Register/select customer.
4. Create order and add items.
5. Show order total using `GET /api/orders/{orderId}/total`.

