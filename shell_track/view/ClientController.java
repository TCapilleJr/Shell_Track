package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.Client;
import shell_track.model.User;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class controls the client list that corresponds to the available inputs of the combobox
 * for clients in Input Transaction.  The list is serialized and reloaded everytime the page is accessed
 */
public class ClientController extends Controller implements Serializable, Initializable {
    //This stage is set inside Input Transaction fxml
    private Stage dialogStage;
    //Textfield of lists
    @FXML
    private TextField newClient;
    @FXML
    private TextField address;
    @FXML
    private TextField contactInfo;

    @FXML
    private ListView clientList;

    private final String QUERY = "SELECT * FROM main.clients";


    public ObservableList<Client> getClients() throws SQLException, ClassNotFoundException {

        ObservableList<Client> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(new Client(resultSet.getString("client_name"), resultSet.getString("address"), resultSet.getString("contact_info")));
        }
        return data;
    }

    public ObservableList<String> getClientNames() throws SQLException, ClassNotFoundException {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (Client c : getClients()) {
            names.add(c.getClientName());
        }
        return names;
    }

    /**
     * Adds new client to list, checks to make sure the textfield is not empty
     *
     * @param event
     */
    public void addClient(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (newClient.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, null, "Form Error!",
                    "Textfield is blank");
        } else {
            Client new_client = new Client(newClient.getText(), address.getText(), contactInfo.getText());
            DBUtil dbUtil = new DBUtil();
            dbUtil.addToClients(new_client);
            clientList.setItems(getClientNames());
            clearFields();
        }
    }
    public void clearFields(){
        contactInfo.clear();
        address.clear();
        newClient.clear();
    }

    /**
     * Return to Input Transaction scene
     *
     * @param event
     */
    @FXML
    public void cancelAdd(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    /**
     * Delete client from client list.  Checks to for mulls then, resets items in clientList
     *
     * @param event
     */
    @FXML
    public void deleteClient(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (clientList.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, null, "",
                    "No Client Selected");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.deleteClients(clientList.getSelectionModel().getSelectedItem().toString());
            clientList.setItems(getClientNames());
        }
    }

    public void editClient(ActionEvent event) throws SQLException, ClassNotFoundException {
        DBUtil dbUtil = new DBUtil();
        Client selectedClient = dbUtil.selectClient(clientList.getSelectionModel().getSelectedItem().toString());
        if (selectedClient != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_client.fxml"));

            try {
                Parent root = (Parent) loader.load();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("Edit User");
                dialogStage.initModality(Modality.APPLICATION_MODAL);

                // Set the person into the controller, i.e. show person details on the edit window
                EditClientContoller controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setClient(selectedClient);
                dialogStage.showAndWait();
                clientList.setItems(getClientNames());


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            infoBox("No transaction selected", "Selection Error", "Alert");
        }
    }

    /**
     * Set diaglog stage in Input Transaction
     *
     * @param stage
     */
    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    /**
     * Actions performed upon FXML load
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clientList.setItems(getClientNames());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
