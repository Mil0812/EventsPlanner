package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;

@Component
public class EventController {

  @FXML
  private FlowPane imageFlowPane;

  private byte[] eventImage;
  private ImageView selectedImageView;
  private static final String BASE_IMAGES_PATH = "src/main/resources/com/mil0812/eventsplanner/images/event_options";

  @FXML
  public void initialize() {
    // relative URLs of the images
    String[] imagePaths = {
        "/birthday.png",
        "/conference.png",
        "/movie.png",
        "/party.png",
        "/performance.png",
        "/video-meeting.png",
        "/learning.png"
    };

    // Додаємо картинки до FlowPane
    for (String relativePath : imagePaths) {
      String fullPath = BASE_IMAGES_PATH + relativePath;
      ImageView imageView = new ImageView(new Image(STR."file:\{fullPath}"));
      imageView.setFitHeight(88);
      imageView.setFitWidth(85);
      imageView.setPickOnBounds(true);
      imageView.setPreserveRatio(true);
      imageView.setCursor(Cursor.HAND);
      imageView.setOnMouseClicked(mouseEvent -> {
        highlightSelectedImage(imageView);
        loadEventImageBytes(fullPath);
      });
      imageFlowPane.getChildren().add(imageView);
    }
  }

  private void highlightSelectedImage(ImageView imageView) {
    if (selectedImageView != null) {
      selectedImageView.getStyleClass().remove("selected-image");
    }
    imageView.getStyleClass().add("selected-image");
    selectedImageView = imageView;
  }

  private void loadEventImageBytes(String chosenImagePath) {
    try (InputStream is = new FileInputStream(chosenImagePath)) {
      eventImage = is.readAllBytes();
      AlertUtil.showInfoAlert(STR."Event image: \{chosenImagePath}");
    } catch (IOException e) {

      AlertUtil.showErrorAlert("Не вдалося завантажити зображення...");
    }
  }
}
