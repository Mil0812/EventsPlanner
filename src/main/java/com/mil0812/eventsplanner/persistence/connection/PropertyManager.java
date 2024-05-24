package com.mil0812.persistence.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Клас, де зчитуються значення з application.properties
 */
@Component
public final class PropertyManager {

  private static final Properties PROPERTIES = new Properties();
  final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

  public PropertyManager() {
    loadProperties();
  }
  public PropertyManager(InputStream applicationProperties) {
    try (applicationProperties) {
      PROPERTIES.load(applicationProperties);
    } catch (IOException e) {
      logger.error("Помилка при завантаженні файлику властивостей програми %s...".formatted(e));
      throw new RuntimeException(e);
    }
  }

  public String get(String key) {
    return PROPERTIES.getProperty(key);
  }

  private void loadProperties() {
    try (InputStream applicationProperties =
        PropertyManager.class
            .getClassLoader()
            .getResourceAsStream("application.properties")) {
      PROPERTIES.load(applicationProperties);
    } catch (IOException e) {
      logger.error("Не вдалося прочитати властивості програми %s...".formatted(e));
      throw new RuntimeException(e);
    }
  }

}
