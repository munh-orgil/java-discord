package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("IP_input.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Constants.ImportStyle(scene, this);
        stage.setTitle("MongolHub");
        stage.setScene(scene);
        stage.show();
        Constants.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}