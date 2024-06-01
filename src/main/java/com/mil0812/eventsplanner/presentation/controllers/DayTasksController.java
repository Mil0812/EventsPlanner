package com.mil0812.eventsplanner.presentation.controllers;

import com.mil0812.eventsplanner.Main;
import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.repository.interfaces.DayTaskRepository;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.DayTaskUnitOfWork;
import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import com.mil0812.eventsplanner.presentation.utils.CurrentUser;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DayTasksController {

  @FXML
  public GridPane tasksContainer;
  @FXML
  public GridPane createdTasksContainer;
  @FXML
  public Button viewTasksButton;
  @FXML
  private Button addTaskButton;
  @FXML
  private TextField taskTextField;

  @FXML
  private Label taskLabel;
  @FXML
  private CheckBox completedTaskCheckBox;
  private String taskText;
  private boolean taskCompleteness = false;
  private UUID currentUserId;
  private String currentLabelName;
  private boolean taskAddedSuccessfully = false;
  private int taskCount = 0;

  private final DayTaskUnitOfWork dayTaskUnitOfWork;
  private final DayTaskRepository dayTaskRepository;

  @Autowired
  public DayTasksController(DayTaskUnitOfWork dayTaskUnitOfWork,
      DayTaskRepository dayTaskRepository) {
    this.dayTaskUnitOfWork = dayTaskUnitOfWork;
    this.dayTaskRepository = dayTaskRepository;
  }


  @FXML
  public void initialize(){

    //Отримуємо id поточного користувача
    currentUserId = CurrentUser.getInstance().getCurrentUser().id();

    addTaskButton.setOnAction(actionEvent -> addTask());
    viewTasksButton.setOnAction(actionEvent ->  checkCreatedTasks());
    controlInputLLength();
  }

  private void checkCreatedTasks() {
    Main.logger.info("find All Started!");
    dayTaskRepository.findAllWhere(STR."user_id IN('\{currentUserId}')");

  }


  private void addTask() {
    taskText = taskTextField.getText();
    if(dayTaskUnitOfWork.repository.findByName(taskText).isEmpty()){
      moveElements();
    }
    else{
      AlertUtil.showWarningAlert("Вкажіть унікальну назву, будь ласка");
      taskAddedSuccessfully = false;
    }

    if(taskAddedSuccessfully) registerTaskInDatabase();
  }

  private void moveElements() {
    if(!taskTextField.getText().isEmpty()) {
      taskText = taskTextField.getText();
      taskTextField.setText("");

      taskLabel = new Label();
      taskLabel.setText(taskText);
      taskLabel.getStyleClass().add("task-label");

      completedTaskCheckBox = new CheckBox();
      completedTaskCheckBox.setText("Виконано");
      completedTaskCheckBox.setFont(Font.font(("beer money"), 17.0));

      completedTaskCheckBox.setTextFill(Paint.valueOf("#9f7321"));

      //На ставлення галочки
      completedTaskCheckBox.setOnAction(event -> {
        Integer rowIndex = GridPane.getRowIndex(completedTaskCheckBox);
        Main.logger.info(STR."Row index = \{rowIndex}");

        if (rowIndex != null) {
          // label, що знаходиться в тому ж рядку, що і виділений checkBox
          Label correspondingLabel = getNodeByRowColumnIndex(tasksContainer, rowIndex, Label.class);

          if (completedTaskCheckBox.isSelected()) {
            completedTaskCheckBox.setTextFill(Paint.valueOf("#219F85"));
            if (correspondingLabel != null) {
              correspondingLabel.setTextFill(Paint.valueOf("#219F85"));
            }
            taskCompleteness = true;
          } else {
            completedTaskCheckBox.setTextFill(Paint.valueOf("#9f7321"));
            if (correspondingLabel != null) {
              correspondingLabel.setTextFill(Paint.valueOf("#9f7321"));
            }
            taskCompleteness = false;
          }

          if (correspondingLabel != null) {
            updateTaskInDatabase(correspondingLabel.getText()); // оновлення
          }
        }
      });

      tasksContainer.add(taskLabel, 0, taskCount + 1); // +1, щоб TextBox був завжди перед ними
      tasksContainer.add(completedTaskCheckBox, 1, taskCount + 1);

      taskCount++;
      taskAddedSuccessfully = true;
    }
    else{
      taskAddedSuccessfully = false;
      AlertUtil.showWarningAlert("Впишіть назву завдання, будь ласка");

    }
  }

  /**
   * Модифікація завдання
   * @param currentLabelName - строчка, яку змінюватимемо
   */
  private void updateTaskInDatabase(String currentLabelName) {
    Optional<DayTask> optionalDayTask = dayTaskUnitOfWork.repository.findByName(currentLabelName);

    if (optionalDayTask.isPresent()) {
      DayTask dayTaskToModify = optionalDayTask.get();
      DayTask updatedDayTask = new DayTask(
          dayTaskToModify.id(),
          dayTaskToModify.userId(),
          dayTaskToModify.name(),
          dayTaskToModify.createdAt(),
          taskCompleteness
      );

      //Реєстрація оновленого завдання
      try {
        dayTaskUnitOfWork.registerModified(updatedDayTask);
        dayTaskUnitOfWork.commit();
      } catch (Exception e) {
        AlertUtil.showErrorAlert("Не вдалося оновити статус Вашого завдання у базі даних...");
        Main.logger.error(STR."Problem is \{e}");
      }
    }
    else{
        AlertUtil.showErrorAlert("Не вдалося знайти дані для оновлення...");
    }
  }

  /**
   * Метод для того, щоб отримувати вузол за його типом та індексом рядка
   */
  private <T extends Node> T getNodeByRowColumnIndex(GridPane gridPane, int row, Class<T> nodeClass) {
    for (Node node : gridPane.getChildren()) {
      if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row
          && nodeClass.isInstance(node)) {
        return nodeClass.cast(node);
      }
    }
    return null;
  }


  private void registerTaskInDatabase() {

    // Встановлюємо поточну дату та час
    LocalDateTime createdAt = LocalDateTime.now();

    //Створюємо новий об'єкт таска
    DayTask dayTask = new DayTask(null, currentUserId, taskText, createdAt, taskCompleteness);

    //Реєстрація завдання
    try{
      dayTaskUnitOfWork.registerNew(dayTask);
      dayTaskUnitOfWork.commit();
    }
    catch (Exception e){
      AlertUtil.showErrorAlert("Не вдалося зберегти Ваше завдання у базу даних...");
      Main.logger.error(STR."Problem is \{e}");
    }
    taskCompleteness = false;
  }

  /**
   * Щоб довжина введеного тексту була не більше 40 символів
   */
  private void controlInputLLength() {
    // Set the text formatter with a max length of 40 characters
    taskTextField.setTextFormatter(new TextFormatter<>(change -> {
      if (change.getControlNewText().length() <= 40) {
        return change;
      } else {
        return null;
      }
    }));
  }
}
