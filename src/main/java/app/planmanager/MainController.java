package app.planmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    private Button addPlanButton;

    @FXML
    private Button deletePlanButton;

    @FXML
    private Button editPlanButton;

    @FXML
    private TableColumn<?, ?> hour;

    @FXML
    private MenuButton listOfMajors;

    @FXML
    private Button loginButton;

    @FXML
    private TableColumn<?, ?> numberOfLesson;

    @FXML
    private TableView<?> schedule;

    @FXML
    private Label userData;

    @FXML
    private TableColumn<?, ?> monday;

    @FXML
    private TableColumn<?, ?> tuesday;

    @FXML
    private TableColumn<?, ?> wednesday;

    @FXML
    private TableColumn<?, ?> thursday;

    @FXML
    private TableColumn<?, ?> friday;

    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(e -> onLoginButtonClick());
    }

    @FXML
    private void onLoginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginWindow.fxml"));
            Parent root = loader.load();

            // Pobierz kontroler zalogowanego okna
            LoginController loginController = loader.getController();

            // Przekaż referencję do MainController do LoginController
            loginController.setMainController(this);

            // ... (inne operacje przed otwarciem okna logowania)

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metoda do ustawiania danych użytkownika
    public void setUserData(User user) {
        this.currentUser = user;
        // Aktualizuj etykietę z danymi użytkownika
        userData.setText("Zalogowany jako: \n" + user.getName_() + " " + user.getSurname_());
    }
}
