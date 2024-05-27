package com.mil0812.eventsplanner.persistence.connection;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  final static Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
  private final ConnectionManager connectionManager;

  public DatabaseInitializer(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  public void init() {
    Connection connection = null;
    try {
      connection = connectionManager.get();
      Statement statement = connection.createStatement();

      InputStream ddlInputStream = DatabaseInitializer.class.getResourceAsStream("/ddl.sql");
      if (ddlInputStream == null) {
        throw new RuntimeException("Не вдалося знайти файл ddl.sql");
      }

      InputStream dmlInputStream = DatabaseInitializer.class.getResourceAsStream("/dml.sql");
      if (dmlInputStream == null) {
        throw new RuntimeException("Не вдалося знайти файл dml.sql");
      }

      executeScript(statement, ddlInputStream);
      logger.info("База даних успішно створена!");
      executeScript(statement, dmlInputStream);
      logger.info("База даних успішно заповнена даними!");

    } catch (SQLException e) {
      logger.error(STR."Помилка при створенні або заповненні бази даних: \{e.getMessage()}");
    } catch (Exception e) {
      logger.error(STR."Помилка: \{e.getMessage()}");
    } finally {
      if (connection != null) {
        connectionManager.release(connection);
      }
    }
  }

  private static void executeScript(Statement statement, InputStream inputStream)
      throws SQLException {
    Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
    StringBuilder scriptBuilder = new StringBuilder();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (!line.isEmpty()) {
        scriptBuilder.append(line).append("\n");
        if (line.endsWith(";")) {
          String script = scriptBuilder.toString();
          statement.execute(script);
          scriptBuilder.setLength(0);
        }
      }
    }
  }
}