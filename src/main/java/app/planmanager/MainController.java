package app.planmanager;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private MenuButton listOfMajors;

    @FXML
    private Button loginButton;

    @FXML
    private TableView<Lesson> schedule;

    @FXML
    private Label userData;

    @FXML
    private TableColumn<Lesson, String> classroom;

    @FXML
    private TableColumn<Lesson, String> hour;

    @FXML
    private TableColumn<Lesson, Integer> numberOfLesson;

    @FXML
    private Button prevButton;

    @FXML
    private TableColumn<Lesson, String> subject;

    @FXML
    private TableColumn<Lesson, String> teacher;

    @FXML
    private Button nextButton;

    @FXML
    private ListView<String> dayListView;

    private ObservableList<String> daysOfWeek;

    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(e -> onLoginButtonClick());

        daysOfWeek = FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        dayListView.setItems(daysOfWeek);

        // Ustaw domyślny dzień
        dayListView.getSelectionModel().selectFirst();

        // Obsługa przycisków
        prevButton.setOnAction(event -> scroll(-1));
        nextButton.setOnAction(event -> scroll(1));
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
        userData.setText("Zalogowany jako: " + user.getName_() + " " + user.getSurname_());
    }

    private void scroll(int direction) {
        int currentIndex = dayListView.getSelectionModel().getSelectedIndex();
        int newIndex = currentIndex + direction;

        if (newIndex >= 0 && newIndex < daysOfWeek.size()) {
            dayListView.getSelectionModel().select(newIndex);
        }
    }
}
