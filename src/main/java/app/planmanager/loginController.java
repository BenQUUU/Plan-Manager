package app.planmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class loginController extends MainApp implements Initializable {

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

        DBConnector dbConnection = new DBConnector();
        Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));

        String email = userPassedEmail.getText();
        String password = userPassedPassword.getText();

        DBFunctions dbFunctions = new DBFunctions();

        User user = dbFunctions.getUserEmailAndPassword(connection, email, password);

        if(user != null){
            //login, turn on new window
            ArrayList<Lesson> lessonArrayList = dbFunctions.getAllPlanInformation(connection);
            System.out.println(lessonArrayList.size());
            for(Lesson element : lessonArrayList){
                System.out.println(element);
            }
            //openNewWindow();
        }else{
            //error during login
            displayAlertIfErrorOccurredDuringLogin();
        }

        System.out.println(user);

        dbConnection.closeDatabase(connection);

        System.out.println(email + " " + password);
    }

    public void displayAlertIfErrorOccurredDuringLogin(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error during login");
        alert.setContentText("Wrong email or password");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            System.out.println("Closed");
        } else if (result.get() == ButtonType.OK) {
            System.out.println("ok");
        } else if (result.get() == ButtonType.CANCEL) {
            System.out.println("never");
        }
    }
    public void openNewWindow(){
        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nowe Okno");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.WINDOW_MODAL);

            stage.initOwner(loginButton.getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
