# Retail Rewards Backend

Production-grade Spring Boot 2.7.x backend for a retail rewards program, designed as a modular monolith with clear domain boundaries.

## Stack

- Java 17
- Spring Boot
- Spring Security with JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Swagger/OpenAPI
- Lombok
- Groovy + Spock

Spring Boot 2.7.x is used because it requires Java 17 and remains compatible with the requested Java version. springdoc-openapi 1.x is used because it supports Spring Boot 2.x. JJWT is used for JWT creation and validation. Spock are used for readable tests and containerized PostgreSQL integration tests.

## Architecture

The codebase is a modular monolith split by business capability:

- `auth` for registration, login, and JWT issuance
- `customer` for customer profile management
- `transaction` for transaction capture and reward point calculation
- `rewards` for reporting and summaries
- `security` for JWT filters and authorization
- `common`, `exception`, `config`, `util` for shared concerns

This structure keeps the application deployable as a single unit while preserving clean boundaries for future extraction if needed.

## Reward Rules

- 2 points for every dollar over 100 in a transaction
- 1 point for every dollar between 50 and 100
- No points for the first 50 dollars

Example for 120:

- 2 × 20 = 40
- 1 × 50 = 50
- Total = 90

## API Summary

Public:

- `POST /auth/register`
- `POST /auth/login`

Authenticated:

- `GET /customers`
- `POST /customers`
- `GET /customers/{id}`
- `GET /transactions`
- `POST /transactions`
- `GET /rewards/{customerId}`
- `GET /rewards/{customerId}/monthly`

## Authentication Flow

1. Client calls `/auth/register` or `/auth/login`
2. Passwords are hashed using BCrypt
3. On success, the API returns a signed JWT
4. Client sends `Authorization: Bearer <token>`
5. JWT filter validates the token and populates the security context
6. Endpoints are protected using Spring Security and method-level authorization

## Database Design

### Tables

- `roles`
- `app_users`
- `app_user_roles`
- `customers`
- `customer_transactions`

### Indexing Strategy

- Unique index on user email
- Unique index on customer email
- Composite index on `(customer_id, transaction_date)` for reward reporting
- Indexes on `customer_id` and `transaction_date` for transaction filters

### Relationships

- `app_users` ↔ `roles` many-to-many
- `customers` ↔ `app_users` one-to-one
- `customer_transactions` → `customers` many-to-one

## Setup

### Prerequisites

- Java 17
- Maven 3.5+
- PostgreSQL 9.5+

### Run locally

1. Create the PostgreSQL database `retail_rewards`
2. Update `src/main/resources/application.yml`
3. Run:

```bash
mvn clean spring-boot:run
```

### Swagger UI

- `/swagger-ui.html`
- `/v3/api-docs`

## Example Requests

### Register

```http
POST /auth/register
Content-Type: application/json

{
  "firstName": "Alice",
  "lastName": "Walker",
  "email": "alice@example.com",
  "password": "Password@123",
  "phone": "9999999999"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "alice@example.com",
  "password": "Password@123"
}
```

### Create Transaction

```http
POST /transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "customerId": 1,
  "amount": 120.00,
  "transactionDate": "2026-05-01T10:15:30",
  "description": "Electronics purchase"
}
```

### Rewards Summary

```http
GET /rewards/1?from=2026-01-01&to=2026-05-31
Authorization: Bearer <token>
```

## Testing

### Unit tests

- Reward calculation
- Auth service
- Transaction service

### Integration tests

- PostgreSQL-backed repository tests
- MockMvc security tests
- Invalid JWT and unauthorized request handling
- Edge cases and boundary conditions

Run:

```bash
mvn test
```

## Future Evolution

This monolith can be evolved without disruption:

- Extract `transaction` and `rewards` into services if traffic grows
- Replace synchronous reporting with event-driven summaries
- Add caching for high-volume reward queries
- Add message queues for transaction ingestion
- Introduce tenant segregation if the retail model expands
