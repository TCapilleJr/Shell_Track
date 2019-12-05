package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Follows same set up as Client Controller etc.
 * Load list, user can add and dlete from list.
 */
public class HarvestLocationController extends Controller implements Initializable {
    private Stage dialogStage;
    @FXML
    private TextField newHarvestLocation;
    @FXML
    private ListView harvestLocationList;

    private final String QUERY = "SELECT * FROM main.harvest_locations";

    public ObservableList<String> getHarvestLocations() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("harvest_locations"));
        }
        return data;
    }

    public void addHarvestLocation(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (newHarvestLocation.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, null, "Form Error!",
                    "Textfield is blank");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.addToHarvestLocations(newHarvestLocation.getText());
            ObservableList<String> harvestNumbers = getHarvestLocations();
            harvestLocationList.setItems(harvestNumbers);
        }
        newHarvestLocation.clear();
    }

    @FXML
    public void cancelAdd(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    public void deleteHarvestLocation(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (harvestLocationList.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, null, "",
                    "No Location Type Selected");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.deleteHarvestLocations(harvestLocationList.getSelectionModel().getSelectedItem().toString());
            harvestLocationList.setItems(getHarvestLocations());
        }
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<String> data = getHarvestLocations();
            harvestLocationList.setItems(data);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
