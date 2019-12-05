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
public class HarvesterNumberController extends Controller implements Initializable {
    private Stage dialogStage;
    @FXML
    private TextField newHarvesterNumber;
    @FXML
    private ListView harvesterNumberList;

    private final String QUERY = "SELECT * FROM main.original_harvest_number";

    public ObservableList<String> getHarvestNumberData() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("original_harvester_number"));
        }
        return data;
    }

    public void addHarvesterNumber(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (newHarvesterNumber.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, null, "Form Error!",
                    "Textfield is blank");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.addToOriginalHarvestNumber(newHarvesterNumber.getText());
            ObservableList<String> harvestNumbers = getHarvestNumberData();
            harvesterNumberList.setItems(harvestNumbers);
            newHarvesterNumber.clear();
        }
    }

    @FXML
    public void cancelAdd(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    public void deleteHarvesterNumber(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (harvesterNumberList.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, null, "",
                    "No Product Type Selected");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.deleteOriginalHarvestNumber(harvesterNumberList.getSelectionModel().getSelectedItem().toString());
            ObservableList<String> productTypes = getHarvestNumberData();
            harvesterNumberList.setItems(productTypes);
        }
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<String> data = getHarvestNumberData();
            harvesterNumberList.setItems(data);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
