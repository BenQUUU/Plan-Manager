package app.planmanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

