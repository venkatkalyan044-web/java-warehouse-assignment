# Java Warehouse Assignment

## Overview

This repository contains the **Java Warehouse assignment** developed for the IKEA client assessment. It demonstrates:

- A Quarkus microservice managing **Warehouse**, **Product**, and **Store** domains.
- An OpenAPI‑first implementation for the Warehouse API, including a **search and filter** endpoint.
- Robust concurrency handling with proper transaction management.
- Comprehensive unit, integration, and **search‑endpoint** tests using Testcontainers.
- CI pipeline (GitHub Actions) that builds, tests, and generates a **JaCoCo** coverage report (≥ 80 %).
- Health‑check endpoint for database readiness.

## How to Review

1. **Build & Test**
   ```bash
   ./mvnw.cmd clean verify
   ```
   The build will run all tests and produce a coverage report at `target/site/jacoco/index.html`.

2. **Run the Service**
   ```bash
   ./mvnw.cmd quarkus:dev
   ```
   The API will be available at `http://localhost:8080/`. Swagger UI can be accessed at `/q/swagger-ui`.

3. **Explore the Search Endpoint**
   ```bash
   curl "http://localhost:8080/warehouse/search?location=AMSTERDAM-001&minCapacity=30&sortBy=capacity&sortOrder=desc"
   ```

4. **CI Pipeline**
   The [GitHub Actions workflow](.github/workflows/ci.yml) runs on every push, ensuring the project remains stable.

## Project Structure

```
src/main/java/…/warehouses/          # Application code
src/main/resources/openapi/          # OpenAPI spec (includes /warehouse/search)
src/test/java/…/warehouses/         # Unit & integration tests
pom.xml                              # Maven configuration (Quarkus, JaCoCo, CI plugins)
README.md                            # This file
```

Feel free to explore the code, run the tests, and evaluate the design decisions described in `QUESTIONS.md`.
