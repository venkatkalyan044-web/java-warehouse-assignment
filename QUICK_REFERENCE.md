# Quick Reference Guide

## 🚀 Quick Start

### Prerequisites
```bash
# Verify Java 17+
java -version
# Expected: openjdk version "17.0.11"

# Set JAVA_HOME for Windows
$env:JAVA_HOME = "C:\Users\Hp\Downloads\hackathon-java-assignment\jdk17\jdk-17.0.11+9"
```

### Development

```bash
# Start development server (with hot reload)
./mvnw quarkus:dev

# Access Swagger UI
open http://localhost:8080/q/swagger-ui

# Access Dev UI
open http://localhost:8080/q/dev
```

### Testing

```bash
# Run all tests
./mvnw clean test

# Run specific test class
./mvnw test -Dtest=ArchiveWarehouseUseCaseTest

# Run specific test method
./mvnw test -Dtest=ArchiveWarehouseUseCaseTest#testArchiveWarehouseSuccessfully

# Run integration tests
./mvnw verify

# Skip integration tests (faster)
./mvnw clean test -DskipITs

# Generate code coverage report
./mvnw clean test jacoco:report
# Open: target/site/jacoco/index.html
```

---

## 📊 Test Classes Overview

### Unit Tests

| Class | Count | Focus |
|---|---|---|
| ArchiveWarehouseUseCaseTest | 5 | Archive validations, concurrency |
| ReplaceWarehouseUseCaseTest | 4+ | Replace validations, concurrency |
| WarehouseValidationTest | 8+ | Parameterized edge cases |
| WarehouseOptimisticLockingTest | 2 | Version-based locking |
| LocationGatewayTest | 7 | Location resolution |

### Integration Tests

| Class | Count | Focus |
|---|---|---|
| WarehouseEndpointIT | 9 | REST endpoints |
| WarehouseSearchIT | 2 | Search/filter API |
| WarehouseConcurrencyIT | 3 | Concurrent operations |
| WarehouseTestcontainersIT | 5 | Database operations |
| StoreEventObserverTest | 2 | Event handling |
| StoreTransactionIntegrationTest | 1 | Transaction integrity |
| ProductEndpointTest | 1 | CRUD operations |

---

## 🔍 API Quick Reference

### Warehouse Endpoints

**List all warehouses**
```bash
GET /warehouse
```

**Create warehouse**
```bash
POST /warehouse
Content-Type: application/json

{
  "businessUnitCode": "WH-001",
  "location": "AMSTERDAM-001",
  "capacity": 50,
  "stock": 10
}
```

**Get warehouse by code**
```bash
GET /warehouse/WH-001
```

**Archive warehouse**
```bash
DELETE /warehouse/WH-001
```

**Replace warehouse**
```bash
POST /warehouse/WH-001/replacement
Content-Type: application/json

{
  "location": "ZWOLLE-001",
  "capacity": 40,
  "stock": 5
}
```

**Search & filter warehouses**
```bash
GET /warehouse/search?location=AMSTERDAM-001&minCapacity=40&sortBy=capacity&sortOrder=desc&page=0&pageSize=10
```

### Store Endpoints

**List all stores**
```bash
GET /store
```

**Create store**
```bash
POST /store
Content-Type: application/json

{
  "name": "Store Name",
  "quantityProductsInStock": 100
}
```

**Get store by ID**
```bash
GET /store/1
```

**Update store**
```bash
PUT /store/1
Content-Type: application/json

{
  "name": "New Name",
  "quantityProductsInStock": 200
}
```

### Product Endpoints

**List all products**
```bash
GET /product
```

**Create product**
```bash
POST /product
Content-Type: application/json

{
  "name": "Product Name",
  "stock": 100
}
```

---

## 📍 Valid Locations

```
ZWOLLE-001 (max capacity: 40, max warehouses: 1)
ZWOLLE-002 (max capacity: 50, max warehouses: 2)
AMSTERDAM-001 (max capacity: 100, max warehouses: 5) ← Largest
AMSTERDAM-002 (max capacity: 75, max warehouses: 3)
TILBURG-001 (max capacity: 40, max warehouses: 1)
HELMOND-001 (max capacity: 45, max warehouses: 1)
EINDHOVEN-001 (max capacity: 70, max warehouses: 2)
VETSBY-001 (max capacity: 90, max warehouses: 1)
```

---

## 🐛 Debugging

### Enable detailed logging
```properties
# In application.properties
quarkus.log.level=DEBUG
quarkus.hibernate-orm.log.sql=true
```

### Run with debug mode
```bash
./mvnw test -Dmaven.surefire.debug
# Attach debugger to port 5005
```

### Check database (during dev mode)
- Dev UI: http://localhost:8080/q/dev
- Includes H2 console for test database

---

## 📦 Building for Production

### Compile
```bash
./mvnw clean compile
```

### Package
```bash
./mvnw clean package
```

### Run JAR
```bash
java -jar ./target/quarkus-app/quarkus-run.jar
```

### Native build (GraalVM required)
```bash
./mvnw package -Pnative
./target/java-code-assignment-1.0.0-SNAPSHOT-runner
```

---

## ✅ Pre-commit Checklist

- [ ] All tests pass: `./mvnw clean verify`
- [ ] Coverage > 80%: Check jacoco report
- [ ] No compilation errors: `./mvnw clean compile`
- [ ] Code follows patterns: Review architecture docs
- [ ] Logging added: Check use cases
- [ ] Exception handling: Verify error messages

---

## 📚 Documentation Files

- **IMPLEMENTATION_REPORT.md** - Full architecture and implementation details
- **DELIVERABLES_SUMMARY.md** - Complete deliverables checklist
- **QUESTIONS.md** - Discussion questions with answers
- **BRIEFING.md** - Domain context and entity overview
- **CODE_ASSIGNMENT.md** - Original task descriptions
- **GETTING_STARTED.md** - Setup and orientation guide

---

## 🔗 Common Issues & Solutions

**Issue**: Port 8080 already in use
```bash
# Change in application.properties
quarkus.http.port=8081
```

**Issue**: Tests failing with database errors
```bash
# Clean and rebuild
./mvnw clean install
```

**Issue**: OpenAPI code not generated
```bash
# Force regeneration
./mvnw clean compile
```

**Issue**: Java version mismatch
```bash
# Set JAVA_HOME
$env:JAVA_HOME = "C:\path\to\jdk17"
```

---

## 📞 Key Contacts & Info

- **Framework**: Quarkus 3.13.3
- **Database**: PostgreSQL (production) / H2 (test)
- **ORM**: Hibernate Panache
- **Testing**: JUnit 5, Testcontainers
- **Coverage Tool**: JaCoCo

---

## 🎯 Next Steps

1. Push to GitHub: `git push origin main`
2. Set up CI/CD pipeline
3. Configure health checks in Kubernetes
4. Monitor code coverage with JaCoCo
5. Plan database migrations for production

---

**Last Updated**: June 9, 2026
**Status**: Ready for Use

