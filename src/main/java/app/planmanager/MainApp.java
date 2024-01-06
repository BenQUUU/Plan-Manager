package app.planmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import static app.planmanager.WindowCreator.createNewWindow;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        createNewWindow("loginWindow.fxml", "Logowanie");
    }

    public static void main(String[] args) {
        launch();
    }
}