package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LocationGateway
 *
 * Tests the location resolution logic including:
 * - Finding existing locations
 * - Handling non-existent locations
 * - Verifying location attributes
 */
public class LocationGatewayTest {

  private final LocationGateway locationGateway = new LocationGateway();

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    // given
    // when
    Location location = locationGateway.resolveByIdentifier("ZWOLLE-001");

    // then
    assertNotNull(location, "Location should not be null");
    assertEquals("ZWOLLE-001", location.identifier());
    assertEquals(1, location.maxNumberOfWarehouses());
    assertEquals(40, location.maxCapacity());
  }

  @ParameterizedTest
  @ValueSource(strings = {"AMSTERDAM-001", "AMSTERDAM-002", "TILBURG-001", "HELMOND-001", "EINDHOVEN-001", "VETSBY-001", "ZWOLLE-001", "ZWOLLE-002"})
  public void testAllPredefinedLocationsResolve(String locationId) {
    Location location = locationGateway.resolveByIdentifier(locationId);
    assertNotNull(location, "Location " + locationId + " should be resolved");
    assertEquals(locationId, location.identifier());
    assertTrue(location.maxCapacity() > 0, "Max capacity should be positive");
    assertTrue(location.maxNumberOfWarehouses() > 0, "Max warehouses should be positive");
  }

  @Test
  public void testNonExistentLocationReturnsNull() {
    Location location = locationGateway.resolveByIdentifier("NON-EXISTENT");
    assertNull(location, "Non-existent location should return null");
  }

  @Test
  public void testEmptyLocationIdReturnsNull() {
    Location location = locationGateway.resolveByIdentifier("");
    assertNull(location, "Empty location ID should return null");
  }

  @Test
  public void testNullLocationIdReturnsNull() {
    Location location = locationGateway.resolveByIdentifier(null);
    assertNull(location, "Null location ID should return null");
  }

  @Test
  public void testAmsterdamLocationHasCorrectAttributes() {
    Location location = locationGateway.resolveByIdentifier("AMSTERDAM-001");
    assertNotNull(location);
    assertEquals("AMSTERDAM-001", location.identifier());
    assertEquals(5, location.maxNumberOfWarehouses());
    assertEquals(100, location.maxCapacity());
  }

  @Test
  public void testZwolleLocationsHaveDifferentCapacities() {
    Location zwolleOne = locationGateway.resolveByIdentifier("ZWOLLE-001");
    Location zwolleTwo = locationGateway.resolveByIdentifier("ZWOLLE-002");

    assertNotNull(zwolleOne);
    assertNotNull(zwolleTwo);
    assertEquals(40, zwolleOne.maxCapacity());
    assertEquals(50, zwolleTwo.maxCapacity());
  }
}
