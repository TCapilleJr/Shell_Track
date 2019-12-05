package shell_track.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract controller to reusing switching screen functions and alert and info box functions.
 * Also includes serialization and deserialization methods for lists.
 */
public abstract class Controller {
    /**
     * Controller info box
     *
     * @param infoMessage - message to be sent to user
     * @param headerText  - Header of info box
     * @param title       - Box title
     */

    private static Stage primaryStage;

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    /**
     * Controller alerts
     *
     * @param alertType
     * @param owner
     * @param title
     * @param message
     */
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    /**
     * General method to switch to next scene
     *
     * @param fxml - fxml to switch to
     * @throws IOException
     */
    public void switchScreen(String fxml, ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(scene);
        stage.show();

    }

}
