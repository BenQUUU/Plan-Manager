package app.planmanager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private TextField email;

    @FXML
    private TextField email2;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField password2;

    @FXML
    private Button registerButton;

    @FXML
    private TextField surname;

    @FXML
    private Label infoLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setOnAction(e -> createNewUser());
        registerButton.setDisable(true);

        // Dodanie nasłuchiwanie zmian w polach tekstowych
        email.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        email2.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        name.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        surname.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        password.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        password2.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private void checkFields() {
        // Sprawdzamy, czy wszystkie pola tekstowe nie są puste
        boolean fieldsNotEmpty = !email.getText().trim().isEmpty() &&
                !email2.getText().trim().isEmpty() &&
                !name.getText().trim().isEmpty() &&
                !surname.getText().trim().isEmpty() &&
                !password.getText().trim().isEmpty() &&
                !password2.getText().trim().isEmpty();

        // Odblokowujemy przycisk rejestracji, jeśli wszystkie pola są wypełnione
        registerButton.setDisable(!fieldsNotEmpty);
    }

    @FXML
    private void createNewUser(){



        try{
            String userEmail = email.getText();
            String userPassword = password.getText();
            String userName = name.getText();
            String userSurname = surname.getText();

            if((!Objects.equals(userEmail, email2.getText())) || (!Objects.equals(userPassword, password2.getText()))){
                throw new InputMismatchException("Emaile bądź hasła różnią się od siebie!");
            } else {

                DBConnector dbConnector = new DBConnector();
                Connection connection = dbConnector.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));

                try{
                    DBFunctions dbFunctions = new DBFunctions();
                    User user = new User(userName,userSurname,userEmail,userPassword, Group.user);
                    dbFunctions.registerUser(connection, user);
                }catch (Exception e){
                    System.out.println("Error: " + e);
                }

                infoLabel.setTextFill(Color.GREEN);
                infoLabel.setText("Pomyślnie stworzono użytkownika");

                dbConnector.closeDatabase(connection);
            }
        } catch (InputMismatchException e){
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText(e.getMessage());
        }
    }
}
