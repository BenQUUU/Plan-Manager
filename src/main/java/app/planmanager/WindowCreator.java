package app.planmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowCreator {
    public static void createNewWindow(String filename, String title){
        try {
            FXMLLoader loader = new FXMLLoader(WindowCreator.class.getResource(filename));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));

            stage.initModality(Modality.WINDOW_MODAL);

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
