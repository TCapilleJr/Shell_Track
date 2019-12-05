package shell_track.view;


//import com.google.protobuf.compiler.PluginProtos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
//import jdk.jfr.Category;
import shell_track.database.DBConnection;
import shell_track.database.DBUtil;
import shell_track.model.Transaction;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static javafx.fxml.FXMLLoader.load;

import java.util.logging.Logger;

public class ReportController extends EditTransactionController implements Initializable {

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

    private ObservableList<Transaction> incomingTransactionData;

    private ObservableList<Transaction> outgoingTransactionData;
    @FXML
    private MenuButton charts;

    @FXML
    private MenuButton logs;

    @FXML
    private Label client;
    @FXML
    private Label productTypes;
    @FXML
    private Label dateFrom;
    @FXML
    private Label dateTo;

    private ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    @FXML
    @Override
    public void search(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search_filter.fxml"));
        try {
            Parent root = (Parent) loader.load();
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            SearchController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();
            transactionData = controller.getTransactionData();
            if (transactionData != null) {
                transactionTable.getItems().setAll(controller.getTransactionData());
            }
        } catch (IOException e) {
            e.printStackTrace();
            infoBox("Filter Unsuccessful", "Filter", "Alert");
        }
    }

    @FXML
    public void clientVsTypeChart(ActionEvent event) {
        if (transactionData.isEmpty()) {
            infoBox("No Client Selected", "Selection Error", "Alert");
            return;
        }
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Type of Product");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<String, Number>();
        dataSeries.setName("Client vs Product Types");
        Map<String, Integer> map = new HashMap<>();
        String client_name = transactionData.get(0).getClient();
        for (Transaction transaction : transactionData) {
            if (map.containsKey(transaction.getTypeOfShellfish())) {
                int prev = map.get(transaction.getTypeOfShellfish());
                int addition = transaction.getNumberOfUnits();
                map.put(transaction.getTypeOfShellfish(), prev + addition);
            } else {
                map.put(transaction.getTypeOfShellfish(), transaction.getNumberOfUnits());
            }
        }
        barChart.setTitle(client_name + " vs " + "Product Types");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(dataSeries);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox(barChart);
        Scene scene = new Scene(vBox, 400, 400);
        dialog.setScene(scene);
        dialog.show();
    }

    @FXML
    public void typeVsClientChart(ActionEvent event) {
        if (transactionData.isEmpty()) {
            infoBox("No Product Type Selected", "Selection Error", "Alert");
            return;
        }
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Client");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<String, Number>();
        dataSeries.setName("Product Types vs Clients");
        barChart.setTitle(productType.getText() + " vs " + "Clients");
        Map<String, Integer> map = new HashMap<>();
        for (Transaction transaction : transactionData) {
            if (map.containsKey(transaction.getClient())) {
                int prev = map.get(transaction.getClient());
                int addition = transaction.getNumberOfUnits();
                map.put(transaction.getClient(), prev + addition);
            } else {
                map.put(transaction.getClient(), transaction.getNumberOfUnits());
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(dataSeries);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox vBox = new VBox(barChart);
        Scene scene = new Scene(vBox, 400, 400);
        dialog.setScene(scene);
        dialog.show();
    }


    @FXML
    public void exportIncomingAudit(ActionEvent event) throws FileNotFoundException {
        if (transactionTable.getSelectionModel().isEmpty()) {
            infoBox("No Transaction Selected", "No Selection", "Alert");
            return;
        } else if (transactionTable.getSelectionModel().getSelectedItem().getSoldOrReceived().equals("sold")) {
            infoBox("Outgoing Transaction Selected", "Wrong Selection", "Alert");
            return;
        }
        Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
        if (productType.getText().isEmpty()) {
            infoBox("No Product Type Selected", "Selection Error", "Alert");
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showSaveDialog(dialog);

        if (file != null) {
            SaveFile(transaction, file);
        }
    }

    private void SaveFile(Transaction transaction, File file) throws FileNotFoundException {
        try {

            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("Incoming Product - \n Client: " + transaction.getClient() + " Harvest Location: " + transaction.getHarvestLocation() + " Harvest Date:  " + transaction.getHarvestDate() + " Product on Hand: " + transaction.getProductOnHand());
            ObservableList<Transaction> outgoing = getOutgoingTransactionData(transaction.getId());
            for (Transaction t : outgoing) {
                printWriter.println("ID: " + t.getId() + " Sold To: " + t.getClient() + " Amount Sold: " + t.getNumberOfUnits());
            }
            printWriter.close();
            {
                infoBox("Successful Save", "File Save", "Alert");
            }
        } catch (IOException e) {
            infoBox("Unsuccessful Save", "File Save", "Alert");
            e.printStackTrace();
        }
    }

    public ObservableList<Transaction> getOutgoingTransactionData(int in_ID) {
        ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
        String SELECT_FROM_OUTGOING_LOG_AUDIT = "SELECT * from main.outgoing_transactions WHERE ID_recieved = " + in_ID;
        try {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_OUTGOING_LOG_AUDIT);
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

    @FXML
    public void exportReports(ActionEvent event) throws FileNotFoundException {
        if (transactionData.isEmpty()) {
            infoBox("No Transaction Selected", "No Selection", "Alert");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showSaveDialog(dialog);

        if (file != null) {
            try {

                PrintWriter printWriter = new PrintWriter(file);
                for (Transaction t : transactionData) {
                    printWriter.println("ID: " + t.getId() + " Number of Units: " + t.getNumberOfUnits() + " Count in Units: " + t.getCountPerUnit() + " Harvest Date: " + t.getHarvestDate()
                            + " Harvest Location: " + t.getHarvestLocation() + " Client: " + t.getClient() + " Sold/Received: " + t.getSoldOrReceived());
                }
                printWriter.close();
                {
                    infoBox("Successful Save", "File Save", "Alert");
                }
            } catch (IOException e) {
                infoBox("Unsuccessful Save", "File Save", "Alert");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void backupDatabase(ActionEvent event) {
        EditTransactionController edc = new EditTransactionController();
        incomingTransactionData = edc.getIncomingTransactionData();
        outgoingTransactionData = edc.getOutgoingTransactionData();
        transactionData.addAll(incomingTransactionData);
        transactionData.addAll(outgoingTransactionData);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showSaveDialog(dialog);

        if (file != null) {
            try {

                PrintWriter printWriter = new PrintWriter(file);
                for (Transaction t : transactionData) {
                    printWriter.println("ID: " + t.getId() + " Number of Units: " + t.getNumberOfUnits() + " Count in Units: " + t.getCountPerUnit() + " Harvest Date: " + t.getHarvestDate()
                            + " Harvest Location: " + t.getHarvestLocation() + " Client: " + t.getClient() + " Sold/Received: " + t.getSoldOrReceived());
                }
                printWriter.close();
                {
                    infoBox("Successful Save", "File Save", "Alert");
                }
            } catch (IOException e) {
                infoBox("Unsuccessful Save", "File Save", "Alert");
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        date.setCellValueFactory(new PropertyValueFactory<>("shippingDate"));
        productType.setCellValueFactory(new PropertyValueFactory<>("typeOfShellfish"));
        clientName.setCellValueFactory(new PropertyValueFactory<>("client"));
        soldReceived.setCellValueFactory(new PropertyValueFactory<>("soldOrReceived"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        EditTransactionController edc = new EditTransactionController();
        incomingTransactionData = edc.getIncomingTransactionData();
        outgoingTransactionData = edc.getOutgoingTransactionData();
        transactionData.addAll(incomingTransactionData);
        transactionData.addAll(outgoingTransactionData);
        transactionTable.getItems().addAll(incomingTransactionData.subList((incomingTransactionData.size() < 10 ? 0 : incomingTransactionData.size() - 10), incomingTransactionData.size()));
        transactionTable.getItems().addAll(outgoingTransactionData.subList((outgoingTransactionData.size() < 10 ? 0 : outgoingTransactionData.size() - 10), outgoingTransactionData.size()));
    }
}


