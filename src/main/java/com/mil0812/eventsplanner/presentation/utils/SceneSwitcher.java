package com.mil0812.eventsplanner.presentation.utils;

import com.mil0812.eventsplanner.presentation.SpringFXMLLoader;
import com.mil0812.eventsplanner.presentation.controllers.MainMenuController;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Клас для переключення між сторінками
 */
@Component
public class SceneSwitcher {
  final Logger logger = LoggerFactory.getLogger(SceneSwitcher.class);
  private final SpringFXMLLoader fxmlLoader;

  @Autowired
  public SceneSwitcher(SpringFXMLLoader fxmlLoader) {
    this.fxmlLoader = fxmlLoader;
  }

  public void switchScene(String fxmlPath, String stylePath) {
    try {
      logger.info(STR."Завантажуємо FXML з \{fxmlPath}");

      Parent root = fxmlLoader.load(fxmlPath);
      if (stylePath != null) {
        logger.info(STR."Завантажуємо стиль з \{stylePath}");
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource(stylePath)).toExternalForm());
      }
      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.setResizable(false);
      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
