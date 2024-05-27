package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.entity.impl.User;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.UserUnitOfWork;
import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import com.mil0812.eventsplanner.presentation.utils.CurrentUser;
import com.mil0812.eventsplanner.presentation.utils.SceneSwitcher;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
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
  private final SceneSwitcher sceneSwitcher;

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



  @Autowired
  public RegistrationPageController(UserUnitOfWork userUnitOfWork, SceneSwitcher sceneSwitcher,
      ConnectionManager connectionManager) {
    this.userUnitOfWork = userUnitOfWork;
    this.sceneSwitcher = sceneSwitcher;
    imageData = defaultImageInBytes();
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

    validateUser();
    checkAllFields();

    if (allFieldsErrorsList.isEmpty()) {
      successValidation = true;
    }

    if (successValidation) {

      //Registration
      User user = new User(null, username, password, firstName, imageData);
      userUnitOfWork.registerNew(user);
      userUnitOfWork.commit();

      //Setting current user for other pages
      CurrentUser.getInstance().setCurrentUser(user);

      //Switching
      signUpButton.getScene().getWindow().hide();
      AlertUtil.showInfoAlert("Ви успішно зареєстровані!");
      sceneSwitcher.switchScene
          ("/com/mil0812/eventsplanner/view/enter-view.fxml",
              "/com/mil0812/eventsplanner/style/menu.css");

    } else {
      AlertUtil.showInfoAlert("Помилка при реєстрації...");
    }
  }

  private byte[] readImage(File file){

    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] data = new byte[(int) file.length()];
      fis.read(data);
      return data;
    } catch (IOException e) {
      logger.error(STR."Помилка зчитування байтів із зображення... \{e}");
      return null;
    }
  }

  private byte[] defaultImageInBytes() {
    try (InputStream is = getClass().getResourceAsStream
        ("/com/mil0812/eventsplanner/images/name.png")) {
      if (is == null) {
        throw new IOException("Дефолтне зображення не знайдено...");
      }
      return is.readAllBytes();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
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