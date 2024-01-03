package app.planmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private TextField userPassedEmail;

    @FXML
    private TextField userPassedPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginButton.setOnAction(e -> loginUserToApp());
    }
    @FXML
    private void loginUserToApp() {
        String email = userPassedEmail.getText();
        String password = userPassedPassword.getText();

        System.out.println(email + " " + password);
    }


}
