package shell_track.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shell_track.database.DBUtil;
import shell_track.model.Client;

import java.sql.SQLException;


public class EditClientContoller extends Controller {

    @FXML
    private TextField clientName;

    @FXML
    private TextField address;

    @FXML
    private TextField contactInfo;

    private Stage dialogStage;

    private String originalClientName;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setClient(Client client) {
        originalClientName = client.getClientName();
        System.out.println(originalClientName);
        clientName.setText(client.getClientName());
        address.setText(client.getAddress());
        contactInfo.setText(client.getContactInfo());
    }

    @FXML
    public void editClient(ActionEvent event) throws SQLException, ClassNotFoundException {
        System.out.println(originalClientName);
        Client client = new Client(clientName.getText(), address.getText(), contactInfo.getText());
        DBUtil dbUtil = new DBUtil();
        dbUtil.editClient(client, originalClientName);
        dialogStage.close();
    }

    @FXML
    public void cancel(ActionEvent event) {
        dialogStage.close();
    }

}
