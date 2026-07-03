# Team4 Demo Banking Application

Spring Boot REST API for a simple banking domain, matching the Demo Banking spec.

## Stack
Spring Web, Spring Validation, Lombok, Java 21. In-memory storage, JSON only, no UI.

## Endpoints
- `POST /account` — create an account (IBAN is server-generated)
- `GET /account/{id}` — get account by id
- `GET /account/{id}/transactions` — list an account's transactions
- `POST /transfer` — transfer between accounts

## Run
```
mvn spring-boot:run
```
Starts on http://localhost:3400 or https://team4.acnbootcamp.lv/

## Structure
- `controller` — HTTP layer
- `service` — business logic (AccountService, TransferService)
- `repository` — in-memory storage (AccountRepository, TransactionRepository)
- `mapper` — model → DTO mapping
- `dto/request`, `dto/response` — request/response objects
- `model` — domain models (Account, Transaction, User, Role, TransactionType)
- `exception` — custom exceptions + global handler
