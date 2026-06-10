package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = warehouse.businessUnitCode;
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.createdAt = warehouse.createdAt;
    dbWarehouse.archivedAt = warehouse.archivedAt;
    
    this.persist(dbWarehouse);
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse dbWarehouse = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (dbWarehouse == null) {
      throw new IllegalArgumentException(
          "Warehouse with business unit code '" + warehouse.businessUnitCode + "' does not exist");
    }
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.archivedAt = warehouse.archivedAt;

    getEntityManager().flush();
  }

  @Override
  public List<Warehouse> search(
      String location,
      Integer minCapacity,
      Integer maxCapacity,
      String sortBy,
      String sortOrder,
      Integer page,
      Integer pageSize) {

    StringBuilder query = new StringBuilder("archivedAt IS NULL");
    java.util.Map<String, Object> params = new java.util.HashMap<>();

    if (location != null && !location.trim().isEmpty()) {
      query.append(" AND location = :location");
      params.put("location", location);
    }
    if (minCapacity != null) {
      query.append(" AND capacity >= :minCapacity");
      params.put("minCapacity", minCapacity);
    }
    if (maxCapacity != null) {
      query.append(" AND capacity <= :maxCapacity");
      params.put("maxCapacity", maxCapacity);
    }

    // Dynamic sorting validation to prevent SQL injection
    String sortField = "createdAt";
    if ("capacity".equalsIgnoreCase(sortBy)) {
      sortField = "capacity";
    }
    String direction = "asc";
    if ("desc".equalsIgnoreCase(sortOrder)) {
      direction = "desc";
    }

    query.append(" ORDER BY ").append(sortField).append(" ").append(direction);

    int pageNum = (page != null) ? page : 0;
    int size = (pageSize != null) ? pageSize : 10;
    if (size > 100) {
      size = 100;
    }

    var panacheQuery = find(query.toString(), params);
    panacheQuery.page(io.quarkus.panache.common.Page.of(pageNum, size));

    return panacheQuery.list().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void remove(Warehouse warehouse) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'remove'");
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    DbWarehouse dbWarehouse = find("businessUnitCode", buCode).firstResult();
    return dbWarehouse != null ? dbWarehouse.toWarehouse() : null;
  }
}
