package app.planmanager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import static app.planmanager.WindowCreator.createNewWindow;


public class MainController implements Initializable {

    @FXML
    private Button addPlanButton;

    @FXML
    private Button deletePlanButton;

    @FXML
    private Button editPlanButton;

    @FXML
    private ComboBox<String> listOfMajors;

    private ObservableList<String> classes;

    @FXML
    private Button loginButton;

    @FXML
    private TableView<Lesson> schedule;

    @FXML
    private Label userData;

    @FXML
    private TableColumn<Lesson, String> classroom = new TableColumn<>("KLASA");

    @FXML
    private TableColumn<Lesson, String> hour = new TableColumn<>("GODZINA");

    @FXML
    private TableColumn<Lesson, Integer> numberOfLesson = new TableColumn<>("NR");

    @FXML
    private Button prevButton;

    @FXML
    private TableColumn<Lesson, String> subject = new TableColumn<>("PRZEDMIOT");

    @FXML
    private TableColumn<Lesson, String> teacher = new TableColumn<>("NAUCZYCIEL");

    @FXML
    private Button nextButton;

    @FXML
    private ListView<String> dayListView;

    @FXML
    private final ObservableList<String> daysOfWeek = FXCollections.observableArrayList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");;

    private User currentUser;

    private ObservableList<Lesson> currentDayLessons;
    private String currentDay;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnector dbConnection = new DBConnector();
        Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));
        DBFunctions dbFunctions = new DBFunctions();

        dayListView.setItems(daysOfWeek);
        dayListView.getSelectionModel().selectFirst();

        numberOfLesson.setCellValueFactory(new PropertyValueFactory<>("lessonNumber"));
        hour.setCellValueFactory(new PropertyValueFactory<>("hour"));
        classroom.setCellValueFactory(new PropertyValueFactory<>("classroom"));
        subject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("subjectTeacherInitials"));

        currentDayLessons = FXCollections.observableArrayList(dbFunctions.getAllPlanInformation(connection, "Monday"));

        for (Lesson l : currentDayLessons) {
            l.setHour();
        }

        schedule.getColumns().addAll(numberOfLesson, hour, classroom, subject, teacher);
        schedule.setItems(currentDayLessons);

        classes = FXCollections.observableArrayList(dbFunctions.getAllTablesName(connection));
        listOfMajors = new ComboBox<>(classes);
        listOfMajors.setPromptText("Kierunki");
        // Dodaj listener do ComboBoxa
        listOfMajors.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Tutaj dodaj kod do aktualizacji planu lekcji dla wybranej klasy
                updatePlan(dbFunctions, connection);
            }
        });

        // Obsługa przycisków
        prevButton.setOnAction(event -> scroll(-1));
        nextButton.setOnAction(event -> scroll(1));
        loginButton.setOnAction(e -> onLoginButtonClick());
        addPlanButton.setOnAction(e -> createNewWindow("addPlanWindow.fxml", "Nowy plan"));
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
    @FXML
    private void updatePlan(DBFunctions dbFunctions, Connection connection){
        dayListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentDay = newValue;
                currentDayLessons = FXCollections.observableArrayList(dbFunctions.getAllPlanInformation(connection, currentDay));
                for (Lesson l : currentDayLessons) {
                    l.setHour();
                }
                schedule.setItems(currentDayLessons);
            }
        });
    }
}
