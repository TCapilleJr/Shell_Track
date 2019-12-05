package shell_track.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.model.Transaction;
import shell_track.model.User;

/**
 * Displays all information of transaction in pop-up scene
 */
public class MoreInfoUserController extends Controller {
    @FXML
    private Label userName;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label address;
    @FXML
    private Label access;
    @FXML
    private Label email;


    private Stage dialogStage;
    private User user;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getUserName());
        firstName.setText(user.getUserFirstName());
        lastName.setText(user.getUserLastName());
        address.setText(user.getUserAddress());
        access.setText(user.getUserAccess());
        email.setText(user.getUserEmail());

    }

    @FXML
    public void back(ActionEvent event) {
        dialogStage.close();
    }

}