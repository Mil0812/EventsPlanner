package com.mil0812.eventsplanner.presentation.utils;

import com.mil0812.eventsplanner.persistence.entity.impl.User;

public class CurrectUser {
  private static CurrectUser instance;
  private User currentUser;

  private CurrectUser() {}

  public static CurrectUser getInstance() {
    if (instance == null) {
      instance = new CurrectUser();
    }
    return instance;
  }

  public User getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(User user) {
    this.currentUser = user;
  }

  // Додавання методу для отримання зображення поточного користувача
  public byte[] getCurrentUserImage() {
    if (currentUser != null) {
      return currentUser.avatar();
    } else {
      return null;
    }
  }

}
