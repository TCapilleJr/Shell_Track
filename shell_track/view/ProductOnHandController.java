package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.model.Transaction;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controls scene to display product on hand upon selecting outgoing transaction
 */
public class ProductOnHandController extends Controller implements Initializable {
    private Stage dialogStage;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> productType;
    @FXML
    private TableColumn<Transaction, String> harvestLocation;
    @FXML
    private TableColumn<Transaction, String> harvestDate;
    @FXML
    private TableColumn<Transaction, String> productOnHand;

    private boolean cancel;

    private Transaction selectedTransaction;
    //SQL Query to retrieve all entries with product on hand > 0
    private final String PRODUCT_ON_HAND = "SELECT * FROM main.incoming_transaction WHERE product_on_hand > 0";


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Transaction getSelectedTransaction() {
        return selectedTransaction;
    }

    @FXML
    public void cancel(ActionEvent event) {
        dialogStage.close();
        cancel = true;
    }

    public boolean getCancel() {
        return cancel;
    }

    @FXML
    public void select(ActionEvent event) {
        selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        dialogStage.close();
    }

    /**
     * Sets list from desired entries
     *
     * @return
     */
    private ObservableList<Transaction> getTransactionData() {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(PRODUCT_ON_HAND);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactionData.add(new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), resultSet.getString("original_harvest_number"), resultSet.getString("type_of_shellfish"),
                        resultSet.getInt("recieved_units"), resultSet.getInt("count_in_units"), resultSet.getString("harvest_date"), resultSet.getString("harvest_location"),
                        resultSet.getInt("temperature"), null, null, null, resultSet.getString("received_date"), resultSet.getInt("product_on_hand")));
            }
            for (Transaction t : transactionData) {
                System.out.println(t.getId());
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return transactionData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        harvestDate.setCellValueFactory(new PropertyValueFactory<>("harvestDate"));
        productType.setCellValueFactory(new PropertyValueFactory<>("typeOfShellfish"));
        harvestLocation.setCellValueFactory(new PropertyValueFactory<>("harvestLocation"));
        productOnHand.setCellValueFactory(new PropertyValueFactory<>("productOnHand"));

        ObservableList<Transaction> data = getTransactionData();
        transactionTable.getItems().setAll(data);
    }
}
