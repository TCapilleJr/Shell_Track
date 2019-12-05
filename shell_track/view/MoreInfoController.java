package shell_track.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.model.Transaction;

/**
 * Displays all information of transaction in pop-up scene
 */
public class MoreInfoController extends Controller {
    @FXML
    private Label transactionID;
    @FXML
    private Label client;
    @FXML
    private Label originalHarvestNumber;
    @FXML
    private Label orig_harvest_number;
    @FXML
    private Label typeOfShellfish;
    @FXML
    private Label countPerUnit;
    @FXML
    private Label numberOfUnits;
    @FXML
    private Label harvestLocation;
    @FXML
    private Label harvestDate;
    @FXML
    private Label harvest_date;
    @FXML
    private Label shippingDate;
    @FXML
    private Label temperature;
    @FXML
    private Label soldReceived;
    @FXML
    private Label enteredBy;
    @FXML
    private Label timestamp;
    @FXML
    private Label productOnHand;
    @FXML
    private Label product_on_hand;

    private Stage dialogStage;
    private Transaction transaction;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        if ((transaction.getSoldOrReceived().equals("sold"))) {
            product_on_hand.setText("Incoming ID: ");
//            harvestDate.setVisible(false);
//            harvestDate.setVisible(false);
//            orig_harvest_number.setVisible(false);
//            originalHarvestNumber.setVisible(false);
        }
//            product_on_hand.setText("Incoming ID: ");
        transactionID.setText(String.valueOf(transaction.getId()));
        client.setText(String.valueOf(transaction.getClient()));
        originalHarvestNumber.setText(String.valueOf(transaction.getOriginalHarvestNumber()));
        typeOfShellfish.setText(String.valueOf(transaction.getTypeOfShellfish()));
        numberOfUnits.setText(String.valueOf(transaction.getNumberOfUnits()));
        countPerUnit.setText(String.valueOf(transaction.getCountPerUnit()));
        harvestLocation.setText(transaction.getHarvestLocation());
        harvestDate.setText(String.valueOf(transaction.getHarvestDate()));
        temperature.setText(String.valueOf(transaction.getTemperature()));
        soldReceived.setText(String.valueOf(transaction.getSoldOrReceived()));
        enteredBy.setText(String.valueOf(transaction.getEnteredBy()));
        shippingDate.setText(String.valueOf(transaction.getShippingDate()));
        timestamp.setText(String.valueOf(transaction.getTimeEntered()));
        productOnHand.setText(String.valueOf(transaction.getProductOnHand()));

    }

    @FXML
    public void back(ActionEvent event) {
        dialogStage.close();
    }

}
