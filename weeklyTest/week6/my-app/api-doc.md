# Site Scanner API - Frontend Integration Guide

This document describes the backend endpoints your frontend can call.

## Base URL

- Local: `http://localhost:8080`

## Data Model (`Scan`)

```json
{
  "id": 1,
  "domainName": "example.com",
  "numPages": 120,
  "numBrokenLinks": 8,
  "numMissingImages": 3,
  "deleted": false
}
```

Field notes:
- `id`: number (server-generated)
- `domainName`: string
- `numPages`: number
- `numBrokenLinks`: number
- `numMissingImages`: number
- `deleted`: boolean (internal soft-delete flag)

## Endpoints

### 1) Health Check

- **GET** `/healthcheck`
- **Response**: `200 OK`, empty body

Use this to confirm backend availability.

---

### 2) Get All Scans

- **GET** `/scan`
- **Response**: `200 OK`

Response body (array):

```json
[
  {
    "id": 1,
    "domainName": "example.com",
    "numPages": 120,
    "numBrokenLinks": 8,
    "numMissingImages": 3,
    "deleted": false
  }
]
```

---

### 3) Get Scan By ID

- **GET** `/scan/{id}`
- **Path param**: `id` (number)
- **Response**: `200 OK` with a single `Scan`
- **Error**: `404 Not Found` if ID does not exist

Example:
- `GET /scan/10`

---

### 4) Create Scan

- **POST** `/scan`
- **Request body**: `Scan` object **without `id`**
- **Response**: `200 OK` with created `Scan`
- **Error**: `400 Bad Request` if `id` is provided in request body

Example request body:

```json
{
  "domainName": "example.com",
  "numPages": 120,
  "numBrokenLinks": 8,
  "numMissingImages": 3,
  "deleted": false
}
```

---

### 5) Delete Scan (Soft Delete)

- **DELETE** `/scan/{id}`
- **Path param**: `id` (number)
- **Response**: `200 OK`, empty body
- **Error**: `404 Not Found` if ID does not exist

Note: This is a soft delete. Backend marks `deleted=true` instead of physically removing the record.

---

### 6) Search By Domain Name

- **GET** `/scan/search/{domainName}`
- **Path param**: `domainName` (string)
- **Query param (optional)**: `orderBy` (string)
- **Response**: `200 OK` with array of matching scans
- **Error**: `400 Bad Request` if `orderBy` is invalid

Example calls:
- `/scan/search/example.com`
- `/scan/search/example.com?orderBy=numBrokenLinks`

`orderBy` must match a valid `Scan` field name.

## Error Handling

The API uses HTTP status codes with exception-based handling:

- `400 Bad Request`: invalid input (for example: create request includes `id`, or invalid `orderBy` value)
- `404 Not Found`: requested scan not found

## Frontend Integration Tips

- Keep backend base URL in an environment variable (for example: `VITE_API_BASE_URL` / `REACT_APP_API_BASE_URL`).
- Treat non-2xx responses as errors and parse status code for user messaging.
- For create flow, never send `id` from the client.
- For delete flow, update UI optimistically or re-fetch list after success.

