package shell_track.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditUserController extends Controller implements Initializable {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> userName;
    @FXML
    private TableColumn<User, String> userAccess;
    @FXML
    private Button addNewUser;
    @FXML
    private Button editUser;
    @FXML
    private Button deleteUser;


    private final String USER_QUERY = "SELECT * from main.users";

    private final String USER_QUERY_LOGIN = "SELECT * from main.login where user_name = ?";

    /**
     * Populates User Tables by creating Observable List, list is called in the initialize method of this class
     *
     * @return list of users, except the user who is currently signed in
     */
    private ObservableList<User> getUserData() {
        ObservableList<User> userData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(USER_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //Does not add the user that is currently signed in
                PreparedStatement preparedStatement2 = connection.prepareStatement(USER_QUERY_LOGIN);
                preparedStatement2.setString(1, resultSet.getString("user_name"));
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                String password = "";
                String access = "";
                if (resultSet2.next()) {
                    password = resultSet2.getString("password");
                    access = resultSet2.getString("user_access");
                }

                if (!resultSet.getString("user_name").equals(DBUtil.signedInUser)) {
                    userData.add(new User(resultSet.getString("user_name"), password, access, resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("address"), resultSet.getString("email")));
                }
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userData;
    }

    /**
     * Displayes add/edit user FXML and sets the userTable accordingly after user's input
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void addNewUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_user.fxml"));
        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            AddUserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            userTable.setItems(getUserData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to make sure user is selected, then deletes the selected user from the database
     *
     * @param event
     */
    @FXML
    private void deleteUser(ActionEvent event) {
        int selected = userTable.getSelectionModel().getSelectedIndex();
        if (selected >= 0) {
            User selectedPerson = userTable.getSelectionModel().getSelectedItem();
            userTable.getItems().remove(selected);
            try {
                DBUtil dbUtil = new DBUtil();
                dbUtil.deleteUser(selectedPerson.getUserName());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            infoBox("Delete Unsuccessful", "Error", "Alert");
        }
    }

    /**
     * Grabs selected users and changes there access from admin to employee or employee to admin.
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void editUserAccess(ActionEvent event) throws SQLException {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_user_scene.fxml"));

            try {
                Parent root = (Parent) loader.load();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("Edit User");
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                // Set the person into the controller, i.e. show person details on the edit window
                EditUserDialogController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setUser(selectedUser);
                dialogStage.showAndWait();


            } catch (IOException e) {
            }
        } else {
            infoBox("No transaction selected", "Selection Error", "Alert");
        }
    }

    @FXML
    public void moreInfo(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("more_info_user.fxml"));

            try {
                Parent root = (Parent) loader.load();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("User Information");
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                // Set the person into the controller, i.e. show person details on the edit window
                MoreInfoUserController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setUser(selectedUser);
                dialogStage.showAndWait();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Sets items of User Tables
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        userAccess.setCellValueFactory(new PropertyValueFactory<>("userAccess"));

        ObservableList<User> data = getUserData();

        userTable.getItems().addAll(data);
    }
}
