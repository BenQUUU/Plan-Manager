package app.planmanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static app.planmanager.WindowCreator.createNewWindow;

public class LoginController extends MainApp implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private TextField userPassedEmail;

    @FXML
    private TextField userPassedPassword;

    @FXML
    private Button registerButton;

    @FXML
    private Label loginInfo;

    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(e -> loginUserToApp());
        registerButton.setOnAction(e -> createNewWindow("registerWindow.fxml", "Plan lekcji"));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void loginUserToApp() {
        DBConnector dbConnection = new DBConnector();
        Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));

        String email = userPassedEmail.getText();
        String password = userPassedPassword.getText();

        DBFunctions dbFunctions = new DBFunctions();
        User user = dbFunctions.checkEmailAndPasswordValidity(connection, email, password);
        if (user != null) {
            loginInfo.setText("Logowanie powiodło się");
            // Ustawienie danych użytkownika w MainController po zalogowaniu
            mainController.setUserData(user);

            // Zamknięcie okna logowania
            ((Stage) userPassedEmail.getScene().getWindow()).close();
        } else {
            loginInfo.setText("Niepoprawne dane logowania");
        }

        dbConnection.closeDatabase(connection);
    }
}
