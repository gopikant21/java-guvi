# REST API Documentation (Frontend Integration)

Base URL: `http://localhost:8080`

## Quick Facts

- Authentication: Not configured
- Database: Not configured (data stored in-memory)
- Content Type: `application/json`

> If you use only HTML + CSS, you can submit forms, but dynamic API response rendering needs JavaScript (`fetch`).

---

## Book APIs

Base path: `/api/books`

### Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/books` | Create book |
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| PUT | `/api/books/{id}` | Update book by ID |
| DELETE | `/api/books/{id}` | Delete book by ID |
| GET | `/api/books/title/{title}` | Search by title |
| GET | `/api/books/author/{author}` | Search by author |
| GET | `/api/books/publisher/{publisher}` | Search by publisher |
| GET | `/api/books/price/above/{price}` | Books above price |
| GET | `/api/books/price/below/{price}` | Books below price |
| GET | `/api/books/price-range?min=100&max=1000` | Price range filter |
| GET | `/api/books/sort/title` | Sort by title |
| GET | `/api/books/sort/author` | Sort by author |
| GET | `/api/books/sort/price` | Sort by price asc |
| GET | `/api/books/sort/price-desc` | Sort by price desc |
| GET | `/api/books/stats/count` | Total books |
| GET | `/api/books/stats/average-price` | Average price |
| GET | `/api/books/stats/max-price` | Max price |
| GET | `/api/books/stats/min-price` | Min price |
| GET | `/api/books/stats/author-count` | Count grouped by author |
| GET | `/api/books/stats/publisher-count` | Count grouped by publisher |

### Request Body (POST/PUT)

```json
{
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "publisher": "Addison-Wesley",
  "price": 850.0
}
```

---

## Product APIs

Base path: `/api/products`

### Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/products` | Create product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| PUT | `/api/products/{id}` | Update product by ID |
| DELETE | `/api/products/{id}` | Delete product by ID |
| GET | `/api/products/search?name=Phone` | Search by exact name |
| GET | `/api/products/search/keyword?keyword=pro` | Keyword search in name/description |
| GET | `/api/products/category/{category}` | Filter by category |
| GET | `/api/products/brand/{brand}` | Filter by brand |
| GET | `/api/products/price/above/{price}` | Products above price |
| GET | `/api/products/price/below/{price}` | Products below price |
| GET | `/api/products/price/range?min=1000&max=10000` | Price range filter |
| GET | `/api/products/stock/in` | In-stock products |
| GET | `/api/products/stock/out` | Out-of-stock products |
| PUT | `/api/products/{id}/buy?quantity=1` | Buy quantity |
| PUT | `/api/products/{id}/restock?quantity=10` | Restock quantity |
| GET | `/api/products/sort/price/asc` | Sort by price asc |
| GET | `/api/products/sort/price/desc` | Sort by price desc |
| GET | `/api/products/sort/rating` | Sort by rating desc |
| GET | `/api/products/sort/name` | Sort by name |
| GET | `/api/products/top-rated?limit=5` | Top rated products |
| GET | `/api/products/discounted?minRating=4.0` | Discounted products by rating |
| GET | `/api/products/stats/average-price` | Average price |
| GET | `/api/products/stats/max-price` | Max price |
| GET | `/api/products/stats/min-price` | Min price |
| GET | `/api/products/stats/count` | Total products |
| GET | `/api/products/stats/inventory-value` | Inventory value |
| GET | `/api/products/stats/category-count` | Count grouped by category |
| GET | `/api/products/stats/brand-count` | Count grouped by brand |

### Request Body (POST/PUT)

```json
{
  "name": "Laptop",
  "description": "15-inch business laptop",
  "price": 65000.0,
  "discountPercentage": 10.0,
  "taxPercentage": 18.0,
  "category": "Electronics",
  "brand": "Lenovo",
  "stockQuantity": 25,
  "rating": 4.5,
  "reviewCount": 120,
  "active": true,
  "sku": "LAP-001"
}
```

---

## HTML Form Integration (Without JavaScript)

Browser forms support `GET` and `POST` directly.

### Example: Product keyword search

```html
<form action="http://localhost:8080/api/products/search/keyword" method="get">
  <label>Keyword:</label>
  <input name="keyword" type="text" required />
  <button type="submit">Search</button>
</form>
```

### Example: Create Book

```html
<form action="http://localhost:8080/api/books" method="post">
  <input name="title" placeholder="Title" />
  <input name="author" placeholder="Author" />
  <input name="publisher" placeholder="Publisher" />
  <input name="price" type="number" step="0.01" placeholder="Price" />
  <button type="submit">Save</button>
</form>
```

> Note: If backend methods use `@RequestBody`, plain HTML form encoding may not work for create/update APIs. Use JavaScript `fetch` for JSON requests.

---

## Common Response Codes

- `200 OK` - Request successful
- `201 Created` - Resource created (if configured)
- `400 Bad Request` - Invalid query/body/path input
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Unhandled backend exception

