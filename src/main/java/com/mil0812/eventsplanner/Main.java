package com.mil0812.eventsplanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class
            .getResource("main-menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 922, 600);
        stage.setTitle("Events Planner");
        stage.setScene(scene);
        stage.setMaxWidth(922);
        stage.setMaxHeight(600);
        stage.setMinHeight(600);
        stage.setMinWidth(922);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}