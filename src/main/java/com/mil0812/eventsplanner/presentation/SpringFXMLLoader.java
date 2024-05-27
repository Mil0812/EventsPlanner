package com.mil0812.eventsplanner.presentation;

import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

  public Parent load(String fxmlPath) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setControllerFactory(context::getBean);
    try (InputStream fxmlStream = getClass().getResourceAsStream(fxmlPath)) {
      return loader.load(fxmlStream);
    }
  }
}
