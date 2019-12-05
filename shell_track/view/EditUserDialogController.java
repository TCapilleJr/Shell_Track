package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import shell_track.database.DBUtil;
import shell_track.model.User;

import javax.swing.text.TableView;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditUserDialogController extends Controller implements Initializable {
    @FXML
    private TextField userName;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField address;
    @FXML
    private TextField email;
    @FXML
    private ComboBox<String> access;

    private static ObservableList<String> accessField = FXCollections.observableArrayList();

    private Stage dialogStage;

    private User user;

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User user) {
        this.user = user;
        userName.setText(user.getUserName());
        firstName.setText(user.getUserFirstName());
        lastName.setText(user.getUserLastName());
        address.setText(user.getUserAddress());
        email.setText(user.getUserEmail());
        access.getSelectionModel().select(user.getUserAccess());
    }

    public void handleOk(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (isValidInput()) {
            String key = user.getUserName();

            user.setUserName(userName.getText());
            user.setUserFirstName(firstName.getText());
            user.setUserLastName(lastName.getText());
            user.setUserAddress(address.getText());
            user.setUserEmail(email.getText());
            user.setUserAccess(access.getSelectionModel().getSelectedItem());
            DBUtil dbUtil = new DBUtil();
            dbUtil.editUser(user, key);
            infoBox("User Edited", " Edit User", "Alert");
        } else {
            infoBox("Invalid Input", " Edit User", "Alert");
        }
        dialogStage.close();

    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    public boolean isValidInput() {
        boolean valid = true;
        if (userName.getText().isEmpty() || firstName.getText().isEmpty()) {
            valid = false;
        } else if (lastName.getText().isEmpty() || address.getText().isEmpty()) {
            valid = false;
        } else if (email.getText().isEmpty() || email.getText().indexOf('@') == -1) {
            valid = false;
        } else if (access.getSelectionModel().getSelectedItem().isEmpty()) {
            valid = false;
        }
        return valid;
    }
    public void initialize(URL location, ResourceBundle resources) {
        String[] fields_for_access = {"admin", "employee"};
        accessField.setAll(fields_for_access);
        access.setItems(accessField);
    }
}
