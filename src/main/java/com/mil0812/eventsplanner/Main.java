package com.mil0812.eventsplanner;

import com.mil0812.eventsplanner.persistence.ApplicationConfig;
import com.mil0812.eventsplanner.persistence.connection.ConnectionManager;
import com.mil0812.eventsplanner.persistence.connection.DatabaseInitializer;
import com.mil0812.eventsplanner.persistence.entity.impl.DayTask;
import com.mil0812.eventsplanner.persistence.unit_of_work.impl.DayTaskUnitOfWork;
import com.mil0812.eventsplanner.presentation.SpringFXMLLoader;
import com.mil0812.eventsplanner.presentation.controllers.MainMenuController;
import com.mil0812.eventsplanner.presentation.utils.AlertUtil;
import java.util.UUID;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {
    ConnectionManager connectionManager;
    public static AnnotationConfigApplicationContext springContext;
    public final static Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {

        SpringFXMLLoader loader = springContext.getBean(SpringFXMLLoader.class);
        Scene scene = new Scene(
            loader.load("/com/mil0812/eventsplanner/view/enter-view.fxml"), 922.0, 600.0);

        stage.setTitle("Events Planner");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connectionManager != null) {
            connectionManager.closePool();
        }
    }

    public static void main(String[] args) {
        springContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        var connectionManager = springContext.getBean(ConnectionManager.class);
        var databaseInitializer = springContext.getBean(DatabaseInitializer.class);

        try {
            //databaseInitializer.init();
            launch();

        } finally {
            connectionManager.closePool();
        }
    }
}