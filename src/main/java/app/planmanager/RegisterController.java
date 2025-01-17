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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        boolean fieldsNotEmpty = isFieldsEmpty();

        // Odblokowujemy przycisk rejestracji, jeśli wszystkie pola są wypełnione
        registerButton.setDisable(!fieldsNotEmpty);
    }

    private boolean isFieldsEmpty() {
        return !email.getText().trim().isEmpty() &&
                !email2.getText().trim().isEmpty() &&
                !name.getText().trim().isEmpty() &&
                !surname.getText().trim().isEmpty() &&
                !password.getText().trim().isEmpty() &&
                !password2.getText().trim().isEmpty();
    }

    private boolean checkUserPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            return true;
        } else {
            Pattern pattern = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]");
            Matcher matcher = pattern.matcher(password);
            return !matcher.find();
        }
    }

    @FXML
    private void createNewUser() {
        try {
            String userEmail = email.getText();
            String userPassword = password.getText();
            String userName = name.getText();
            String userSurname = surname.getText();

            if ((!Objects.equals(userEmail, email2.getText())) || (!Objects.equals(userPassword, password2.getText()))) {
                throw new InputMismatchException("Emaile bądź hasła różnią się od siebie!");
            } else if (checkUserPassword(userPassword)) {
                throw new InputMismatchException("Brak znaków specjalnych lub zła długość hasła!");
            } else {

                DBConnector dbConnector = new DBConnector();
                Connection connection = dbConnector.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));

                try {
                    DBFunctions dbFunctions = new DBFunctions();
                    User user = new User(userName, userSurname, userEmail, userPassword, Group.user);
                    if(!dbFunctions.registerUser(connection, user)){
                        infoLabel.setTextFill(Color.RED);
                        infoLabel.setText("Użytkownik już istnieje");
                    }else{
                        infoLabel.setTextFill(Color.GREEN);
                        infoLabel.setText("Pomyślnie stworzono użytkownika");
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                dbConnector.closeDatabase(connection);
            }
        } catch (InputMismatchException e) {
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText(e.getMessage());
        }
    }
}
