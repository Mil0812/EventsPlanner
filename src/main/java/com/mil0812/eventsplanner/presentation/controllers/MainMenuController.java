package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.presentation.SpringFXMLLoader;
import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import com.mil0812.eventsplanner.presentation.utils.CurrentUser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenuController {

  @FXML
  public Button calendarButton;
  @FXML
  public Button eventsButton;
  @FXML
  public Button dayListButton;
  @FXML
  public Button exitButton;
  public ImageView avatarButtonImage;
  @FXML
  public ImageView calendarButtonImage;
  @FXML
  public ImageView eventsButtonImage;
  @FXML
  public ImageView dayListButtonImage;
  @FXML
  public ImageView exitButtonImage;
  @FXML
  public StackPane contentArea;
  @FXML
  private Button profileButton;
  private final SpringFXMLLoader fxmlLoader;
  private String cssFileUrl;

  final static Logger logger = LoggerFactory.getLogger(MainMenuController.class);

  @Autowired
  public MainMenuController(SpringFXMLLoader fxmlLoader) {
    this.fxmlLoader = fxmlLoader;
  }

  @FXML
  void initialize() {
    initializeButtonImages();
    getUserProfile();

    calendarButton.setOnAction(actionEvent -> showHomePage());
    eventsButton.setOnAction(actionEvent -> showEventPage());
    dayListButton.setOnAction(actionEvent -> showDayListPage());
    profileButton.setOnAction(actionEvent -> showProfilePage());
    exitButton.setOnAction(actionEvent -> System.exit(0));

    //Subscription)
      CurrentUser.getInstance().userAvatarProperty().addListener((observable, oldImage, newImage) -> {
      if (newImage != null) {
        avatarButtonImage.setImage(newImage);
      }
    });

  }

  private void initializeButtonImages() {
    calendarButtonImage.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/com/mil0812/eventsplanner/images/calendar.png"))));
    eventsButtonImage.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/com/mil0812/eventsplanner/images/events.png"))));
    dayListButtonImage.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/com/mil0812/eventsplanner/images/day_list.png"))));
    exitButtonImage.setImage(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/com/mil0812/eventsplanner/images/exit.png"))));
  }

  private void getUserProfile() {

    try {
      Image currentUserAvatar = CurrentUser.getInstance().getCurrentUserAvatar();
      if (currentUserAvatar != null) {

        avatarButtonImage.setImage(currentUserAvatar); // Встановлення зображення в ImageView
        CurrentUser.getInstance().setUserAvatar(currentUserAvatar); //ініціалізація стану
      }
    } catch (Exception e) {
      logger.error(STR."Щось не те з завантаженням користувача...\{e}");
    }


      //Заокруглене зображення на кнопці - аватарка
      double width = 100.0;
      double height = 100.0;
      avatarButtonImage.setFitWidth(width);
      avatarButtonImage.setFitHeight(height);

      Rectangle clip = new Rectangle(width, height);
      clip.setArcWidth(20);
      clip.setArcHeight(20);

      avatarButtonImage.setClip(clip);

  }

  private void showHomePage() {
    loadPageInArea("/com/mil0812/eventsplanner/view/calendar-view.fxml");
  }

  private void showEventPage() {
    loadPageInArea("/com/mil0812/eventsplanner/view/event-view.fxml");
  }

  private void showDayListPage() {
    loadPageInArea("/com/mil0812/eventsplanner/view/day-tasks-view.fxml");
  }

  private void showProfilePage() {
    loadPageInArea("/com/mil0812/eventsplanner/view/profile-view.fxml");
  }

  private void loadPageInArea(String fileUrl) {
    cssFileUrl="/com/mil0812/eventsplanner/style/style.css";
    try {
      //FXML
      Parent root = fxmlLoader.load(fileUrl);

      //CSS
      try {
        String css = Objects.requireNonNull(getClass().getResource(cssFileUrl)).toExternalForm();
        root.getStylesheets().add(css);
      }
      catch (Exception e){
        logger.error(STR."Помилка при завантаженні файлику .css: \{e}");
      }

      contentArea.getChildren().clear();
      contentArea.getChildren().add(root);
    } catch (IOException ex) {
      logger.error(STR."Помилка при завантаженні сторінки \{fileUrl}", ex);
      AlertUtil.showErrorAlert("Не вдалося перейти на іншу сторінку...");
    }
  }
}
