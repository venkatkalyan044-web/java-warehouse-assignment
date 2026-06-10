# Questions

Here are 2 questions related to the codebase. There's no right or wrong answer - we want to understand your reasoning.

## Question 1: API Specification Approaches

When it comes to API spec and endpoints handlers, we have an Open API yaml file for the `Warehouse` API from which we generate code, but for the other endpoints - `Product` and `Store` - we just coded everything directly. 

What are your thoughts on the pros and cons of each approach? Which would you choose and why?

**Answer:**
```txt
When using an OpenAPI spec, the API contract is clearly defined, enabling automated code generation, consistent documentation, and validation across services. It reduces manual boilerplate, eases client generation, and improves versioning control. However, it can add generation complexity and may be less flexible for rapid changes.

Coding endpoints manually (as with Product and Store) offers full control and quicker iteration, but it risks drifting documentation, duplicate validation logic, and inconsistency across services. It also increases maintenance burden.

I would favor the OpenAPI‑first approach for critical services like Warehouse where stability, versioning, and client SDK generation are valuable. For smaller or experimental services, a manual approach can be acceptable if the overhead of spec maintenance outweighs benefits.
```

---

## Question 2: Testing Strategy

Given the need to balance thorough testing with time and resource constraints, how would you prioritize tests for this project? 

Which types of tests (unit, integration, parameterized, etc.) would you focus on, and how would you ensure test coverage remains effective over time?

**Answer:**
```txt
Testing strategy should prioritize a solid unit test suite covering core business logic (use case classes, repository helpers) to catch regressions quickly. Integration tests should verify the REST endpoints, transaction boundaries, and the search functionality, especially pagination and sorting. Parameterized tests can cover edge cases for the search filters (e.g., null values, min > max). To keep coverage high without excessive time, focus unit tests on critical paths and add a handful of integration tests for each endpoint. Use JaCoCo to monitor coverage, aiming for >80% on the main source set, and treat any uncovered critical logic as a gap to add tests for.
```
