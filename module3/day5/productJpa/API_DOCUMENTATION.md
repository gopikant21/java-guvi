# Product JPA API Documentation

This project exposes a REST API under these base paths:

- `/api/customers`
- `/api/products`
- `/api/orders`

A simple HTML + JavaScript client is available at:

- `src/main/resources/static/index.html`

When the Spring Boot app is running, open:

- `http://localhost:8080/index.html`

## 1) Customer APIs

### Create customer

- **POST** `/api/customers/register`

Request body:

```json
{
  "name": "Alice",
  "email": "alice@example.com",
  "address": "Main Street",
  "phone": "9876543210"
}
```

Response (example):

```json
{
  "id": 1,
  "name": "Alice",
  "email": "alice@example.com",
  "address": "Main Street",
  "phone": "9876543210"
}
```

### Get customers

- **GET** `/api/customers`
- **GET** `/api/customers/{id}`
- **GET** `/api/customers/email/{email}`
- **GET** `/api/customers/phone/{phone}`

### Update / delete

- **PUT** `/api/customers/{id}`
- **DELETE** `/api/customers/{id}`

## 2) Product APIs

### Create product

- **POST** `/api/products`

Request body:

```json
{
  "name": "Laptop",
  "price": 70000,
  "category": "Electronics",
  "brand": "Lenovo",
  "stocks": 10
}
```

Response (example):

```json
{
  "id": 1,
  "name": "Laptop",
  "price": 70000.0,
  "category": "Electronics",
  "brand": "Lenovo",
  "stocks": 10
}
```

### Get products

- **GET** `/api/products`
- **GET** `/api/products/{id}`
- **GET** `/api/products/category/{category}`
- **GET** `/api/products/brand/{brand}`
- **GET** `/api/products/search?name=lap`
- **GET** `/api/products/price?minPrice=100&maxPrice=2000`

### Stock operations

- **PUT** `/api/products/{id}/reduce-stock?quantity=1`
- **PUT** `/api/products/{id}/increase-stock?quantity=1`
- **PUT** `/api/products/{id}/restock?quantity=10`

## 3) Order APIs

### Create order

- **POST** `/api/orders/customer/{customerId}`

Response (example):

```json
{
  "orderId": 1,
  "customerId": 1,
  "items": [],
  "total": 0.0
}
```

### Add item to order

- **POST** `/api/orders/{orderId}/items`

Request body:

```json
{
  "productId": 1,
  "quantity": 2
}
```

Response (example):

```json
{
  "id": 1,
  "productId": 1,
  "productName": "Laptop",
  "productPrice": 70000.0,
  "quantity": 2,
  "lineTotal": 140000.0
}
```

### Get orders and totals

- **GET** `/api/orders`
- **GET** `/api/orders/{orderId}`
- **GET** `/api/orders/{orderId}/items`
- **GET** `/api/orders/{orderId}/total`
- **DELETE** `/api/orders/{orderId}`

## 4) JavaScript fetch examples

### Create customer

```javascript
await fetch('/api/customers/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    name: 'Alice',
    email: 'alice@example.com',
    address: 'Main Street',
    phone: '9876543210'
  })
});
```

### List products

```javascript
const response = await fetch('/api/products');
const products = await response.json();
console.log(products);
```

### Create order and add item

```javascript
const orderRes = await fetch('/api/orders/customer/1', { method: 'POST' });
const order = await orderRes.json();

await fetch(`/api/orders/${order.orderId}/items`, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ productId: 1, quantity: 2 })
});
```

## 5) Frontend integration notes

- If your HTML/JS is served by this Spring Boot app (`/static`), use relative URLs like `/api/products`.
- If your HTML/JS runs from another origin (for example `http://127.0.0.1:5500`), configure CORS in Spring Boot.
- Ensure PostgreSQL is running with the values in `src/main/resources/application.properties`.

