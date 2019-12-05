package shell_track.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.Transaction;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Allows for searching of records by dates, product type, or client. Or any combo of the 3
 */
public class SearchController extends Controller implements Initializable {
    private Stage dialogStage;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private ComboBox<String> productType;
    @FXML
    private ComboBox<String> client;
    @FXML
    private CheckBox soldReceived;

    private ObservableList<Transaction> transactionData;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void cancel(ActionEvent event) {
        dialogStage.close();
    }

    public String makeSearchQuery() {
        String searchQuery = "";
        //All selections are made
        if (!soldReceived.isSelected()) {
            if ((dateFrom.getValue() != null) && (dateTo.getValue() != null) && !productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                System.out.println("test1");
                searchQuery = "SELECT * FROM main.incoming_transaction WHERE client_name = '" + client.getSelectionModel().getSelectedItem().replaceAll("'", "") + "' AND type_of_shellfish = '" +
                        productType.getSelectionModel().getSelectedItem() + "' AND DATE(received_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(received_date) <= " + "'" + dateTo.getValue() + "'";
                // Just dates are picked
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && (productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty())) {
                System.out.println("test2");
                searchQuery = "SELECT * FROM main.incoming_transaction WHERE DATE(received_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(received_date) <= " + "'" + dateTo.getValue() + "'";
                //Just Product Type is Picked
            } else if ((dateFrom.getValue() == null && dateTo.getValue() == null) && !productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty()) {
                System.out.println("test3");
                searchQuery = "Select * FROM main.incoming_transaction WHERE type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'";
                System.out.println(productType.getSelectionModel().getSelectedItem());
                //Just Client is Picked
            } else if ((dateFrom.getValue() == null && dateTo.getValue() == null) && productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.incoming_transaction WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'";
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && !productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.incoming_transaction WHERE type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'" + " AND DATE(received_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(received_date) <= " + "'" + dateTo.getValue() + "'";
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.incoming_transaction WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'" + " AND DATE(received_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(received_date) <= " + "'" + dateTo.getValue() + "'";
            } else if((dateFrom.getValue() == null && dateTo.getValue() == null) && !productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                System.out.println("test 4");
                searchQuery = "Select * FROM main.incoming_transaction WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'" + "AND type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'";
            }else{
                searchQuery = "SELECT * FROM main.incoming_transaction";
            }
        } else {
            if ((dateFrom.getValue() != null) && (dateTo.getValue() != null) && !productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                System.out.println("test1");
                searchQuery = "SELECT * FROM main.outgoing_transactions WHERE client_name = '" + client.getSelectionModel().getSelectedItem().replaceAll("'", "") + "' AND type_of_shellfish = '" +
                        productType.getSelectionModel().getSelectedItem() + "' AND DATE(shipping_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(shipping_date) <= " + "'" + dateTo.getValue() + "'";
                // Just dates are picked
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && (productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty())) {
                System.out.println("test2");
                searchQuery = "SELECT * FROM main.outgoing_transactions WHERE DATE(shipping_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(shipping_date) <= " + "'" + dateTo.getValue() + "'";
                //Just Product Type is Picked
            } else if ((dateFrom.getValue() == null && dateTo.getValue() == null) && !productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty()) {
                System.out.println("test3");
                searchQuery = "Select * FROM main.outgoing_transactions WHERE type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'";
                System.out.println(productType.getSelectionModel().getSelectedItem());
                //Just Client is Picked
            } else if ((dateFrom.getValue() == null && dateTo.getValue() == null) && productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.outgoing_transactions WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'";
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && !productType.getSelectionModel().isEmpty() && client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.outgoing_transactions WHERE type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'" + " AND DATE(shipping_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(shipping_date) <= " + "'" + dateTo.getValue() + "'";
            } else if ((dateFrom.getValue() != null && dateTo.getValue() != null) && productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.outgoing_transactions WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'" + " AND DATE(shipping_date) >= " + "'" + dateFrom.getValue() + "'" + " AND DATE(shipping_date) <= " + "'" + dateTo.getValue() + "'";
            } else if((dateFrom.getValue() == null && dateTo.getValue() == null) && !productType.getSelectionModel().isEmpty() && !client.getSelectionModel().isEmpty()) {
                searchQuery = "Select * FROM main.outgoing_transactions WHERE client_name = '" + client.getSelectionModel().getSelectedItem() + "'" + "AND type_of_shellfish = '" + productType.getSelectionModel().getSelectedItem() + "'";
            }else{
                searchQuery = "SELECT * FROM main.outgoing_transactions";
            }
        }
        return searchQuery;
    }

    /**
     * Returns result of desired query
     *
     * @param event
     */
    @FXML
    public void submit(ActionEvent event) {
        if((dateFrom.getValue() == null && dateTo.getValue() != null) || (dateFrom.getValue() != null && dateTo.getValue() == null)){
            infoBox("No Search Made", "Search Error", "Alert");
            return;
        }
        String searchQuery = makeSearchQuery();
        if (searchQuery.isEmpty()) {
            infoBox("No Search Made", "Search Error", "Alert");
            return;
        }
        if (soldReceived.isSelected()) {
            this.transactionData = getOutgoingTransactionData();
        } else {
            this.transactionData = getIncomingTransactionData();
        }
        dialogStage.close();

    }

    private ObservableList<Transaction> getIncomingTransactionData() {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(makeSearchQuery());
            ResultSet resultSet = preparedStatement.executeQuery();
            DBUtil dbUtil = new DBUtil();
            while (resultSet.next()) {
                String[] entered_byAndTimestamp = dbUtil.getTimeEnteredAndTimeStamp(resultSet.getInt("ID"));
                transactionData.add(new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), resultSet.getString("original_harvest_number"), resultSet.getString("type_of_shellfish"),
                        resultSet.getInt("recieved_units"), resultSet.getInt("count_in_units"), resultSet.getString("harvest_date"), resultSet.getString("harvest_location"),
                        resultSet.getInt("temperature"), "received", entered_byAndTimestamp[0], entered_byAndTimestamp[1], resultSet.getString("received_date"), resultSet.getInt("product_on_hand")));
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return transactionData;
    }

    private ObservableList<Transaction> getOutgoingTransactionData() {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(makeSearchQuery());
            ResultSet resultSet = preparedStatement.executeQuery();
            DBUtil dbUtil = new DBUtil();
            // In the case of Outgoing Transaction product on hand is replaced by ID_received
            while (resultSet.next()) {
                String[] entered_byAndTimestamp = dbUtil.getTimeEnteredAndTimeStamp(resultSet.getInt("ID"));
                String[] incomingData = dbUtil.getHarvestLocationAndHarvestNumber(resultSet.getInt("ID_recieved"));
                transactionData.add(new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), incomingData[1], resultSet.getString("type_of_shellfish"),
                        resultSet.getInt("units_sold"), Integer.parseInt(incomingData[3]), incomingData[2], incomingData[0],
                        resultSet.getInt("temperature"), "sold", entered_byAndTimestamp[0], entered_byAndTimestamp[1], resultSet.getString("shipping_date"), resultSet.getInt("ID_recieved")));
            }
            connection.close();
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return transactionData;
    }

    public ObservableList<Transaction> getTransactionData() {
        return transactionData;
    }

    public String getDateFrom() {
        if (dateFrom.getValue() != null) {
            return dateFrom.getValue().toString();
        }
        return "";
    }

    public String getDateTo() {
        if (dateFrom.getValue() != null) {
            return dateTo.getValue().toString();
        }
        return "";
    }

    public String getClientSelection() {
        if (!client.getSelectionModel().isEmpty()) {
            return client.getSelectionModel().getSelectedItem();
        } else {
            return "";
        }
    }

    public String getProductType() {
        if (!productType.getSelectionModel().isEmpty()) {
            return productType.getSelectionModel().getSelectedItem();
        } else {
            return "";
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputTransactionController itc = new InputTransactionController();
        try {
            productType.setItems(itc.getProduct());
            client.setItems(itc.getClient());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

