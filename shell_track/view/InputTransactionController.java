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
import javafx.stage.Modality;
import javafx.stage.Stage;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.Transaction;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Input Transaction Controller provides the user a way to eneter transaction data inot database.
 * For incoming transactions, a checkbox is provided to signify that it is incoming.  Upon selection
 * a window of product on hand that is not below zero is displayed.  If any are selected they fields will be preloaded
 * with that transaction attributes.  If product on hand is < 0 an alarm will be displayed
 */
public class InputTransactionController extends Controller implements Initializable, Serializable {

    @FXML
    private ComboBox<String> typeOfShellFish;
    @FXML
    private TextField numberOfUnits;
    @FXML
    private TextField countPerUnit;
    @FXML
    private ComboBox<String> harvestLocation;
    @FXML
    private TextField temperature;
    @FXML
    private ComboBox<String> client;
    @FXML
    private DatePicker harvestDate;
    @FXML
    private DatePicker shippingDate;
    @FXML
    private CheckBox sold;
    @FXML
    private ComboBox<String> originalHarvestNumber;

    //Incoming transaction to be modified if inputted transaction is outgoing
    private Transaction selectedTransaction;

    private final String PRODUCT_TYPE_QUERY = "SELECT * FROM main.type_of_product";
    private final String CLIENTS_QUERY = "SELECT * FROM main.clients";
    private final String HARVEST_LOCATIONS_QUERY = "SELECT * FROM main.harvest_locations";
    private final String ORIGINAL_HARVEST_NUMBERS_QUERY = "SELECT * FROM main.original_harvest_number";

    public ObservableList<String> getHarvestLocations() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(HARVEST_LOCATIONS_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("harvest_locations"));
        }
        return data;
    }

    public ObservableList<String> getOriginalHarvestNumbers() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(ORIGINAL_HARVEST_NUMBERS_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("original_harvester_number"));
        }
        return data;
    }

    public ObservableList<String> getClient() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CLIENTS_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("client_name"));
        }
        return data;
    }

    public ObservableList<String> getProduct() throws SQLException, ClassNotFoundException {

        ObservableList<String> data = FXCollections.observableArrayList();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(PRODUCT_TYPE_QUERY);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            data.add(resultSet.getString("type"));
        }
        return data;
    }

    /**
     * Checks for proper input. Checks for incoming or outgoing. If out then subtracts the product on hand of corresponding incoming transaction from the num units of
     * outgoing transaction.
     *
     * @param event
     * @throws SQLException
     */
    public void addTransaction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (isCorrectInput()) {
            String shellfish_type = typeOfShellFish.getSelectionModel().getSelectedItem();
            String orig_harvest_num = originalHarvestNumber.getSelectionModel().getSelectedItem();
            int num_of_units = Integer.parseInt(numberOfUnits.getText());
            int num_in_units = Integer.parseInt(countPerUnit.getText());
            String location = harvestLocation.getSelectionModel().getSelectedItem();
            String harvest_date = harvestDate.getValue().toString();
            String shipping_date = shippingDate.getValue().toString();
            String to_from = client.getSelectionModel().getSelectedItem();
            String sold_received = "";
            int temp = Integer.parseInt(temperature.getText());
            Transaction transaction = new Transaction(0, to_from, orig_harvest_num, shellfish_type, num_of_units, num_in_units, harvest_date, location, temp, sold_received, null, null, shipping_date, 0);
            DBUtil dbUtil = new DBUtil();
            //Timestamp and Entered by
            int row = dbUtil.addToTransactionLog();
            System.out.println(row);
            if (sold.isSelected() && selectedTransaction != null) {
                dbUtil.editTransactionforSale(selectedTransaction, num_of_units);
                dbUtil.addToOutGoing(transaction, row, selectedTransaction.getId());
                if (selectedTransaction.getProductOnHand() - num_of_units < 0) {
                    infoBox("Product has been oversold", "Product Alert", "Alert");
                    return;
                }
            } else if (!sold.isSelected()) {
                dbUtil.addToIncoming(transaction, row);
            } else {
                infoBox("Must Select from Product Available", "Inventory Control", "Alert");
                return;
            }
        } else {
            infoBox("Not a proper input", "Invalid Selection", "Alert");
            return;
        }
        clearAllFields();
    }

    /**
     * Checks to make sure all fields are properly filled with their correct attributes
     *
     * @return
     */
    public boolean isCorrectInput() {
        try {
            Integer.parseInt(numberOfUnits.getText());
            Integer.parseInt(numberOfUnits.getText());
            Integer.parseInt(temperature.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        if (typeOfShellFish.getSelectionModel().isEmpty() || typeOfShellFish.getSelectionModel().getSelectedItem().isEmpty()) {
            return false;
        } else if (numberOfUnits.getText().isEmpty()) {
            return false;
        } else if (countPerUnit.getText().isEmpty()) {
            return false;
        } else if (harvestLocation.getSelectionModel().isEmpty() || harvestLocation.getSelectionModel().getSelectedItem().isEmpty()) {
            return false;
        } else if (temperature.getText().isEmpty()) {
            return false;
        } else if (client.getSelectionModel().isEmpty() || client.getSelectionModel().getSelectedItem().isEmpty()) {
            return false;
        } else if (shippingDate.getValue() == null) {
            return false;
        } else if (harvestDate.getValue() == null) {
            return false;
        } else if (originalHarvestNumber.getSelectionModel().isEmpty() || originalHarvestNumber.getSelectionModel().getSelectedItem().isEmpty()) {
            return false;
        } else if (harvestDate.getValue().isAfter(shippingDate.getValue())) {
            return false;
        } else {
            return true;
        }
    }

    public void clearAllFields() {
        harvestLocation.getSelectionModel().clearSelection();
        originalHarvestNumber.getSelectionModel().clearSelection();
        typeOfShellFish.getSelectionModel().clearSelection();
        client.getSelectionModel().clearSelection();
        numberOfUnits.clear();
        sold.setSelected(false);
        sold.setDisable(false);
        harvestDate.setValue(null);
        shippingDate.setValue(null);
        temperature.clear();
        countPerUnit.clear();
        countPerUnit.setDisable(false);
        originalHarvestNumber.setDisable(false);
        typeOfShellFish.setDisable(false);
        harvestDate.setDisable(false);
        harvestLocation.setDisable(false);

    }

    /**
     * Loads scene to add edit product type.  Sets list inside this class to new list from ProductTypeController
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void productTypeScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_edit_product_type_dialog.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Product Type Addition");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            ProductTypeController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            typeOfShellFish.setItems(getProduct());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * same as product type except for clients
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void clientScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_edit_client.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Clients");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            ClientController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            client.setItems(getClient());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * same as productTypeScene
     *
     * @param event
     */
    public void harvestLocationScene(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_edit_harvestLocation.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Harvest Location");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            HarvestLocationController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            harvestLocation.setItems(getHarvestLocations());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * same as productTypeScene
     *
     * @param event
     */
    public void harvestNumberScene(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_edit_harvestNumbers.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Original Harvester Number");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            HarvesterNumberController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            originalHarvestNumber.setItems(getOriginalHarvestNumbers());
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * same as productTypeScene
     *
     * @param event
     */
    @FXML
    public void productOnHandScene(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("available_incoming_product.fxml"));

        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.setTitle("Product On Hand");
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            ProductOnHandController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            if(controller.getCancel()){
                clearAllFields();
                return;
            }
            if (controller.getSelectedTransaction() != null) {
                //Must Clear selection prior to selecting product on hand
                harvestLocation.getSelectionModel().clearSelection();
                originalHarvestNumber.getSelectionModel().clearSelection();
                typeOfShellFish.getSelectionModel().clearSelection();
                client.getSelectionModel().clearSelection();

                Transaction transaction = controller.getSelectedTransaction();
                selectedTransaction = transaction;
                harvestLocation.getSelectionModel().select(transaction.getHarvestLocation());
                originalHarvestNumber.getSelectionModel().select(transaction.getOriginalHarvestNumber());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate selectedDate = LocalDate.parse(transaction.getShippingDate(), formatter);
                harvestDate.setValue(selectedDate);
                typeOfShellFish.getSelectionModel().select(transaction.getTypeOfShellfish());
                countPerUnit.setText(String.valueOf(transaction.getCountPerUnit()));
                countPerUnit.setDisable(true);
                originalHarvestNumber.setDisable(true);
                typeOfShellFish.setDisable(true);
                harvestDate.setDisable(true);
                harvestLocation.setDisable(true);
                sold.setSelected(true);
                sold.setDisable(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            typeOfShellFish.setItems(getProduct());
            client.setItems(getClient());
            originalHarvestNumber.setItems(getOriginalHarvestNumbers());
            harvestLocation.setItems(getHarvestLocations());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
