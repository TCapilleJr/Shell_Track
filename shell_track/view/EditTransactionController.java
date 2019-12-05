package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.Transaction;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Edit Transaction allows for the editing of all features in database except for entered by and timestamp
 */
public class EditTransactionController extends Controller implements Initializable, Serializable {
    /**
     * FXML
     */
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> date;
    @FXML
    private TableColumn<Transaction, String> soldReceived;
    @FXML
    private TableColumn<Transaction, String> productType;
    @FXML
    private TableColumn<Transaction, String> clientName;
    @FXML
    private TableColumn<Transaction, Integer> id;

    //List of transaction data
    private ObservableList<Transaction> incomingTransactionData;
    private ObservableList<Transaction> outgoingTransactionData;

    // SQL Query for all transactions in database
    private final String INCOMING_TRANSACTION_QUERY = "SELECT * from main.incoming_transaction";
    private final String OUTGOING_TRANSACTION_QUERY = "SELECT * from main.outgoing_transactions";

    /**
     * Connects to the database and reads all transaction inside shell_track.transaction.  Adds them to Observable list
     * in order to be displayed in tableview.
     * @return
     */
    public ObservableList<Transaction> getIncomingTransactionData() {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INCOMING_TRANSACTION_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            DBUtil dbUtil = new DBUtil();
            while (resultSet.next()) {
                String[] entered_byAndTimestamp = dbUtil.getTimeEnteredAndTimeStamp(resultSet.getInt("ID"));
                transactionData.add(new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), resultSet.getString("original_harvest_number"), resultSet.getString("type_of_shellfish"),
                        resultSet.getInt("recieved_units"), resultSet.getInt("count_in_units"), resultSet.getString("harvest_date"), resultSet.getString("harvest_location"),
                        resultSet.getInt("temperature"), "received", entered_byAndTimestamp[0], entered_byAndTimestamp[1], resultSet.getString("received_date"),resultSet.getInt("product_on_hand")));
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return transactionData;
    }
    public ObservableList<Transaction> getOutgoingTransactionData() {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(OUTGOING_TRANSACTION_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            DBUtil dbUtil = new DBUtil();
            // In the case of Outgoing Transaction product on hand is replaced by ID_received
            while (resultSet.next()) {
                String[] entered_byAndTimestamp = dbUtil.getTimeEnteredAndTimeStamp(resultSet.getInt("ID"));
                String[] incomingData = dbUtil.getHarvestLocationAndHarvestNumber(resultSet.getInt("ID_recieved"));
                transactionData.add(new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), incomingData[1], resultSet.getString("type_of_shellfish"),
                        resultSet.getInt("units_sold") , Integer.parseInt(incomingData[3]), incomingData[2], incomingData[0],
                        resultSet.getInt("temperature"), "sold", entered_byAndTimestamp[0], entered_byAndTimestamp[1], resultSet.getString("shipping_date"),resultSet.getInt("ID_recieved")));
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        return transactionData;
    }

    /**
     * Check to see if index is selected, else connect to database and delete transaction.
     * @param event
     */
    @FXML
    public void deleteTransaction(ActionEvent event) {
        int selected = transactionTable.getSelectionModel().getSelectedIndex();
        if (selected >= 0) {
            Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
            transactionTable.getItems().remove(selected);
            try {
                DBUtil dbUtil = new DBUtil();
                dbUtil.deleteTransaction(selectedTransaction.getId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            infoBox("Delete Unsuccessful", "Error", "Alert");
        }
    }

    /**
     * Displays edit transaction FXML in separate window. Allows user to modify transaction and upon
     * pressing ok modifies the transaction in accordance.
     * @param event
     */
    @FXML
    public void editTrans(ActionEvent event) {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_transaction_dialog.fxml"));

            try {
                Parent root = (Parent) loader.load();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("Edit Transaction");
                dialogStage.initModality(Modality.APPLICATION_MODAL);


                // Set the person into the controller, i.e. show person details on the edit window
                EditTransactionDialog controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setTransaction(selectedTransaction);
                dialogStage.showAndWait();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            infoBox("No person selected", "Selection Error", "Alert");
        }
    }

    /**
     * Displays all the features of a transaction. Checks to make sure a transaction has been selected.
     * @param event
     */
    @FXML
    public void displayMoreInfo(ActionEvent event) {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("more_info_transaction.fxml"));

            try {
                Parent root = (Parent) loader.load();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                dialogStage.setTitle("Transaction Information");
                dialogStage.initModality(Modality.APPLICATION_MODAL);


                // Set the person into the controller, i.e. show person details on the edit window
                MoreInfoController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setTransaction(selectedTransaction);
                dialogStage.showAndWait();


            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }else{
            infoBox("No person selected", "Selection Error", "Alert");
        }
    }

    /**
     * Search through all transaction. Filters include product type, client, between dates, or any combination of the 3.
     * @param event
     */
    @FXML
    public void search(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search_filter.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Search Filter");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            SearchController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            if (controller.getTransactionData() != null) {
                transactionTable.getItems().setAll(controller.getTransactionData());
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Upon initialization of the FXML Tableview display the 10 most recent transaction.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        date.setCellValueFactory(new PropertyValueFactory<>("shippingDate"));
        productType.setCellValueFactory(new PropertyValueFactory<>("typeOfShellfish"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("client"));
        soldReceived.setCellValueFactory(new PropertyValueFactory<>("soldOrReceived"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));


        incomingTransactionData = getIncomingTransactionData();
        outgoingTransactionData = getOutgoingTransactionData();
        transactionTable.getItems().addAll(incomingTransactionData.subList((incomingTransactionData.size() < 10 ? 0 : incomingTransactionData.size() - 10), incomingTransactionData.size()));
        transactionTable.getItems().addAll(outgoingTransactionData.subList((outgoingTransactionData.size() < 10 ? 0 : outgoingTransactionData.size() - 10), outgoingTransactionData.size()));
    }
}

