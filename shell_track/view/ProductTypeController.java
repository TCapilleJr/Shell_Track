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
import shell_track.model.Transaction;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Allows for editing of Product Type List inside Input Transaction
 */
public class ProductTypeController extends Controller implements Initializable {
    private Stage dialogStage;
    @FXML
    private TextField newProductType;
    @FXML
    private ListView productList;

    private final String TYPE_QUERY = "SELECT * FROM main.type_of_product";

    public void clearFields(){
        newProductType.clear();
    }

    public ObservableList<String> getProductTypeData() throws SQLException, ClassNotFoundException {

        ObservableList<String> productTypeData = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(TYPE_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            productTypeData.add(resultSet.getString("type"));
        }
        return productTypeData;
    }

    /**
     * add product type to list
     *
     * @param event
     */
    public void addProductType(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (newProductType.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, null, "Form Error!",
                    "Textfield is blank");
        } else {

            DBUtil dbUtil = new DBUtil();
            dbUtil.addToTypeOfProduct(newProductType.getText());
            ObservableList<String> productTypes = getProductTypeData();
            productList.setItems(productTypes);
        }
        clearFields();
    }

    @FXML
    public void cancelAdd(ActionEvent event) {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    /**
     * delete product type and set new list
     *
     * @param event
     */
    @FXML
    public void deleteProductType(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (productList.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, null, "",
                    "No Product Type Selected");
        } else {
            DBUtil dbUtil = new DBUtil();
            dbUtil.deleteTypeOfProduct(productList.getSelectionModel().getSelectedItem().toString());
            ObservableList<String> productTypes = getProductTypeData();
            productList.setItems(productTypes);
        }
    }

    public void setDialogStage(Stage stage) {
        dialogStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ObservableList<String> productTypes = getProductTypeData();
            productList.setItems(productTypes);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
