# Secure E-Commerce Fulfillment API - TDD Suite

This project now contains a TDD-first test suite for the fulfillment API challenge.

## What is covered

- Service layer behavior (`MerchantServiceTest`, `OrderServiceTest`, `ShipmentServiceTest`)
- Controller contracts with MockMvc (`MerchantControllerTest`, `OrderControllerTest`, `ShipmentControllerTest`)
- Repository constraints with H2 (`MerchantRepositoryTest`, `OrderRepositoryTest`)
- Security behavior (`SecurityTest`)
- Global exception mappings (`GlobalExceptionHandlerTest`)
- Integration-level persistence and fulfillment flow specs (`MerchantIntegrationTest`, `FulfillmentIntegrationTest`)

## Run tests

```powershell
./mvnw.cmd test
```

## Notes

- The test suite is written as executable specification for TDD.
- Several service tests include local specification implementations to document expected behavior clearly before production implementation is completed.

