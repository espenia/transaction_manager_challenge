# Transaction Manager

![Version Badge](https://img.shields.io/badge/0.0.1_SNAPSHOT-green?style=flat&label=Version)
![Java](https://img.shields.io/badge/21-blue?style=flat&label=Java)
![Spring Boot](https://img.shields.io/badge/3.3.5-green?style=flat&label=Spring%20Boot)

REST API for managing financial transactions, built with hexagonal architecture.

---

## Table of Contents

- [Architecture](#architecture)
- [Stack](#stack)
- [Dependencies](#dependencies)
- [Entity](#entity)
- [API Endpoints](#api-endpoints)
- [How to Run](#how-to-run)
- [Tests](#tests)
- [Docker](#docker)

---

## Architecture

The project follows **Hexagonal Architecture** (Ports & Adapters), keeping the business logic isolated from infrastructure and delivery concerns.

```
src/main/java/transactionmanager/challange/
├── core/                        # Domain — no framework dependencies
│   ├── model/                   # Domain entities (Transaction, TransactionType, UpsertTransactionResult)
│   ├── gateway/                 # Output port interfaces (TransactionGateway)
│   ├── usecase/                 # Application use cases (UpsertTransactionUseCase, GetTransactionsByTypeUseCase, GetTransactionSumUseCase)
│   └── exception/               # Domain exceptions
├── infra/                       # Adapters — implements core ports
│   ├── gateway/impl/            # TransactionGatewayImpl — maps domain ↔ entity
│   └── inmemory/
│       ├── entity/              # TransactionEntity — storage model
│       └── repository/          # TransactionRepository + InMemoryTransactionRepository
└── entrypoint/                  # Input adapters
    ├── TransactionController.java
    ├── PingController.java
    ├── dto/                     # Request/response DTOs
    └── handler/                 # Global exception handler
```

**Dependency rule:** `entrypoint` → `core` ← `infra`. The core never depends on infra or entrypoint.

---

## Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 (Spring Web / Jetty) |
| Build tool | Gradle 8 |
| Storage | In-memory (`ConcurrentHashMap`) |
| API docs | SpringDoc OpenAPI (Swagger UI) |
| Monitoring | New Relic |
| Containerization | Docker (multi-stage build) |

---

## Dependencies

| Dependency | Version | Purpose |
|---|---|---|
| `spring-boot-starter-web` | 3.3.5 | HTTP server (Jetty) |
| `springdoc-openapi-starter-webmvc-ui` | 2.1.0 | Swagger UI at `/swagger-ui.html` |
| `lombok` | — | Boilerplate reduction (`@Builder`, `@Getter`, etc.) |
| `newrelic-api` | 8.0.1 | APM integration |
| `caffeine` | 3.1.8 | Caching support |
| `commons-io` | 2.17.0 | I/O utilities |
| `jackson-module-blackbird` | — | Fast JSON serialisation |
| `spring-boot-starter-test` | 3.3.5 | JUnit 5 + Mockito + MockMvc |
| `archunit-junit5` | 1.3.0 | Architecture rule enforcement |
| `wiremock-standalone` | 3.10.0 | HTTP stub server for tests |
| `system-stubs-jupiter` | 2.0.1 | Environment variable stubbing in tests |

---

## Entity

### `Transaction`

| Field | Type | Description |
|---|---|---|
| `id` | `Long` | Auto-generated random positive long (set by client via URL) |
| `amount` | `Double` | Transaction amount |
| `type` | `String` | Transaction type (e.g. `DEBIT`, `CREDIT`) |
| `parentId` | `Long` (nullable) | Reference to a parent transaction |

---

## API Endpoints

### `GET /ping`
Health check.

**Response `200`**
```
pong
```

---

### `PUT /transaction/{transactionId}`
Creates or updates a transaction identified by `transactionId`.

**Request body**
```json
{
  "amount": 100.0,
  "type": "DEBIT",
  "parent_id": null
}
```

**Response `201 Created`** — transaction did not exist, was created:
```json
{ "status": "ok" }
```

**Response `200 OK`** — transaction already existed, was updated:
```json
{
  "amount": 100.0,
  "type": "DEBIT",
  "parent_id": null
}
```

---

### `GET /transaction/types/{type}`
Returns a list of IDs of all transactions matching the given `type`.

**Path parameter**

| Param | Type | Description |
|---|---|---|
| `type` | `String` | Transaction type to filter by (e.g. `DEBIT`, `CREDIT`) |

**Response `200 OK`**
```json
[1, 2, 3]
```

---

### `GET /transaction/sum/{transactionId}`
Returns the sum of amounts for the transaction with the given ID and all its descendants (linked transitively via `parent_id`). Returns `404` if the transaction does not exist.

**Path parameter**

| Param | Type | Description |
|---|---|---|
| `transactionId` | `Long` | Root transaction ID |

**Response `200 OK`**
```json
{ "sum": 175.0 }
```

**Response `404 Not Found`** — transaction does not exist.

> Swagger UI available at `http://localhost:8080/swagger-ui.html`

---

## How to Run

### Prerequisites
- Java 21
- Gradle (or use the included `./gradlew` wrapper)

### Local

```bash
./gradlew bootRun
```

The server starts on **port 8080** by default.

### With a specific profile

```bash
SCOPE_SUFFIX=local ./gradlew bootRun
```

---

## Tests

Run the full test suite:

```bash
./gradlew test
```

Run a specific test class:

```bash
./gradlew test --tests "transactionmanager.challange.core.usecase.UpsertTransactionUseCaseTest"
```

### Test structure

| Test class | Type | What it covers |
|---|---|---|
| `UpsertTransactionUseCaseTest` | Unit (Spring + `@MockBean`) | Create vs update logic, field propagation |
| `GetTransactionsByTypeUseCaseTest` | Unit (Spring + `@MockBean`) | Filter by type, empty result |
| `GetTransactionSumUseCaseTest` | Unit (Spring + `@MockBean`) | Subtree sum, nested children, not-found, excludes siblings |
| `TransactionGatewayImplTest` | Unit (Spring + `@MockBean`) | Domain ↔ entity mapping, id projection by type, findAll |
| `InMemoryTransactionRepositoryTest` | Unit (Spring) | Save, find, id generation, filter by type |
| `TransactionControllerTest` | Integration (`TestRestTemplate`) | `PUT /transaction/{id}` — 201/200; `GET /transaction/types/{type}` — id list; `GET /transaction/sum/{id}` — sum/404 |
| `ControllerExceptionHandlerTest` | Integration | Global error handler |
| `ScopeUtilTest` | Unit | Scope/environment resolution |
| `ArchitectureTest` | Architecture | Hexagonal layer dependency rules |

Test reports are generated at `build/reports/tests/test/index.html`.

---

## Docker

The `Dockerfile` uses a **multi-stage build** to keep the final image lean:

1. **Build stage** — copies source, runs `./gradlew build -x test` inside an `openjdk:21-jdk-slim` image.
2. **Runtime stage** — copies only the compiled JAR into a fresh `openjdk:21-jdk-slim` image.

### Run with Docker Compose (recommended)

```bash
docker compose up --build
```

To run in the background:

```bash
docker compose up --build -d
```

To stop:

```bash
docker compose down
```

The API will be available at `http://localhost:8080`.

### Build and run manually with Docker

```bash
docker build -t transaction-manager .
docker run -p 8080:8080 transaction-manager
```

### Run with environment variables (manual)

```bash
docker run -p 8080:8080 -e SCOPE_SUFFIX=prod transaction-manager
```
