package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.Main;
import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.interfaces.UserRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.DayTaskUnitOfWork;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.UserUnitOfWork;
import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import com.mil0812.eventsplanner.presentation.utils.CurrentUser;
import com.mil0812.eventsplanner.presentation.utils.SceneSwitcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnterPageController {

  private final UserRepository userRepository;
  private final SceneSwitcher sceneSwitcher;

  @FXML
  private Button enterButton;

  @FXML
  private TextField usernameTextField;

  @FXML
  private PasswordField passwordTextField;

  @FXML
  private Hyperlink registrationLink;

  @FXML
  private Text usernameError;

  @FXML
  private Text passwordError;

  private List usernameErrorsList;
  private List passwordErrorsList;
  private User foundUser;

  @Autowired
  public EnterPageController(UserRepository userRepository, DayTaskUnitOfWork dayTaskUnitOfWork,
      UserUnitOfWork userUnitOfWork,
      SceneSwitcher sceneSwitcher) {
    this.userRepository = userRepository;
    this.sceneSwitcher = sceneSwitcher;
  }

  @FXML
  void initialize() {

    enterButton.setOnAction(actionEvent -> checkLogInData());
    registrationLink.setOnAction(actionEvent -> openSignUpPage());
  }

  private void openSignUpPage() {
    registrationLink.getScene().getWindow().hide();
    sceneSwitcher.switchScene
        ("/com/mil0812/eventsplanner/view/registration-view.fxml",
            "/com/mil0812/eventsplanner/style/menu.css");
  }

  private void checkLogInData() {
    String usernameText = usernameTextField.getText().trim();
    String passwordText = passwordTextField.getText().trim();

    String fieldsAreEmptyError = "Заповніть, будь ласка, всі поля";
    String userDoesNotExistError = "На жаль, користувача з таким іменем не знайдено";
    String incorrectPasswordError = "Неправильний пароль";

    usernameErrorsList = new ArrayList();
    passwordErrorsList = new ArrayList();

    if (!usernameText.isEmpty() && !passwordText.isEmpty()) {
      Optional<User> optionalUser = userRepository.findByLogin(usernameText);

      if (optionalUser.isPresent()) {
        foundUser = optionalUser.get();
        if (foundUser.password().equals(passwordText)) {
          // Successful login
          logInUser();
        } else {
          // Incorrect password
          passwordErrorsList.add(incorrectPasswordError);
          showErrorAlert(incorrectPasswordError);
        }
      } else {
        // User does not exist
        usernameErrorsList.add(userDoesNotExistError);
        showErrorAlert(userDoesNotExistError);
      }
    } else {
      showErrorAlert(fieldsAreEmptyError);
    }
  }

  private void showErrorAlert(String message) {
    AlertUtil.showInfoAlert("Помилка входу... Перевірте ще раз дані");
  }

  private void logInUser() {
    AlertUtil.showInfoAlert("Ви успішно увійшли в аккаунт!");

    //Setting default user
    CurrentUser.getInstance().setCurrentUser(foundUser);

    registrationLink.getScene().getWindow().hide();
    sceneSwitcher.switchScene
        ("/com/mil0812/eventsplanner/view/main-view.fxml",
            "/com/mil0812/eventsplanner/style/menu.css");
  }
}