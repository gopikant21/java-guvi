# Flight Booking System

Spring Boot REST API for managing aircraft, passengers, flights, bookings, payments, and tickets.

## Features

- Layered architecture (`controller`, `service`, `repository`, `dto`, `exceptions`)
- Validation on request DTOs
- Centralized error handling with consistent error response format
- Pagination support on list endpoints
- Business rules:
  - unique passenger email/passport
  - unique flight number
  - one payment per booking
  - ticket issue only for confirmed bookings
  - seat uniqueness per flight
- Enum persistence using `@Enumerated(EnumType.STRING)`

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

## Test

```powershell
.\mvnw.cmd test
```

## Base API Path

`/api/v1`

## Main Endpoints

- `POST /aircrafts`, `GET /aircrafts`, `GET /aircrafts/{id}`, `PUT /aircrafts/{id}`, `DELETE /aircrafts/{id}`
- `POST /passengers`, `GET /passengers`, `GET /passengers/{id}`, `PUT /passengers/{id}`, `DELETE /passengers/{id}`
- `POST /flights`, `GET /flights`, `GET /flights/{id}`, `PUT /flights/{id}`, `DELETE /flights/{id}`
- `POST /bookings`, `GET /bookings`, `GET /bookings/{id}`, `PATCH /bookings/{id}/status`, `DELETE /bookings/{id}`
- `POST /payments`, `GET /payments`, `GET /payments/{id}`, `GET /payments/booking/{bookingId}`, `PUT /payments/{id}`, `DELETE /payments/{id}`
- `POST /tickets`, `GET /tickets`, `GET /tickets/{id}`, `PUT /tickets/{id}`, `DELETE /tickets/{id}`

## Notes

- By default, H2 is available for local runs.
- Configure PostgreSQL via standard Spring datasource properties for production-like deployments.

