package shell_track.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.database.DBUtil;

import java.sql.SQLException;


public class ForgotPasswordController extends Controller {
    @FXML
    private TextField userName;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField repeatNewPassword;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean validate() {
        boolean valid = true;
        if (userName.getText().isEmpty() || newPassword.getText().isEmpty() || repeatNewPassword.getText().isEmpty()) {
            valid = false;
            infoBox("Invalid Input", "A field is empty", "Alerts");
        } else if (!newPassword.getText().equals(repeatNewPassword.getText())) {
            valid = false;
            infoBox("Passwords do not match", "Password Entry", "Alert");
        }
        return valid;
    }


    @FXML
    public void cancel(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    public void submit(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validate()) {
            return;
        }
        DBUtil dbUtil = new DBUtil();
        dbUtil.passwordChange(userName.getText(),newPassword.getText());
        dialogStage.close();
    }

}
