
package com.mil0812.eventsplanner.persistence.repository;

import static java.lang.StringTemplate.STR;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.Entity;
import com.mil0812.eventsplanner.persistence.exception.EntityNotFound;
import com.mil0812.eventsplanner.persistence.exception.ErrorOnDelete;
import com.mil0812.eventsplanner.persistence.exception.ErrorOnUpdate;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.StringTemplate.STR;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.Entity;
import com.mil0812.eventsplanner.persistence.exception.EntityNotFound;
import com.mil0812.eventsplanner.persistence.exception.ErrorOnDelete;
import com.mil0812.eventsplanner.persistence.exception.ErrorOnUpdate;
import com.mil0812.eventsplanner.persistence.repository.mappers.RowMapper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericJdbcRepository<T extends Entity> implements Repository<T> {

  private final ConnectionManager connectionManager;
  private final RowMapper<T> rowMapper;
  private final String tableName;
  final Logger logger = LoggerFactory.getLogger(GenericJdbcRepository.class);

  public GenericJdbcRepository(
      ConnectionManager connectionManager, RowMapper<T> rowMapper, String tableName) {
    this.connectionManager = connectionManager;
    this.rowMapper = rowMapper;
    this.tableName = tableName;
  }

  @Override
  public Optional<T> findById(UUID id) {
    return findBy("id", id);
  }

  @Override
  public Optional<T> findBy(String column, Object value) {
    final String sql =
        STR."""
                    SELECT *
                      FROM \{tableName}
                     WHERE \{column} = ?
                """;

    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, value, Types.OTHER);
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      return Optional.ofNullable(rowMapper.mapRow(resultSet));
    } catch (SQLException e) {
      return Optional.empty();
    }
  }

  @Override
  public Set<T> findAllWhere(String whereQuery) {
    logger.info("~ 1 step");
    try (Connection connection = connectionManager.get()) {
      return findAllWhere(whereQuery, connection);
    } catch (SQLException throwables) {
      throw new EntityNotFound(
          STR."Помилка при отриманні всіх запитів з таблиці: \{tableName}");
    }
  }

  private Set<T> findAllWhere(String whereQuery, Connection connection) {
    logger.info("~ 2 step");
    logger.info(STR."connection = \{connection}");

    final String sql = STR."""
            SELECT *
              FROM \{tableName}
             WHERE \{whereQuery}
        """;

    logger.info(STR."sql - \{sql}");

    try (PreparedStatement statement = connection.prepareStatement(sql)) {

      logger.info(STR."~sql = \{sql}");
      logger.info(STR."~prepared statement = \{statement}");

      ResultSet resultSet = statement.executeQuery();
      Set<T> entities = new LinkedHashSet<>();
      while (resultSet.next()) {
        entities.add(rowMapper.mapRow(resultSet));
      }
      logger.info(STR."entities - \{entities}");
      return entities;
    } catch (SQLException throwables) {
      System.out.println(STR."Exception is \{throwables}");
      throw new EntityNotFound(
          STR."Помилка при отриманні всіх запитів з таблиці: \{tableName}");

    }
  }

  @Override
  public Set<T> findAll() {
    final String sql = STR."""
            SELECT *
              FROM \{tableName}
        """;

    try (Connection connection = connectionManager.get();
      PreparedStatement statement = connection.prepareStatement(sql)) {
      ResultSet resultSet = statement.executeQuery();
      Set<T> entities = new LinkedHashSet<>();
      while (resultSet.next()) {
        entities.add(rowMapper.mapRow(resultSet));
      }
      return entities;
    } catch (SQLException throwables) {
      throw new EntityNotFound(
          STR."Помилка при отриманні всіх запитів з таблиці: \{tableName}...");
    }
  }

  @Override
  public Set<T> findAll(int offset, int limit) {
    return findAll(offset, limit, "id", true);
  }

  /**
   * Знаходить всі об'єкти класу, використовуючи такі параметри:
   *
   * @param offset     - зсув відносно початку запиту
   * @param limit      - максимальна кількість результатів. що мають бути повернуті
   * @param sortColumn - стовпець, за яким відбувається сортування
   * @param ascending  - 1 - ASK, 2 - DESK
   * @return - список знайдених об'єктів
   */
  @Override
  public Set<T> findAll(int offset, int limit, String sortColumn, boolean ascending) {
    return findAll(offset, limit, sortColumn, ascending, new HashMap<>());
  }

  public Set<T> findAll(int offset, int limit, String sortColumn, boolean ascending,
      Map<String, Object> filters) {
    return findAll(offset, limit, sortColumn, ascending, filters, "");
  }

  @Override
  public Set<T> findAll(int offset, int limit, String sortColumn, boolean ascending,
      Map<String, Object> filters, String where) {
    StringBuilder whereClass = new StringBuilder(!where.isBlank() ? where : "1 = 1");
    List<Object> values = new ArrayList<>();
    filters.forEach((column, value) -> {

      if (value instanceof String) {
        whereClass.append(STR." AND \{column} LIKE ?");
        values.add(STR."%\{value}%");
      } else {
        whereClass.append(STR." AND \{column} = ?");
        values.add(value);
      }
    });

    String sortDirection = ascending ? "ASC" : "DESC";
    String sql = STR."""
              SELECT *
                FROM \{tableName}
                WHERE \{whereClass}
             ORDER BY \{sortColumn} \{sortDirection}
                LIMIT ?
               OFFSET ?
        """;

    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      for (int i = 0; i < values.size(); i++) {
        statement.setObject(i + 1, values.get(i), Types.OTHER);
      }
      statement.setInt(values.size() + 1, limit);
      statement.setInt(values.size() + 2, offset);

      ResultSet resultSet = statement.executeQuery();
      Set<T> entities = new LinkedHashSet<>();
      while (resultSet.next()) {
        entities.add(rowMapper.mapRow(resultSet));
      }
      return entities;
    } catch (SQLException e) {
      throw new EntityNotFound(
          "Помилка при отриманні даних з таблиці: %s...".formatted(tableName));
    }
  }

  @Override
  public long count() {
    final String sql = STR."""
            SELECT COUNT(ID)
              FROM \{tableName}
        """;

    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      long count = resultSet.getLong(1);
      return count;
    } catch (SQLException throwables) {
      throw new EntityNotFound(
          STR."Помилка при отриманні кількості записів з таблиці: \{tableName}...");
    }
  }

  @Override
  public T save(final T entity) {
    var values = tableValues(entity);

    T newEntity;
    if (Objects.isNull(entity.id())) {
      UUID newId = UUID.randomUUID();
      values.put("id", newId);
      newEntity = insert(values);
    } else {
      values.put("id", entity.id());
      newEntity = update(values);
    }

    return newEntity;
  }

  protected T insert(Map<String, Object> values) {
    var attributes = values.keySet();
    String attributesString = String.join(", ", attributes);
    String placeholders =
        Stream.generate(() -> "?")
            .limit(attributes.size() + 1)
            .collect(Collectors.joining(", "));
    String sql =
        STR."""
                INSERT INTO \{tableName} (\{
            attributesString})
                VALUES (\{placeholders})
        """;

    if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
      values.put("created_at", LocalDateTime.now());
    }
    int idIndex = 0;

    return updateExecute(values.values(), sql, "Помилка при додаванні нового запису в таблицю...");
  }

  protected T update(Map<String, Object> values) {
    var attributes = values.keySet();
    String attributesString =
        attributes.stream()
            .filter(a -> !a.equals("created_at") && !a.equals("id"))
            .map(a -> STR."\{a} = ?")
            .collect(Collectors.joining(", "));
    String sql = STR."""
                          UPDATE \{tableName}
                             SET \{attributesString}
                           WHERE id = ?
                         """;
    logger.info(STR."SQL-запит на оновлення: \{sql}");


    int idIndex = attributes.size();

    return updateExecute(values.values(), sql,
        "Помилка при оновленні існуючого запису в таблиці...");
  }

  private T updateExecute(Collection<Object> rawValues, String sql, String exceptionMessage) {
    List<Object> values = new ArrayList<>(rawValues);
    logger.info(STR."Список значень: \{values}");
    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      for (int i = 0; i < values.size(); i++) {
        statementSetter(values, i, statement);
      }
      statement.executeUpdate();
      return findById((UUID) values.getLast()).orElseThrow();
    } catch (SQLException | NoSuchElementException e) {
      throw new ErrorOnUpdate(exceptionMessage + e.getMessage());
    }
  }

  @Override
  public Set<T> save(Collection<T> entities) {
    Set<T> results;
    List<Map<String, Object>> listOfValues = new ArrayList<>(new LinkedHashSet<>());
    boolean allNew = true; // Змінна для перевірки, чи всі об'єкти нові

    for (var entity : entities) {
      var values = tableValues(entity);
      values.put("id", Objects.isNull(entity.id()) ? UUID.randomUUID() : entity.id());

      listOfValues.add(values);
      logger.info(STR."~ id = \{entity.id()}");
      logger.info(STR."~ list of values = \{listOfValues}");
    }

    if (entities.stream().allMatch(e -> Objects.isNull(e.id()))) {
      results = batchInsert(listOfValues);
    } else {
      results = batchUpdate(listOfValues);
    }
    return results;
  }


  protected Set<T> batchInsert(List<Map<String, Object>> listOfValues) {
    var attributes = listOfValues.getFirst().keySet();
    String attributesString = String.join(", ", attributes);
    String placeholders =
        Stream.generate(() -> "?")
            .limit(attributes.size())
            .collect(Collectors.joining(", "));
    String sql =
        STR."""
                INSERT INTO \{tableName} (\{
            attributesString})
                VALUES (\{placeholders})
        """;

   /* logger.info(STR."~ insert sql = \{sql}");*/

    if (attributes.stream().anyMatch(a -> a.equals("created_at"))) {
      listOfValues.forEach(values -> {
        values.put("created_at", LocalDateTime.now()); // createdAt
      });
    }

    return batchExecute(listOfValues, sql, "Помилка при додаванні нового запису в таблицю...");
  }

  protected Set<T> batchUpdate(List<Map<String, Object>> listOfValues) {
    var attributes = listOfValues.getFirst().keySet();
    String attributesString =
        attributes.stream()
            .filter(a -> !a.equals("id"))
            .map(a -> STR."\{a} = ?")
            .collect(Collectors.joining(", "));
    String sql =
        STR."""
                      UPDATE \{tableName}
                         SET \{attributesString}
                       WHERE id = ?
                    """;
    logger.info(STR."~ sql запит на оновлення - \{sql}");

    return batchExecute(listOfValues, sql, "Помилка при оновленні існуючого запису в таблиці...");
  }

  private Set<T> batchExecute(List<Map<String, Object>> listOfRawValues, String sql,
      String exceptionMessage) {
    Set<T> results;
    var listOfValues = listOfRawValues.stream()
        .map(values -> new ArrayList<>(values.values())).toList();
    try (Connection connection = connectionManager.get()) {
      connection.setAutoCommit(false);

      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        logger.info("batch execute list of values cycle is started!");
        for (var values : listOfValues) {
          for (int i = 0; i < values.size(); i++) {
            statementSetter(values, i, statement);
            logger.info(STR."batchExecute - values cycle - element \{i}, values \{values}");
          }
          logger.info(STR."~ statement = \{statement}");
          statement.addBatch();
        }
        logger.info("batch execute list of values cycle is finished!");

        statement.executeBatch();

        results = getEntitiesAfterBatchExecute(listOfValues, connection);
        logger.info(STR."~ results = \{results}");

        if (results.isEmpty() || listOfValues.size() != results.size()) {
          connection.rollback();
          throw new ErrorOnUpdate(exceptionMessage);
        } else {
          connection.commit();
        }
      } catch (SQLException throwables) {
        connection.rollback();
        throw new ErrorOnUpdate(exceptionMessage);
      }
    } catch (SQLException e) {
      throw new ErrorOnUpdate("Помилка при роботі з підключенням до бази даних...");
    }

    return results;
  }

  private static void statementSetter(List<Object> values, int i, PreparedStatement statement)
      throws SQLException {
    if (values.get(i) instanceof Enum) {
      statement.setString(i + 1, values.get(i).toString());
    } else if (values.get(i) instanceof byte[] && Objects.nonNull(values.get(i))) {
      statement.setBytes(i + 1, (byte[]) values.get(i));
    } else {
      statement.setObject(i + 1, values.get(i), Types.OTHER);
    }
  }

  private Set<T> getEntitiesAfterBatchExecute(List<ArrayList<Object>> listOfValues,
      Connection connection) {
    Set<T> results;
    List<String> ids = listOfValues.stream().map(values -> {
      UUID id = (UUID) values.stream()
          .filter(UUID.class::isInstance)
          .reduce((first, second) -> second) //для знаходження останнього елемента типу UUID
          .orElseThrow(() -> new NoSuchElementException("UUID не знайдено..."));

      logger.info(STR."~ id = \{id}");
      return STR."'\{id.toString()}'";
    }).toList();

    results = findAllWhere(STR."id IN(\{String.join(", ", ids)})", connection);
    return results;
  }

  @Override
  public boolean delete(UUID id) {
    final String sql =
        STR."""
                    DELETE FROM \{tableName}
                          WHERE id = ?
                """;

    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setObject(1, id, Types.OTHER);

      return statement.executeUpdate() > 0;
    } catch (SQLException throwables) {
      throw new ErrorOnDelete(
          STR."Помилка при видаленні запису з таблиці по id: \{id.toString()}...");
    }
  }

  public boolean delete(Collection<UUID> uuid) {
    final String sql =
        STR."""
                    DELETE FROM \{tableName}
                          WHERE id = ?
                """;

    try (Connection connection = connectionManager.get();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      for (var id : uuid) {
        statement.setObject(1, id, Types.OTHER);
        statement.addBatch();
      }
      statement.executeBatch();

      return statement.executeUpdate() > 0;
    } catch (SQLException throwables) {
      throw new ErrorOnDelete(
          STR."Помилка при видаленні записів з таблиці по id: \{uuid.toString()}...");
    }
  }

  protected abstract Map<String, Object> tableValues(T entity);

}