package com.mil0812.eventsplanner.presentation.controllers;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Фабрика контролерів.
 * Потрібна для налаштування FXMLLoader так, щоб він використовував Spring для створення контролерів.
 */
@Component
public class SpringFXMLLoader {
  private final ApplicationContext context;

  public SpringFXMLLoader(ApplicationContext context) {
    this.context = context;
  }

  public Object load(String fxmlPath) throws IOException {
    try (InputStream fxmlStream = getClass().getResourceAsStream(fxmlPath)) {
      FXMLLoader loader = new FXMLLoader();
      loader.setControllerFactory(context::getBean);
      return loader.load(fxmlStream);
    }
  }
}
