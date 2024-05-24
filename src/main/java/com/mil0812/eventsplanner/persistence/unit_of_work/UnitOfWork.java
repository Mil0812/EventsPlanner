package com.mil0812.eventsplanner.persistence.unitOfWork;

import java.util.Set;
import java.util.UUID;

public interface UnitOfWork<T> {

  String INSERT = "INSERT";
  String DELETE = "DELETE";
  String MODIFY = "MODIFY";

  void registerNew(T entity);

  void registerModified(T entity);

  void registerDeleted(T entity);

  void registerDeleted(UUID id);

  void commit();

  T getEntity(UUID id);

  T getEntity();


  Set<T> getEntities();


}
