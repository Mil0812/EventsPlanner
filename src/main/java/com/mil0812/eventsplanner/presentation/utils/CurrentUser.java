package com.mil0812.eventsplanner.presentation.utils;

import com.mil0812.eventsplanner.persistence.entity.impl.User;
import java.io.ByteArrayInputStream;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/**
 * Паттерн "Observer"
 */
public class CurrentUser {
  private static CurrentUser instance;
  private User currentUser;
  private ObjectProperty<Image> userAvatar = new SimpleObjectProperty<>();


  private CurrentUser() {}

  public static synchronized CurrentUser getInstance() {
    if (instance == null) {
      instance = new CurrentUser();
    }
    return instance;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;

    if (user != null && user.avatar() != null) {
      Image image = new Image(new ByteArrayInputStream(user.avatar()));
      setUserAvatar(image);
    }
  }

  public ObjectProperty<Image> userAvatarProperty() {
    return userAvatar;
  }

  public Image getCurrentUserAvatar() {
    return userAvatar.get();
    /*if (currentUser != null) {
      return currentUser.avatar();
    } else {
      return null;
    }*/
  }
  public void setUserAvatar(Image userAvatar) {
    this.userAvatar.set(userAvatar);
  }

}
