package shell_track.view;

import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import shell_track.Shell_Track_App;
import shell_track.database.DBUtil;

import static javafx.fxml.FXMLLoader.*;
import static javafx.fxml.FXMLLoader.load;

public class AdminMenuController extends Controller implements Initializable {
    @FXML
    private Button addNewUser;
    @FXML
    private Button editUser;
    @FXML
    private Button inputTransactions;
    @FXML
    private Button editTransactions;
    @FXML
    private Button reports;
    @FXML
    private AnchorPane switchPane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label signedInUser;


    /**
     * Switch to add/edit user scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void editUser(ActionEvent event) throws IOException {
        Pane newPane = load(getClass().getResource("edit_user.fxml"));

        List<Node> parentChildren = ((Pane) switchPane.getParent()).getChildren();

        parentChildren.set(parentChildren.indexOf(switchPane), newPane);

        switchPane = (AnchorPane) newPane;
    }

    /**
     * Switch to input transaction scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void inputTransaction(ActionEvent event) throws IOException {
        Pane newPane = load(getClass().getResource("input_transaction.fxml"));

        List<Node> parentChildren = ((Pane) switchPane.getParent()).getChildren();

        parentChildren.set(parentChildren.indexOf(switchPane), newPane);

        switchPane = (AnchorPane) newPane;
    }

    /**
     * Switch to edit transaction scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void editTransaction(ActionEvent event) throws IOException {
        Pane newPane = load(getClass().getResource("edit_transaction.fxml"));

        List<Node> parentChildren = ((Pane) switchPane.getParent()).getChildren();

        parentChildren.set(parentChildren.indexOf(switchPane), newPane);

        switchPane = (AnchorPane) newPane;
    }

    /**
     * Switch to reports scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void reports(ActionEvent event) throws IOException {

        Pane newPane = load(getClass().getResource("report.fxml"));

        List<Node> parentChildren = ((Pane) switchPane.getParent()).getChildren();

        parentChildren.set(parentChildren.indexOf(switchPane), newPane);

        switchPane = (AnchorPane) newPane;
    }

    @FXML
    public void signOut(ActionEvent event) throws IOException {
        DBUtil.signedInUser = null;
        switchScreen("login_menu.fxml", event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signedInUser.setText(DBUtil.signedInUser);
    }
}

