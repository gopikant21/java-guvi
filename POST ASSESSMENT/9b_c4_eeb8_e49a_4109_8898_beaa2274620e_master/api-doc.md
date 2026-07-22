# Track Service API Documentation

## Base URL
`http://localhost:8080/music/platform/v1/tracks`

## Data Models

### TrackRequest (request body)
```json
{
  "title": "Lost in Echoes",
  "albumName": "Echoes of the Unknown",
  "releaseDate": "2021-07-15",
  "playCount": 5000
}
```

### Track (response body)
```json
{
  "id": 1,
  "title": "Lost in Echoes",
  "albumName": "Echoes of the Unknown",
  "releaseDate": "2021-07-15",
  "playCount": 5000
}
```

---

## 1) Create Track
- **Method:** `POST`
- **Path:** `/music/platform/v1/tracks`
- **Request Body:** `TrackRequest`
- **Success Status:** `201 Created`
- **Success Response:** `Track`

### Example Request
```http
POST /music/platform/v1/tracks HTTP/1.1
Content-Type: application/json

{
  "title": "Lost in Echoes",
  "albumName": "Echoes of the Unknown",
  "releaseDate": "2021-07-15",
  "playCount": 5000
}
```

### Example Response (`201`)
```json
{
  "id": 1,
  "title": "Lost in Echoes",
  "albumName": "Echoes of the Unknown",
  "releaseDate": "2021-07-15",
  "playCount": 5000
}
```

---

## 2) Get All Tracks
- **Method:** `GET`
- **Path:** `/music/platform/v1/tracks`
- **Request Body:** None
- **Success Status:** `200 OK`
- **Success Response:** `Track[]`

### Example Request
```http
GET /music/platform/v1/tracks HTTP/1.1
```

### Example Response (`200`)
```json
[
  {
    "id": 1,
    "title": "Lost in Echoes",
    "albumName": "Echoes of the Unknown",
    "releaseDate": "2021-07-15",
    "playCount": 5000
  },
  {
    "id": 2,
    "title": "Skyline Waves",
    "albumName": "Night Drive",
    "releaseDate": "2020-06-10",
    "playCount": 3200
  }
]
```

---

## 3) Get Track By Title
- **Method:** `GET`
- **Path:** `/music/platform/v1/tracks/search`
- **Query Param:** `title` (required, string)
- **Success Status:** `200 OK`
- **Success Response:** `Track`

### Example Request
```http
GET /music/platform/v1/tracks/search?title=Lost%20in%20Echoes HTTP/1.1
```

### Example Response (`200`)
```json
{
  "id": 1,
  "title": "Lost in Echoes",
  "albumName": "Echoes of the Unknown",
  "releaseDate": "2021-07-15",
  "playCount": 5000
}
```

> Note: Current implementation returns HTTP `200` with empty/null body if no record matches the title.

---

## 4) Delete Track
- **Method:** `DELETE`
- **Path:** `/music/platform/v1/tracks/{trackId}`
- **Path Param:** `trackId` (required, long)
- **Success Status:** `204 No Content`
- **Success Response:** Empty body

### Example Request
```http
DELETE /music/platform/v1/tracks/1 HTTP/1.1
```

### Example Response (`204`)
No content.

> Note: Current implementation returns HTTP `204` even if the `trackId` does not exist.

