package com.mil0812.eventsplanner.persistence.unit_of_work;

import com.mil0812.eventsplanner.persistence.entity.Entity;
import com.mil0812.eventsplanner.persistence.exception.EntityNotFound;
import com.mil0812.eventsplanner.persistence.repository.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Клас, що реалізує патерн "UnitOfWork". В ньому реєструються зміни CRUD, які мають бути виконані і
 * потім у методі commit з'єднує їх. Цей підхід дозволяє групувати зміни до сутностей та виконувати
 * їх у пакеті, що сприяє оптимізації роботи з базою даних та зменшує кількість запитів до неї.
 *
 * @param <T> - потрібна нам сутність
 */
public class GeneralUnitOfWork<T extends Entity> implements UnitOfWork<T> {

  private static final Logger logger = LoggerFactory.getLogger(GeneralUnitOfWork.class);
  private final Map<UnitActions, List<T>> context;
  private final Repository<T> repository;
  private Set<T> entities;

  public GeneralUnitOfWork(Repository<T> repository) {
    this.repository = repository;
    context = new HashMap<>();
  }

  @Override
  public void registerNew(T entity) {
    logger.info("Реєстрація сутності по id {} для вставки", entity.id());
    logger.info(STR."~entity = \{entity}");
    register(entity, UnitActions.INSERT);
  }

  @Override
  public void registerModified(T entity) {
    logger.info("Реєстрація сутності по id {} для модифікації", entity.id());
    register(entity, UnitActions.MODIFY);
  }

  @Override
  public void registerDeleted(T entity) {
    logger.info("Реєстрація сутності по id {} для видалення", entity.id());
    register(entity, UnitActions.DELETE);
  }

  @Override
  public void registerDeleted(UUID id) {
    logger.info("Реєстрація сутності по UUID {} для видалення", id);
    Entity entity = () -> id;
    register((T) entity, UnitActions.DELETE);
  }

  /**
   * Метод для реєстрації якоїсь сутності до виконання певної операції, що описана в operation
   *
   * @param entity    - сутність, з якою працюємо
   * @param operation - операція з енаму, яку виконуємо
   */
  private void register(T entity, UnitActions operation) {
    var entitiesToOperate = context.get(operation);

    if (entitiesToOperate == null) {
      entitiesToOperate = new ArrayList<>();
    }

    entitiesToOperate.add(entity);
    context.put(operation, entitiesToOperate);
    logger.info(STR."~ context putted operation \{operation}");
  }

  /**
   * Метод для з'єднання попередніх: власне кажучи, основна реалізація даного патерну (UnitOfWork)
   */
  @Override
  public void commit() {
    if (context.isEmpty()) {
      return;
    }
    logger.info("Комміт запущено!");
    if (context.containsKey(UnitActions.INSERT)) {
      commitInsert();
    }
    if (context.containsKey(UnitActions.MODIFY)) {
      commitModify();
    }
    if (context.containsKey(UnitActions.DELETE)) {
      commitDelete();
    }
    logger.info("Комміт завершено!");
    context.clear();
  }

  private void commitInsert() {
    var entitiesToBeInserted = context.get(UnitActions.INSERT);
    logger.info(STR."~ entities to be inserted = \{entitiesToBeInserted}");
    entities = repository.save(entitiesToBeInserted);
  }

  private void commitModify() {
    var entitiesToBeModified = context.get(UnitActions.MODIFY);
    entities = repository.save(entitiesToBeModified);
  }

  private void commitDelete() {
    var entitiesToBeDeleted = context.get(UnitActions.DELETE);
    repository.delete(entitiesToBeDeleted.stream().map(Entity::id).toList());
  }

  @Override
  public T getEntity(UUID id) {
    return entities.stream().filter(e -> e.id().equals(id)).findFirst()
        .orElseThrow(() -> new EntityNotFound(
            "Спершу додайте чи оновіть сутність з таким id"));
  }

  @Override
  public T getEntity() {
    return entities.stream().findFirst().orElseThrow();
  }


  @Override
  public Set<T> getEntities() {
    return entities;
  }
}
