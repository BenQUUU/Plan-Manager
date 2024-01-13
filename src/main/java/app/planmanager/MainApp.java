package app.planmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import static app.planmanager.WindowCreator.createNewWindow;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        createNewWindow("mainWindow.fxml", "Plan zajęć");
    }

    public static void main(String[] args) {
        launch();
    }
}