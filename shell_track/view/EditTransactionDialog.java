package shell_track.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.database.DBUtil;
import shell_track.model.Transaction;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Dialog scene that is displayed outside main menu.  Allows for user to edit transaction minus timestamp and entered by.
 */
public class EditTransactionDialog extends Controller implements Initializable {
    @FXML
    private Label transactionID;
    @FXML
    private ComboBox<String> client;
    @FXML
    private ComboBox<String> originalHarvestNumber;
    @FXML
    private ComboBox<String> typeOfShellfish;
    @FXML
    private TextField countPerUnit;
    @FXML
    private TextField numberOfUnits;
    @FXML
    private ComboBox<String> harvestLocation;
    @FXML
    private DatePicker harvestDate;
    @FXML
    private DatePicker shippingDate;
    @FXML
    private TextField temperature;
    @FXML
    private Label soldReceived;
    @FXML
    private TextField productOnHand;
    @FXML
    private Label enteredBy;
    @FXML
    private Label timestamp;
    @FXML
    private Label product_on_hand;

    private Stage dialogStage;

    /**
     * Transaction to be edited
     */
    private Transaction transaction;

    /**
     * Sets stage from Edit Transaction FXML
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets info to textfields of edit fields
     *
     * @param transaction
     */
    public void setTransaction(Transaction transaction) {

        LocalDate.parse(transaction.getHarvestDate());
        this.transaction = transaction;
        transactionID.setText(String.valueOf(transaction.getId()));
        numberOfUnits.setText(String.valueOf(transaction.getNumberOfUnits()));
        countPerUnit.setText(String.valueOf(transaction.getCountPerUnit()));

        String[] harvest_date = transaction.getShippingDate().split("-");
        harvestDate.setValue(LocalDate.of(Integer.parseInt(harvest_date[0]), Integer.parseInt(harvest_date[1]), Integer.parseInt(harvest_date[2])));

        temperature.setText(String.valueOf(transaction.getTemperature()));
        soldReceived.setText(String.valueOf(transaction.getSoldOrReceived()));
        enteredBy.setText(String.valueOf(transaction.getEnteredBy()));

        String[] ship_date = transaction.getShippingDate().split("-");
        shippingDate.setValue(LocalDate.of(Integer.parseInt(ship_date[0]), Integer.parseInt(ship_date[1]), Integer.parseInt(ship_date[2])));

        timestamp.setText(String.valueOf(transaction.getTimeEntered()));
        client.getSelectionModel().select(transaction.getClient());
        originalHarvestNumber.getSelectionModel().select(transaction.getOriginalHarvestNumber());
        typeOfShellfish.getSelectionModel().select(transaction.getTypeOfShellfish());
        harvestLocation.getSelectionModel().select(transaction.getHarvestLocation());
        //If product is outgoing there is no product on hand and becomes ID of Incoming
        if (soldReceived.getText().equals("sold")) {
            product_on_hand.setText("ID of Incoming: ");
            productOnHand.setText(String.valueOf(transaction.getProductOnHand()));
            productOnHand.setEditable(false);
            countPerUnit.setEditable(false);

        } else {
            productOnHand.setText(String.valueOf(transaction.getProductOnHand()));
        }
    }

    /**
     * Opens DBUtil and edits transaction
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void handleOk(ActionEvent event) throws SQLException, ClassNotFoundException {
        int sold_adjust = transaction.getNumberOfUnits();
        if (isValidInput()) {
            transaction.setClient(client.getSelectionModel().getSelectedItem());
            transaction.setOriginalHarvestNumber(originalHarvestNumber.getSelectionModel().getSelectedItem());
            transaction.setTypeOfShellfish(typeOfShellfish.getSelectionModel().getSelectedItem());
            transaction.setCountPerUnit(Integer.parseInt(countPerUnit.getText()));
            transaction.setNumberOfUnits(Integer.parseInt(numberOfUnits.getText()));
            transaction.setHarvestDate(harvestDate.getValue().toString());
            transaction.setHarvestLocation(harvestLocation.getSelectionModel().getSelectedItem());
            transaction.setShippingDate(shippingDate.getValue().toString());
            transaction.setTemperature(Integer.parseInt(temperature.getText()));
            transaction.setSold(soldReceived.getText());
            transaction.setProductOnHand(Integer.parseInt(productOnHand.getText()));
            transaction.setEnteredBy(enteredBy.getText() + " : " + DBUtil.signedInUser);

            DBUtil dbUtil = new DBUtil();
            if (soldReceived.getText().equals("sold")) {
                if (sold_adjust < transaction.getNumberOfUnits()) {
                    Transaction incomingAdjust = dbUtil.getIncomingTransaction(transaction.getProductOnHand());
                    sold_adjust = incomingAdjust.getProductOnHand() - transaction.getProductOnHand();
                    dbUtil.editTransactionforSale(incomingAdjust, sold_adjust);
                    dbUtil.editOutgoingTransaction(transaction);
                } else {
                    Transaction incomingAdjust = dbUtil.getIncomingTransaction(transaction.getProductOnHand());
                    sold_adjust = incomingAdjust.getProductOnHand() + transaction.getProductOnHand();
                    dbUtil.editTransactionforSale(incomingAdjust, sold_adjust);
                    dbUtil.editOutgoingTransaction(transaction);
                }
            } else {
                dbUtil.editIncomingTransaction(transaction);
            }
            infoBox("Edit Successful", "Edit Transaction", "Alert");
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        dialogStage.close();
    }

    /**
     * Checks to ensure all fields are correctly inputted. Else, alert wil be displayed
     *
     * @return
     */
    private boolean isValidInput() {
        boolean valid = false;
        try {
            Integer.parseInt(temperature.getText());
            Integer.parseInt(numberOfUnits.getText());
            Integer.parseInt(countPerUnit.getText());
            Integer.parseInt(productOnHand.getText());
        } catch (NumberFormatException e) {
            return false;
        }
        if (client.getSelectionModel().isEmpty() || client.getSelectionModel().getSelectedItem().isEmpty()) {

        } else if (originalHarvestNumber.getSelectionModel().isEmpty() || originalHarvestNumber.getSelectionModel().getSelectedItem().isEmpty()) {

        } else if (typeOfShellfish.getSelectionModel().isEmpty() || typeOfShellfish.getSelectionModel().getSelectedItem().isEmpty()) {

        } else if (numberOfUnits.getText() == null) {

        } else if (countPerUnit.getText() == null || countPerUnit.getText().isEmpty()) {

        } else if (harvestDate.getValue() == null) {

        } else if (harvestLocation.getSelectionModel().isEmpty() || harvestLocation.getSelectionModel().getSelectedItem().isEmpty()) {

        } else if (temperature.getText() == null) {

        } else if (soldReceived.getText() == null) {

        } else if (shippingDate.getValue() == null) {

        } else {
            valid = true;
        }
        if (!valid) {
            infoBox("Invalid Input", " Edit Transaction", "Alert");
        }
        return valid;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InputTransactionController inputTransactionController = new InputTransactionController();
        try {
            typeOfShellfish.setItems(inputTransactionController.getProduct());
            client.setItems(inputTransactionController.getClient());
            originalHarvestNumber.setItems(inputTransactionController.getOriginalHarvestNumbers());
            harvestLocation.setItems(inputTransactionController.getHarvestLocations());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
