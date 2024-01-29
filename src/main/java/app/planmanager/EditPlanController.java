package app.planmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class EditPlanController implements Initializable {
    @FXML
    private TextField classroom;

    @FXML
    private ComboBox<String> dayOfWeek;

    @FXML
    private Label infoLabel;

    @FXML
    private ComboBox<Integer> lessonNumber;

    @FXML
    private ComboBox<String> listOfMajors;

    @FXML
    private ComboBox<String> subjects;

    @FXML
    private Button addButton;

    private ObservableList<String> classes;

    private ObservableList<String> daysOfWeek;

    private ObservableList<String> listOfSubjects;

    private final ObservableList<Integer> lessonNumbers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBConnector dbConnection = new DBConnector();
        Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));
        DBFunctions dbFunctions = new DBFunctions();

        listOfSubjects = FXCollections.observableArrayList(dbFunctions.getAllSubjectsFromDB(connection));

        lessonNumber.setItems(lessonNumbers);
        subjects.setItems(listOfSubjects);

        addButton.setOnAction(e -> editPlan());
    }

    public void setClasses(ObservableList<String> classes) {
        this.classes = classes;
        listOfMajors.setItems(classes);
    }

    public void setDaysOfWeek(ObservableList<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        dayOfWeek.setItems(daysOfWeek);
    }

    @FXML
    private void editPlan(){
        try{
            DBConnector dbConnection = new DBConnector();
            Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));
            DBFunctions dbFunctions = new DBFunctions();

            int classroomFromUser = Integer.parseInt(classroom.getText());
            int selectedLessonNumber = lessonNumber.getValue();
            String selectedDay = dayOfWeek.getValue();
            String selectedMajor = listOfMajors.getValue();
            String selectedSubject = subjects.getValue();

            EditPlanContainer planContainer = new EditPlanContainer(selectedMajor, selectedDay, selectedLessonNumber, selectedSubject , classroomFromUser);

            if (dbFunctions.editPlan(connection, planContainer)) {
                infoLabel.setTextFill(Color.GREEN);
                infoLabel.setText("Pomyślnie dodano przedmiot do planu");
            } else {
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Błąd podczas dodawania do bazy");
            }

            dbConnection.closeDatabase(connection);
        }catch(NumberFormatException e){
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText("Niewłaściwy numer klasy");
        }
    }
}

