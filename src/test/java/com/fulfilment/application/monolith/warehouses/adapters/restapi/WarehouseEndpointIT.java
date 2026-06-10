package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for Warehouse REST endpoints.
 * Tests the REST API layer including list, get, create, archive, and replace operations.
 */
@QuarkusIntegrationTest
public class WarehouseEndpointIT {

  @Test
  public void testListAllWarehouses() {
    final String path = "warehouse";

    // List all warehouses - should include the 3 pre-loaded warehouses
    given()
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("MWH.001"), containsString("MWH.012"), containsString("MWH.023"))
        .body(containsString("ZWOLLE-001"), containsString("AMSTERDAM-001"), containsString("TILBURG-001"));
  }

  @Test
  public void testGetWarehouseByBusinessUnitCode() {
    final String path = "warehouse/MWH.001";

    // Get specific warehouse
    given()
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("MWH.001"))
        .body(containsString("ZWOLLE-001"));
  }

  @Test
  public void testGetNonExistentWarehouseReturns404() {
    final String path = "warehouse/NON-EXISTENT";

    // Try to get non-existent warehouse
    given()
        .when()
        .get(path)
        .then()
        .statusCode(404);
  }

  @Test
  public void testCreateWarehouseSuccessfully() {
    final String path = "warehouse";

    String newWarehouseJson = """
        {
          "businessUnitCode": "TEST-CREATE-001",
          "location": "AMSTERDAM-001",
          "capacity": 75,
          "stock": 25
        }
        """;

    // Create warehouse
    given()
        .contentType("application/json")
        .body(newWarehouseJson)
        .when()
        .post(path)
        .then()
        .statusCode(201)
        .body(containsString("TEST-CREATE-001"))
        .body(containsString("AMSTERDAM-001"));
  }

  @Test
  public void testCreateWarehouseWithInvalidLocationFails() {
    final String path = "warehouse";

    String invalidWarehouseJson = """
        {
          "businessUnitCode": "TEST-INVALID-001",
          "location": "INVALID-LOCATION",
          "capacity": 50,
          "stock": 10
        }
        """;

    // Try to create with invalid location
    given()
        .contentType("application/json")
        .body(invalidWarehouseJson)
        .when()
        .post(path)
        .then()
        .statusCode(400);
  }

  @Test
  public void testArchiveWarehouseSuccessfully() {
    final String path = "warehouse/MWH.001";

    // Archive warehouse
    given()
        .when()
        .delete(path)
        .then()
        .statusCode(204);

    // Verify it's archived by trying to get it (should fail or show archived state)
    // Note: Implementation determines if archived warehouses are still retrievable
  }

  @Test
  public void testArchiveNonExistentWarehouseReturns404() {
    final String path = "warehouse/NON-EXISTENT";

    // Try to archive non-existent warehouse
    given()
        .when()
        .delete(path)
        .then()
        .statusCode(404);
  }

  @Test
  public void testSearchWarehousesByLocation() {
    final String path = "warehouse/search";

    // Search for warehouses in AMSTERDAM-001
    given()
        .queryParam("location", "AMSTERDAM-001")
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("MWH.012"))
        .body(not(containsString("MWH.001"))); // MWH.001 is in ZWOLLE-001
  }

  @Test
  public void testSearchWarehousesByCapacityRange() {
    final String path = "warehouse/search";

    // Search for warehouses with capacity between 40 and 100
    given()
        .queryParam("minCapacity", 40)
        .queryParam("maxCapacity", 100)
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body(containsString("MWH.001")) // 100 capacity
        .body(containsString("MWH.012")); // 50 capacity
  }

  @Test
  public void testSearchWarehousesWithPaginationAndSorting() {
    final String path = "warehouse/search";

    // Search with pagination and sorting
    given()
        .queryParam("sortBy", "capacity")
        .queryParam("sortOrder", "desc")
        .queryParam("pageSize", 2)
        .when()
        .get(path)
        .then()
        .statusCode(200)
        .body("size()", equalTo(2)); // Should return 2 warehouses (page size limit)
  }
}
