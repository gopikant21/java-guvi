# Employee Operations - Spring Boot

Simple Spring Boot REST API for employee CRUD operations with layered architecture.

## Folder Structure

- `controller` - REST endpoints
- `service` - business contracts
- `service/impl` - business implementation
- `repository` - JPA repository
- `model` - entity classes
- `dto` - request/response payloads
- `exception` - custom exceptions and global error handler
- `validation` - validation group interfaces
- `config` - security configuration

## APIs

Base path: `/api/employees`

- `POST /api/employees` - create employee
- `GET /api/employees` - list all employees
- `GET /api/employees/{id}` - get employee by id
- `PUT /api/employees/{id}` - update employee
- `DELETE /api/employees/{id}` - delete employee

### Sample Request

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "salary": 75000.00
}
```

## Run

```powershell
.\mvnw.cmd spring-boot:run
```

## Test

```powershell
.\mvnw.cmd test
```

