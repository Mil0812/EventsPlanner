package com.mil0812.eventsplanner.presentation.controllers;

import static java.lang.System.out;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.repository.mappers.impl.UserRowMapper;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.UserUnitOfWork;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationPageController {

  private final UserUnitOfWork userUnitOfWork;

  @FXML
  private Button signUpButton;

  @FXML
  private TextField usernameTextInput;

  @FXML
  private TextField nameTextInput;

  @FXML
  private PasswordField passwordConfirmationInput;

  @FXML
  private PasswordField passwordInput;

  @FXML
  private Text usernameErrors;

  @FXML
  private Text passwordErrors;

  @FXML
  private Text passwordConfirmErrors;

  @FXML
  private Text allFieldsErrors;

  private String firstName;
  private String username;
  private String password;
  private String passwordConfirm;
  private List usernameErrorsList;
  private List passwordErrorsList;
  private List passwordConfirmErrorsList;
  private List allFieldsErrorsList;
  private boolean successValidation = false;
  private byte[] imageData;
  private static final Logger logger = LoggerFactory.getLogger(RegistrationPageController.class);

  private final ConnectionManager connectionManager;


  @Autowired
  public RegistrationPageController(UserUnitOfWork userUnitOfWork,
      ConnectionManager connectionManager) {
    this.userUnitOfWork = userUnitOfWork;
    this.connectionManager = connectionManager;
  }

  @FXML
  void initialize() {

    signUpButton.setOnAction(actionEvent -> signUpNewUser());

    initializeElements();

    nameTextInput.textProperty().addListener((obs, oldText, newText) -> initializeElements());

    // лістенери на зміну вмісту у текст філд
    usernameTextInput.textProperty().addListener((obs, oldText, newText) -> {
      initializeElements();
      validateUser();
      usernameErrors.setText(usernameErrorsList.toString());
    });

    passwordInput.textProperty().addListener((obs, oldText, newText) -> {
      initializeElements();
      validateUser();
      passwordErrors.setText(passwordErrorsList.toString());
    });

    passwordConfirmationInput.textProperty().addListener((obs, oldText, newText) -> {
      initializeElements();
      validateUser();
      passwordConfirmErrors.setText(passwordConfirmErrorsList.toString());
    });
  }

  private void initializeElements() {

    username = usernameTextInput.getText();
    password = passwordInput.getText();
    firstName = nameTextInput.getText();
    passwordConfirm = passwordConfirmationInput.getText();
  }


  private void signUpNewUser() {

    String imagePath = "/com/mil0812/eventsplanner/images/avatar.png";

    try {
      try (InputStream inputStream = getClass().getResourceAsStream(imagePath)) {
        if (inputStream == null) {
          throw new FileNotFoundException("Файл за вказаним шляхом не знайдено...");
        }

        byte[] fileBytes = inputStream.readAllBytes();
        imageData = inputStream.readAllBytes();
      }

    } catch (IOException e) {
      logger.error(STR."Помилка зчитування байтів із зображення... \{e}");
    }

    validateUser();
    checkAllFields();

    if (allFieldsErrorsList.isEmpty()) {
      successValidation = true;
    }

    if (successValidation) {
      out.println("Succeed!");
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Успішна реєстрація");
      alert.setHeaderText(null);
      alert.setContentText("Ви успішно зареєстровані!");
      alert.showAndWait();

      User user = new User(username, password, firstName, imageData);
      userUnitOfWork.registerNew(user);
      userUnitOfWork.commit();
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Помилка при реєстрація");
      alert.setHeaderText(null);
      alert.setContentText("Помилка... Перевірте, будь ласка, всі дані");
      alert.showAndWait();
    }
  }

  /**
   * Метод для перевірки всіх полів, щоб заповнити поле, де виводяться загальні помилки. Перевіряє,
   * чи всі поля заповнені і чи нема помилок валідації в жодному з них.
   */
  private void checkAllFields() {
    allFieldsErrorsList = new ArrayList();

    String emptyFieldsError = "Заповність, будь ласка, всі поля";
    String errorsInFieldsError = "Будь ласка, заповність всі поля коректно";
    String userAlreadyExists = "Користувач з таким іменем вже зареєстрований...";

    if (firstName.isEmpty() || username.isEmpty() || password.isEmpty()
        || passwordConfirm.isEmpty()) {
      allFieldsErrorsList.add(emptyFieldsError);
    } else {
      allFieldsErrorsList.remove(emptyFieldsError);
    }
    if (!(usernameErrorsList.isEmpty() && passwordErrorsList.isEmpty()
        && passwordConfirmErrorsList.isEmpty())) {
      allFieldsErrorsList.add(errorsInFieldsError);
      validateUser();
    } else {
      allFieldsErrorsList.remove(errorsInFieldsError);
    }
    if (userUnitOfWork.repository.findByLogin(username).isPresent()) {
      allFieldsErrorsList.add(userAlreadyExists);
    } else {
      allFieldsErrorsList.remove(userAlreadyExists);
    }

    allFieldsErrors.setText(allFieldsErrorsList.toString());
  }

  private void validateUser() {
    usernameErrorsList = new ArrayList<>();
    passwordErrorsList = new ArrayList<>();
    passwordConfirmErrorsList = new ArrayList<>();

    String onlyLatinPattern = "^[a-zA-Z\\d]+$";
    String passwordPattern = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$";

    String lessThan5SymbolsError = "Поле має містити більше 5 символів";
    String notOnlyLatinLettersError = "Лише латинські літери";
    String notLikePasswordPattern = "Лише латинські літери + принаймні 1 цифра та 1 велика літера";
    String emptyFieldError = "Заповніть, будь ласка, поле паролю";
    String passwordDoesNotMatch = "Паролі не співпадають...";

    if (username.length() <= 5) {
      usernameErrorsList.add(lessThan5SymbolsError);
    } else {
      usernameErrorsList.remove(lessThan5SymbolsError);
    }
    if (!username.matches(onlyLatinPattern)) {
      usernameErrorsList.add(notOnlyLatinLettersError);
    } else {
      usernameErrorsList.remove(notOnlyLatinLettersError);
    }

    if (password.length() <= 5) {
      passwordErrorsList.add(lessThan5SymbolsError);
    } else {
      passwordErrorsList.remove(lessThan5SymbolsError);
    }

    if (!password.matches(passwordPattern)) {
      passwordErrorsList.add(notLikePasswordPattern);
    } else {
      passwordErrorsList.remove(notLikePasswordPattern);
    }

    if (password.isEmpty()) {
      passwordConfirmErrorsList.add(emptyFieldError);
    } else {
      passwordConfirmErrorsList.remove(emptyFieldError);
    }

    if (!passwordConfirm.equals(password)) {
      passwordConfirmErrorsList.add(passwordDoesNotMatch);
    } else {
      passwordConfirmErrorsList.remove(passwordDoesNotMatch);
    }

    usernameErrors.setText(usernameErrorsList.toString());
    passwordErrors.setText(passwordErrorsList.toString());
    passwordConfirmErrors.setText(passwordConfirmErrorsList.toString());
  }
}