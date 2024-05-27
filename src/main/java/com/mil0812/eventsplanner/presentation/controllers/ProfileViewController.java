package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.interfaces.UserRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.UserUnitOfWork;
import com.mil0812.eventsplanner.presentation.utils.CurrentUser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileViewController {


  @FXML
  public Label nameLabel;
  @FXML
  private ImageView avatarImage;
  private String selectedAvatarPath;
  private byte[] avatarInBytes;
  private final UserUnitOfWork userUnitOfWork;
  private final UserRepository userRepository;

  @Autowired
  public ProfileViewController(UserUnitOfWork userUnitOfWork, UserRepository userRepository) {
    this.userUnitOfWork = userUnitOfWork;
    this.userRepository = userRepository;
  }

  @FXML
  private void initialize(){
    initializeUserData();
    avatarImage.setOnMouseClicked(mouseEvent -> uploadAvatar());

    //Subscription)
    CurrentUser.getInstance().userAvatarProperty().addListener((observable, oldImage, newImage) -> {
      if (newImage != null) {
        avatarImage.setImage(newImage);
      }
    });
  }

  private void initializeUserData() {
    try {
      Image currentUserAvatar = CurrentUser.getInstance().getCurrentUserAvatar();
      if (currentUserAvatar != null) {
        avatarImage.setImage(currentUserAvatar);
      }
      //Ім'я
      nameLabel.setText(CurrentUser.getInstance().getCurrentUser().firstName());

    } catch (Exception e) {
      MainMenuController.logger.error(STR."Щось не те з завантаженням користувача...\{e}");
    }
  }

    private void uploadAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Вибір зображення профілю");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Файли зображення", "*.png", "*.jpg")
        );
        File selectedFile = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());
        if (selectedFile != null) {
          selectedAvatarPath = selectedFile.getPath();
          Image image = new Image(selectedFile.toURI().toString());
          avatarImage.setImage(image);
          avatarInBytes = readImageToBytes(selectedFile);

          //Оновлення юзера
          String firstName, login, password;
          UUID id;
          firstName = CurrentUser.getInstance().getCurrentUser().firstName();
          password = CurrentUser.getInstance().getCurrentUser().password();
          login = CurrentUser.getInstance().getCurrentUser().login();
          id = CurrentUser.getInstance().getCurrentUser().id();

          //Реєстрація його в базу даних
          User currentUser = new User(id, login, password, firstName, avatarInBytes);
          userUnitOfWork.registerModified(currentUser);
          userUnitOfWork.commit();

          //Оновлення стану користувача ("Observer")
          CurrentUser.getInstance().setUserAvatar(image);
        }
    }


  private byte[] readImageToBytes(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] data = new byte[(int) file.length()];
      fis.read(data);
      return data;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
