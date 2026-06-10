package com.fulfilment.application.monolith.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Readiness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

  @Inject
  EntityManager em;

  @Override
  @Transactional
  public HealthCheckResponse call() {
    try {
      // Execute a simple query to verify database is online and reachable
      em.createNativeQuery("SELECT 1").getSingleResult();
      return HealthCheckResponse.up("Database connection is healthy");
    } catch (Exception e) {
      return HealthCheckResponse.down("Database connection failed: " + e.getMessage());
    }
  }
}
