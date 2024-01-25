package app.planmanager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class AddPlanController implements Initializable {
    @FXML
    private Button addButton;
    @FXML
    private TextField planName;
    @FXML
    private Label infoLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(e -> createNewPlan());
    }

    @FXML
    private void createNewPlan(){
        String newPlanName = planName.getText();

        try{
            DBConnector dbConnection = new DBConnector();
            Connection connection = dbConnection.connectToDatabase(System.getenv("DBName"), System.getenv("DBUsername"), System.getenv("DBPassword"));
            DBFunctions dbFunctions = new DBFunctions();

            //@TODO miejsce na funkcje (wywalić potem)!
            if(dbFunctions.addNewPlanToDB(connection, newPlanName)){
                infoLabel.setTextFill(Color.GREEN);
                infoLabel.setText("Pomyślnie dodano plan do bazy");
            }else{
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Nazwa tabeli już istnieje");
            }

            dbConnection.closeDatabase(connection);
        }catch (Exception e){
            infoLabel.setTextFill(Color.RED);
            infoLabel.setText("error");
        }

    }
}
