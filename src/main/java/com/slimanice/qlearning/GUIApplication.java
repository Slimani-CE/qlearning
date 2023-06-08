package com.slimanice.qlearning;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Q Learning Algorithm");
        stage.setScene(scene);
        stage.getIcons().add(new Image(GUIApplication.class.getResource("media/robot.png").openStream()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}