package shell_track.view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;

public class LoginController extends Controller {
    /**
     * ID field for TextField login_menu
     */
    @FXML
    private TextField emailIdField;
    /**
     * Password field for login_menu
     */
    @FXML
    private PasswordField passwordField;
    /**
     * Submit button login_menu
     */
    @FXML
    private Button submitButton;
    /**
     * Submit button for add_user
     */
    @FXML
    private Button addSubmitButton;
    /**
     * Cancel Button fo add_user
     */
    @FXML
    private Button addUserCancelButton;
    /**
     * Checkbox for add_user
     */
    @FXML
    private CheckBox adminCheck;
    /**
     * CheckBox for add_user
     */
    @FXML
    private CheckBox employeeCheck;
    /**
     * Textfield for add_user
     */
    @FXML
    private TextField addUser;
    /**
     * Password field add_user
     */
    @FXML
    private PasswordField addPw;

    /**
     * Checks for empty fields, if no null is found, validate entry and check access level
     *
     * @param event not used
     * @throws SQLException
     */
    public void login(ActionEvent event) throws SQLException, ClassNotFoundException {

        Window owner = submitButton.getScene().getWindow();

        System.out.println(emailIdField.getText());
        System.out.println(passwordField.getText());

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        String userName = emailIdField.getText();
        String password = passwordField.getText();

        DBUtil dbUtil = new DBUtil();
        boolean flag = dbUtil.validate(userName, password);
        if (!flag) {
            infoBox("Please enter correct Email and Password", null, "Failed");
        } else {
            infoBox("Login Successful!", null, "Success");
            boolean access = dbUtil.getUserAccess();
            if (access) {
                try {
                    switchScreen("employee_menu.fxml",event);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            } else {
                try {
                    switchScreen("admin_menu.fxml",event);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }
    @FXML
    public void forgotPassword(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("password_reset.fxml"));
        Parent root = (Parent) loader.load();
        Stage dialogStage = new Stage();
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        dialogStage.setTitle("Forgot Password");
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        ForgotPasswordController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        dialogStage.showAndWait();
    }
}