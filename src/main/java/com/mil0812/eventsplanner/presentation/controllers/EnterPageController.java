package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.Main;
import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.interfaces.UserRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.UserUnitOfWork;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnterPageController {

  private final UserRepository userRepository;
  private final UserUnitOfWork userUnitOfWork;

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

  private String name;

  @Autowired
  public EnterPageController(UserRepository userRepository, UserUnitOfWork userUnitOfWork) {
    this.userRepository = userRepository;
    this.userUnitOfWork = userUnitOfWork;
  }

  @FXML
  void initialize() {

    enterButton.setOnAction(actionEvent -> checkLogInData());

    registrationLink.setOnAction(actionEvent -> openSignUpPage());
  }

  private void openSignUpPage() {
    registrationLink.getScene().getWindow().hide();

    try {
      SpringFXMLLoader fxmlLoader = Main.springContext.getBean(SpringFXMLLoader.class);
      Parent root = (Parent) fxmlLoader.load("/com/mil0812/eventsplanner/view/registration-view.fxml");
      Stage stage = new Stage();
      stage.setScene(new Scene(root));
      stage.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
        User user = optionalUser.get();
        if (user.password().equals(passwordText)) {
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
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Помилка");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void logInUser() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Успішна авторизація");
    alert.setHeaderText(null);
    alert.setContentText("Ви успішно увійшли в аккаунт!");
    alert.showAndWait();
  }
}

/*
      if (!(userExists(usernameText, passwordText))) {
        usernameErrorsList.add(userDoesNotExistError);
      } else {
        usernameErrorsList.remove(userDoesNotExistError);
      }
      passwordIsCorrect();
    }
    // logInUser(usernameText, passwordText);

    else {
      System.out.println("Заповніть поля, будь ласка");
    }
  }*/

 /* private boolean userExists(String username, String password) {
    String receivedFirstName = "";

    User user = new User(receivedFirstName, username, password);
    userUnitOfWork.getEntity(user);
    user.setUsername(username);
    user.setPassword(password);
    ResultSet result = databaseHandler.getUser(user);

    int counter = 0;

    // проходимося по користувачам: перевіряємо, чи є такий

    try {
      while (result.next()) {
        name = result.getString("username");
        System.out.println("User name is " + name);
        counter++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (counter >= 1) {
      return true;
    } else {
      return false;
    }
  }

  private boolean passwordIsCorrect() {

    return false;
  }*/