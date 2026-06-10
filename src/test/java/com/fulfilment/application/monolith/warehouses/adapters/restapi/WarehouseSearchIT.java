package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fulfilment.application.monolith.warehouses.adapters.database.DbWarehouse;
import com.fulfilment.application.monolith.warehouses.adapters.database.WarehouseRepository;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;

/**
 * Integration tests for the /warehouse/search endpoint.
 * It verifies filtering by location and capacity range, as well as pagination and sorting.
 */
@QuarkusIntegrationTest
public class WarehouseSearchIT {

  @Inject WarehouseRepository warehouseRepository;

  @BeforeEach
  @Transactional
  public void setup() {
    // Clean the DB
    warehouseRepository.deleteAll();
    // Insert sample data
    create("WH-001", "AMSTERDAM-001", 30);
    create("WH-002", "AMSTERDAM-001", 50);
    create("WH-003", "ZWOLLE-001", 70);
  }

  private void create(String code, String location, int capacity) {
    DbWarehouse db = new DbWarehouse();
    db.businessUnitCode = code;
    db.location = location;
    db.capacity = capacity;
    db.stock = 0;
    db.createdAt = java.time.LocalDateTime.now();
    warehouseRepository.persist(db);
  }

  @Test
  public void testSearchByLocationAndCapacity() {
    // Search for AMSTERDAM warehouses with capacity >=40
    given()
        .queryParam("location", "AMSTERDAM-001")
        .queryParam("minCapacity", 40)
        .when()
        .get("warehouse/search")
        .then()
        .statusCode(200)
        .body("businessUnitCode", hasItem("WH-002"))
        .body("businessUnitCode", not(hasItem("WH-001")))
        .body("businessUnitCode", not(hasItem("WH-003")));
  }

  @Test
  public void testPaginationAndSorting() {
    // Request pageSize=1, page=0, sorted by capacity desc
    given()
        .queryParam("sortBy", "capacity")
        .queryParam("sortOrder", "desc")
        .queryParam("page", 0)
        .queryParam("pageSize", 1)
        .when()
        .get("warehouse/search")
        .then()
        .statusCode(200)
        .body("size()", org.hamcrest.CoreMatchers.is(1))
        .body("[0].businessUnitCode", org.hamcrest.CoreMatchers.is("WH-003"));
  }
}
