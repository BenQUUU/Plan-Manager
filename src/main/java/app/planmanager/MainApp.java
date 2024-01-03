package app.planmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        DBConnector dbConnection = new DBConnector();
        Connection connection = dbConnection.connectToDatabase("planManagerDB", "postgres", "zaq1@WSX");

        User user = dbConnection.getUserEmailAndPassword(connection, "lebron.james@email.com", "hashed_password");
        System.out.println(user);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("loginWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}