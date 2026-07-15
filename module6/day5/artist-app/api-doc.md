# Artist Service API Documentation (Frontend)

## Base Configuration

- Base URL: `http://localhost:9090`
- Resource Path: `/v1/artists`
- Full Resource Base: `http://localhost:9090/v1/artists`
- CORS: enabled for all origins (`*`)

## DTOs

### Request DTO: `ArtistRequest`

Used in create artist API.

| Field | Type | Required | Description |
|---|---|---|---|
| `firstName` | string | Yes | Artist first name |
| `lastName` | string | Yes | Artist last name |

Example:

```json
{
  "firstName": "John",
  "lastName": "Lennon"
}
```

### Response DTO: `ArtistResponse` (Serialized `Artist` model)

Used in create/get/list APIs.

| Field | Type | Description |
|---|---|---|
| `id` | number (int64) | Artist identifier (auto-generated) |
| `firstName` | string | Artist first name |
| `lastName` | string | Artist last name |

Example:

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Lennon"
}
```

---

## APIs

### 1) Create Artist

- Method: `POST`
- URL: `/v1/artists`
- Request Body DTO: `ArtistRequest`
- Success Response: `201 Created`
- Success Response DTO: `ArtistResponse`

Request Example:

```json
{
  "firstName": "Freddie",
  "lastName": "Mercury"
}
```

Success Response Example (`201`):

```json
{
  "id": 12,
  "firstName": "Freddie",
  "lastName": "Mercury"
}
```

### 2) Get Artist By ID

- Method: `GET`
- URL: `/v1/artists/{artistId}`
- Path Param:
  - `artistId` (number)
- Request Body DTO: none
- Success Response: `200 OK`
- Success Response DTO: `ArtistResponse`
- Not Found Response: `404 Not Found` (empty body)

Success Response Example (`200`):

```json
{
  "id": 12,
  "firstName": "Freddie",
  "lastName": "Mercury"
}
```

### 3) Get All Artists

- Method: `GET`
- URL: `/v1/artists`
- Request Body DTO: none
- Success Response: `200 OK`
- Success Response DTO: `ArtistResponse[]`

Success Response Example (`200`):

```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Lennon"
  },
  {
    "id": 2,
    "firstName": "Adele",
    "lastName": "Adkins"
  }
]
```

### 4) Delete Artist

- Method: `DELETE`
- URL: `/v1/artists/{artistId}`
- Path Param:
  - `artistId` (number)
- Request Body DTO: none
- Success Response: `204 No Content` (empty body)
- Not Found Response: `404 Not Found` (empty body)

## Frontend Notes

- For `DELETE`, do not try to parse JSON from the response on success (`204` has no body).
- For `GET /v1/artists/{artistId}`, handle `404` to show a not-found UI state.
- No validation annotations are present on request DTO fields, so backend accepts null/empty values unless additional validation is added later.