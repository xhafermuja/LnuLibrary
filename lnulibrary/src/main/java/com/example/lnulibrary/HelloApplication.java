package com.example.lnulibrary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

//    private final Logger logger = Logger.getLogger(HelloApplication.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
//        PropertyConfigurator.configure(getClass().getResource("/com/example/lnulibrary/log4j.properties"));
//        logger.info("Starting");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}