package shell_track.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import shell_track.database.DBUtil;
import shell_track.model.User;

import java.io.IOException;
import java.sql.SQLException;

public class AddUserController extends Controller {
    /**
     * Textfield for new user
     */
    @FXML
    private TextField addUserName;
    /**
     * Password field for new user
     */
    @FXML
    private PasswordField addUserPw;
    /**
     * admin checkbox
     */
    @FXML
    private CheckBox adminCheck;
    /**
     * employee checkbox
     */
    @FXML
    private CheckBox employeeCheck;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField email;


    private Stage dialogStage;

    private User user;

    private boolean successfulInsert;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuccessfulInsert() {
        return successfulInsert = false;
    }


    /**
     * Establishes a connection to the login database. Retrieves the strings that correspond to
     * addUserEmail and addUserPw. Checks to make sure selection is valid by checking for empty string cases
     * and checkboxes are correctly selected.
     *
     * @param event the current event
     * @throws IOException
     */
    public void addUser(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        DBUtil dbUtil = new DBUtil();
        if (isCorrectSelection()) {
            String user_name = addUserName.getText();
            String password = addUserPw.getText();
            String first_name = firstName.getText();
            String last_name = lastName.getText();
            String address_string = address.getText();
            String email_string = email.getText();
            if (adminCheck.isSelected()) {
                //will only switch screen if insertion is accepted returns false IF it is a successful entry
                user = new User(user_name, password, "admin", first_name, last_name, address_string, email_string);
                if (dbUtil.addUserToUser(user)) {
                    dbUtil.addUserToLogin(user);
                    successfulInsert = true;
                    dialogStage.close();
                }
            } else {
                user = new User(user_name, password, "employee", first_name, last_name, address_string, email_string);
                if (dbUtil.addUserToUser(user)) {
                    dbUtil.addUserToLogin(user);
                    successfulInsert = true;
                    dialogStage.close();
                }
            }
        } else {
            infoBox("Not a proper input", "Invalid Selection", "Alert");
        }
    }


    /**
     * Checks to make sure texts fields are filled and one checkbox is selected
     *
     * @return true is selection is correct
     */
    private boolean isCorrectSelection() {
        if (adminCheck.isSelected() && employeeCheck.isSelected() || (!adminCheck.isSelected() && !employeeCheck.isSelected())) {
            return false;
        } else if (addUserName.getText().isEmpty() || addUserPw.getText().isEmpty()) {
            return false;
        } else if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || address.getText().isEmpty() || email.getText().isEmpty() || email.getText().indexOf('@') == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Reloads the AddUser FXML to refresh the table view reflecting changes to user database
     *
     * @param event
     * @throws IOException
     */
    public void cancel(ActionEvent event) throws IOException {
        dialogStage.close();
    }

    /**
     * Sets the dialog stage for Add/Edit user
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
