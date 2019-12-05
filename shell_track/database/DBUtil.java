package shell_track.database;

import org.sqlite.SQLiteException;
import shell_track.Email;
import shell_track.model.Client;
import shell_track.model.Transaction;
import shell_track.model.User;
import shell_track.view.AddUserController;
import shell_track.view.Controller;
import shell_track.view.EditUserController;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DBUtil {
    /**
     * Current User Signed In
     */
    public static String signedInUser;

    /**
     * Sql Selection Queries
     */
    private static final String SELECT_QUERY = "SELECT * from main.login WHERE user_name = ? AND password = ?";

    private final String SELECT_FROM_TRANSACTION_LOG = "SELECT * from main.transaction_log WHERE ID = ?";

    private final String SELECT_FROM_INCOMING_LOG = "SELECT * from main.incoming_transaction WHERE ID = ?";

    /**
     * Access level of user
     */
    private String userAccess;

    /**
     * SQL Delete Statements
     */
    private final String DELETE_USER_LOGIN = "DELETE FROM main.users where user_name = ?";

    private final String DELETE_TRANSACTION = "DELETE FROM main.transaction_log  where ID = ?";

    private final String DELETE_PRODUCT_TYPE = "DELETE FROM main.type_of_product  where type = ?";

    private final String DELETE_HARVEST_LOCATION = "DELETE FROM main.harvest_locations  where harvest_locations = ?";

    private final String DELETE_ORGINAL_HARVEST_NUMBER = "DELETE FROM main.original_harvest_number  where original_harvester_number = ?";

    private final String DELETE_CLIENT = "DELETE FROM main.clients  where client_name = ?";

    /**
     * Sql Addition Statements
     */
    private final String ADD_USER_TO_LOGIN = "INSERT INTO main.login(user_name, password, user_access)" + "VALUES (?,?,?)";

    private final String ADD_USER_TO_USERS = "INSERT INTO main.users(user_name, first_name, last_name, email, address)" + "VALUES (?,?,?,?,?)";

    private final String ADD_TO_TRANSACTION_LOG = "INSERT INTO main.transaction_log(entered_by, time_stamp)" + "VALUES (?,?)";

    private final String ADD_TO_TYPE_OF_PRODUCT = "INSERT INTO main.type_of_product(type)" + "VALUES (?)";

    private final String ADD_TO_HARVEST_LOCATIONS = "INSERT INTO main.harvest_locations(harvest_locations)" + "VALUES (?)";

    private final String ADD_TO_ORIGINAL_HARVEST_NUMBER = "INSERT INTO main.original_harvest_number(original_harvester_number)" + "VALUES (?)";

    private final String ADD_TO_CLIENTS = "INSERT INTO main.clients(client_name,address,contact_info)" + "VALUES (?,?,?)";

    private final String ADD_TO_INCOMING = "INSERT INTO main.incoming_transaction(ID,client_name,recieved_units,count_in_units," +
            "harvest_date,harvest_location,original_harvest_number,product_on_hand,received_date,temperature,type_of_shellfish)" + "VALUES (?,?,?," +
            "?,?,?,?,?,?,?,?)";

    private final String ADD_TO_OUTGOING = "INSERT INTO main.outgoing_transactions(ID,client_name,units_sold," +
            "shipping_date,ID_recieved,temperature,type_of_shellfish)" + "VALUES (?,?,?,?,?,?,?)";

    /**
     * SQL Edit Statements
     */

    private final String EDIT_USER_USERS = "UPDATE main.users set user_name = ?, first_name = ?, last_name = ?, " +
            "email = ?, address = ? WHERE user_name = ?";

    private final String EDIT_USER_LOGIN = "UPDATE main.login set user_name = ?, password = ?, user_access = ? WHERE user_name = ?";

    private final String EDIT_CLIENT = "UPDATE main.clients set client_name = ?, address = ?, contact_info = ? WHERE client_name = ?";

    private final String EDIT_TRANSACTION_FOR_SALE = "UPDATE main.incoming_transaction set product_on_hand = ? WHERE ID = ?";

    private final String EDIT_OUTGOING_TRANSACTION = "UPDATE main.outgoing_transactions set client_name = ?, units_sold = ?, shipping_date = ?, ID_recieved = ?, type_of_shellfish = ?, temperature = ? WHERE ID = ?";

    private final String EDIT_INCOMING_TRANSACTION = "UPDATE main.incoming_transaction set client_name = ?, recieved_units = ?, count_in_units = ?, harvest_date = ?, harvest_location = ?, original_harvest_number = ?, product_on_hand = ?, received_date = ?, type_of_shellfish = ?, temperature = ? WHERE ID = ?";

    private final String EDIT_PASSWORD = "UPDATE main.login set password = ? WHERE user_name = ?";

    /**
     * Uses prepared SELECT_QUERY statement to select a user from login table.  User name and password
     * are used together to check for existence. If user exists, signed in user will be assigned the user name of
     * person signing in and their access is stored in the boolean userAccess.
     *
     * @param userName - text from username textfield
     * @param password - password from password field
     * @return true if user exists, false otherwise
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean validate(String userName, String password) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            System.out.println(SELECT_QUERY);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                signedInUser = userName;
                userAccess = resultSet.getString("user_access");
                return true;
            } else {
                resultSet.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return True if access is employee level
     */
    public boolean getUserAccess() {
        return userAccess.equals("employee");
    }

//    public boolean addTransaction(String client, String originalHarvestNumber, String typeOfShellFish, int numberOfUnits,
//                                  int countPerUnit, String harvestLocation, String harvestDate, String shippingDate, int temperature, String sold, int productOnHand) throws SQLException, ClassNotFoundException {
//
//        java.util.Date date = new Date();
//        System.out.println(ADD_TRANSACTION);
//        DBConnection database = new DBConnection();
//        Connection connection = database.getConnection();
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TRANSACTION);
//            preparedStatement.setString(1, client);
//            preparedStatement.setString(2, originalHarvestNumber);
//            preparedStatement.setString(3, typeOfShellFish);
//            preparedStatement.setInt(4, numberOfUnits);
//            preparedStatement.setInt(5, countPerUnit);
//            preparedStatement.setString(6, harvestLocation);
//            preparedStatement.setString(7, harvestDate);
//            preparedStatement.setInt(8, temperature);
//            preparedStatement.setString(9, sold);
//            preparedStatement.setString(10, signedInUser);
//            preparedStatement.setString(11, date.toString());
//            preparedStatement.setString(12, shippingDate);
//            preparedStatement.setInt(13, productOnHand);
//            if (!preparedStatement.execute()) {
//                AddUserController.infoBox("New Transaction Added", "Entry Accepted", "Alert");
//                return true;
//            }
//        } catch (SQLIntegrityConstraintViolationException e) {
//            AddUserController.infoBox("Entry already exists", "Duplicate Entry", "Alert");
//        }
//        return false;
//    }


    /**
     * Establish a connect with try with resource statement. Uses a prepared statement along with our final ADD_USer
     * to add user to database. Catches Duplicate entries
     *
     * @returnIf Injection is sucessful method return True, otherwise false
     */
    public boolean addUserToLogin(User user) throws SQLException, ClassNotFoundException {
        System.out.println(ADD_USER_TO_LOGIN);
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        DBConnection database = new DBConnection();
        // Step 2:Create a statement using connection object
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatementLogin = connection.prepareStatement(ADD_USER_TO_LOGIN);
            preparedStatementLogin.setString(1, user.getUserName());
            preparedStatementLogin.setString(2, user.getUserPassword());
            preparedStatementLogin.setString(3, user.getUserAccess());
            preparedStatementLogin.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addUserToUser(User user) throws SQLException, ClassNotFoundException {
        System.out.println(ADD_USER_TO_USERS);
        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        DBConnection database = new DBConnection();
        // Step 2:Create a statement using connection object
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatementUser = connection.prepareStatement(ADD_USER_TO_USERS);
            preparedStatementUser.setString(1, user.getUserName());
            preparedStatementUser.setString(2, user.getUserFirstName());
            preparedStatementUser.setString(3, user.getUserLastName());
            preparedStatementUser.setString(4, user.getUserEmail());
            preparedStatementUser.setString(5, user.getUserAddress());
            boolean executeUser = preparedStatementUser.execute();
            if (!executeUser) {
                AddUserController.infoBox("New User Added", "Entry Accepted", "Alert");
                Email email = new Email();
                email.sendEmail(user.getUserEmail(), "Welcome to ShellTrack", "Welcome to the worlds premiere shellfish tracking service! ");

                return true;
            }
            //Print Sql Information  and catch duplicate entries
        } catch (SQLException e) {
            AddUserController.infoBox("Entry already exists", "Duplicate Entry", "Alert");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(String username) throws SQLException, ClassNotFoundException {
        if (username.isEmpty()) {
            EditUserController.infoBox("No Entry Selected", "Duplicate Entry", "Alert");
            return false;
        }
        System.out.println(DELETE_USER_LOGIN);
        System.out.println(username);
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_LOGIN);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
            AddUserController.infoBox("User Deleted", "User Deleted", "Alert");
            return true;
        } catch (SQLiteException e) {
            AddUserController.infoBox("Not Deleted", "Delete Attempt", "Alert");
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTransaction(int id) throws SQLException, ClassNotFoundException {
        System.out.println(DELETE_TRANSACTION);
        System.out.println(id);
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRANSACTION);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            AddUserController.infoBox("Transaction Deleted", "Transaction Deleted", "Alert");
            return true;
        } catch (SQLiteException e) {
            AddUserController.infoBox("Not Deleted", "Delete Attempt", "Alert");
        }
        return false;
    }

    public boolean editUser(User user, String key) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatementUser = connection.prepareStatement(EDIT_USER_USERS);
            PreparedStatement preparedStatementLogin = connection.prepareStatement(EDIT_USER_LOGIN);

            preparedStatementUser.setString(1, user.getUserName());
            preparedStatementUser.setString(2, user.getUserFirstName());
            preparedStatementUser.setString(3, user.getUserLastName());
            preparedStatementUser.setString(4, user.getUserEmail());
            preparedStatementUser.setString(5, user.getUserAddress());
            preparedStatementUser.setString(6, key);

            preparedStatementLogin.setString(1, user.getUserName());
            preparedStatementLogin.setString(2, user.getUserPassword());
            preparedStatementLogin.setString(3, user.getUserAccess());
            preparedStatementLogin.setString(4, key);

            preparedStatementUser.executeUpdate();
            preparedStatementLogin.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public int editTransactionforSale(Transaction transaction, int amountSold) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        System.out.println(EDIT_TRANSACTION_FOR_SALE);
        System.out.println(transaction.getId());
        int amount = transaction.getProductOnHand() - amountSold;
        System.out.println(amount);
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(EDIT_TRANSACTION_FOR_SALE);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, transaction.getId());
            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int addToTransactionLog() throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        Date date = new Date();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_TRANSACTION_LOG, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, signedInUser);
            preparedStatement.setString(2, date.toString());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Controller.infoBox("Successful Insertion", "Success", "Alert");
                return resultSet.getInt(1);

            }
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful Insertion", "Error", "Alert");
        }
        return -1;
    }

    public void addToTypeOfProduct(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((ADD_TO_TYPE_OF_PRODUCT));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful Insertion", "Product not Inserted", "Alert");
        }
    }

    public void deleteTypeOfProduct(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((DELETE_PRODUCT_TYPE));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful deletion", "Failure to Delete", "Alert");
        }
    }

    public void addToHarvestLocations(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((ADD_TO_HARVEST_LOCATIONS));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
            Controller.infoBox("Successful Insertion", "Success", "Alert");
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful Insertion", "Product not Inserted", "Alert");
        }
    }

    public void deleteHarvestLocations(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((DELETE_HARVEST_LOCATION));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful deletion", "Failure to Delete", "Alert");
        }
    }


    public void addToOriginalHarvestNumber(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((ADD_TO_ORIGINAL_HARVEST_NUMBER));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Successful Insertion", "Success", "Alert");
        }
    }

    public void deleteOriginalHarvestNumber(String type) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((DELETE_ORGINAL_HARVEST_NUMBER));
            preparedStatement.setString(1, type);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful deletion", "Failure to Delete", "Alert");
        }
    }

    public void addToClients(Client client) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((ADD_TO_CLIENTS));
            preparedStatement.setString(1, client.getClientName());
            preparedStatement.setString(2, client.getAddress());
            preparedStatement.setString(3, client.getContactInfo());
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Successful Insertion", "Success", "Alert");
        }
    }

    public void deleteClients(String name) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((DELETE_CLIENT));
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful deletion", "Failure to Delete", "Alert");
            e.printStackTrace();
        }
    }

    public void editClient(Client client, String name) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((EDIT_CLIENT));
            preparedStatement.setString(1, client.getClientName());
            preparedStatement.setString(2, client.getAddress());
            preparedStatement.setString(3, client.getContactInfo());
            preparedStatement.setString(4, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Controller.infoBox("Unsuccessful deletion", "Failure to Delete", "Alert");
        }
    }

    public Client selectClient(String name) throws SQLException, ClassNotFoundException {
        String query = "SELECT * FROM main.clients where client_name = '" + name + "'";
        ArrayList<String> client = new ArrayList<>();
        DBConnection database = new DBConnection();
        //Connection connection = database.getConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement((query));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                client.add(rs.getString("client_name"));
                client.add(rs.getString("address"));
                client.add(rs.getString("contact_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Client(client.get(0), client.get(1), client.get(2));
    }

    public boolean addToIncoming(Transaction transaction, int row) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_INCOMING);
            preparedStatement.setInt(1, row);
            preparedStatement.setString(2, transaction.getClient());
            preparedStatement.setInt(3, transaction.getNumberOfUnits());
            preparedStatement.setInt(4, transaction.getCountPerUnit());
            preparedStatement.setString(5, transaction.getHarvestDate());
            preparedStatement.setString(6, transaction.getHarvestLocation());
            preparedStatement.setString(7, transaction.getOriginalHarvestNumber());
            preparedStatement.setInt(8, transaction.getNumberOfUnits());
            preparedStatement.setString(9, transaction.getShippingDate());
            preparedStatement.setInt(10, transaction.getTemperature());
            preparedStatement.setString(11, transaction.getTypeOfShellfish());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            Controller.infoBox("Insert not executed", "Transaction Insert to Incoming Log", "Alert");
        }
        return false;
    }

    public boolean addToOutGoing(Transaction transaction, int row, int id_received) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_OUTGOING);
            preparedStatement.setInt(1, row);
            preparedStatement.setString(2, transaction.getClient());
            preparedStatement.setInt(3, transaction.getNumberOfUnits());
            preparedStatement.setString(4, transaction.getShippingDate());
            preparedStatement.setInt(5, id_received);
            preparedStatement.setInt(6, transaction.getTemperature());
            preparedStatement.setString(7, transaction.getTypeOfShellfish());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            Controller.infoBox("Insert not executed", "Transaction Insert to Outgoing Log", "Alert");
            e.printStackTrace();
        }
        return false;
    }

    public String[] getTimeEnteredAndTimeStamp(int id) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        String[] timeAndEnterby = new String[2];
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_TRANSACTION_LOG);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                timeAndEnterby[0] = rs.getString("entered_by");
                timeAndEnterby[1] = rs.getString("time_stamp");
                return timeAndEnterby;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Controller.infoBox("Transaction Not Found", "Transaction Query", "Alert");
            return new String[]{"N/A", "N/A"};
        }
        return null;
    }

    public String[] getHarvestLocationAndHarvestNumber(int id) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        String[] harvestLocationAndNumber = new String[4];
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_INCOMING_LOG);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                harvestLocationAndNumber[0] = rs.getString("harvest_location");
                harvestLocationAndNumber[1] = rs.getString("original_harvest_number");
                harvestLocationAndNumber[2] = rs.getString("harvest_date");
                harvestLocationAndNumber[3] = String.valueOf(rs.getInt("count_in_units"));
                return harvestLocationAndNumber;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Controller.infoBox("Incoming Transaction ID " + id + " Not Found ", "Transaction Query", "Alert");
            return new String[]{"N/A", "N/A", "N/A", "N/A"};
        } catch (NullPointerException e) {
            Controller.infoBox("Incoming Transaction ID " + id + " Not Found ", "Transaction Query", "Alert");
            return new String[]{"N/A", "N/A", "N/A", "N/A"};
        }
        return null;
    }

    public void editOutgoingTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(EDIT_OUTGOING_TRANSACTION);
            preparedStatement.setString(1, transaction.getClient());
            preparedStatement.setInt(2, transaction.getNumberOfUnits());
            preparedStatement.setString(3, transaction.getShippingDate());
            preparedStatement.setInt(4, transaction.getProductOnHand());
            preparedStatement.setString(5, transaction.getTypeOfShellfish());
            preparedStatement.setInt(6, transaction.getTemperature());
            preparedStatement.setInt(7, transaction.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Controller.infoBox("Transaction Not Edited", "Transaction Edit", "Alert");
        }
    }

    public void editIncomingTransaction(Transaction transaction) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(EDIT_INCOMING_TRANSACTION);
            preparedStatement.setString(1, transaction.getClient());
            preparedStatement.setInt(2, transaction.getNumberOfUnits());
            preparedStatement.setInt(3, transaction.getCountPerUnit());
            preparedStatement.setString(4, transaction.getHarvestDate());
            preparedStatement.setString(5, transaction.getHarvestLocation());
            preparedStatement.setString(6, transaction.getOriginalHarvestNumber());
            preparedStatement.setInt(7, transaction.getProductOnHand());
            preparedStatement.setString(8, transaction.getShippingDate());
            preparedStatement.setString(9, transaction.getTypeOfShellfish());
            preparedStatement.setInt(10, transaction.getTemperature());
            preparedStatement.setInt(11, transaction.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Controller.infoBox("Transaction Not Edited", "Transaction Edit", "Alert");
            e.printStackTrace();
        }
    }

    public Transaction getIncomingTransaction(int id) throws SQLException, ClassNotFoundException {

        String query = "SELECT * from main.incoming_transaction WHERE ID = ?";
        DBConnection database = new DBConnection();
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                connection.close();
                return new Transaction(resultSet.getInt("ID"), resultSet.getString("client_name"), resultSet.getString("original_harvest_number"), resultSet.getString("type_of_shellfish"), 0, 0, null, null, 0, null, null, null, null, resultSet.getInt("product_on_hand"));
            }
        } catch (SQLException e) {
            Controller.infoBox("Incoming Transaction Not Adjusted For Edit", "Transaction Edit", "Alert");
        }
        return null;
    }

    public boolean passwordChange(String userName, String password) throws SQLException, ClassNotFoundException {
        DBConnection database = new DBConnection();
        String getUserEmail = "SELECT * FROM main.users WHERE user_name = ?";
        String emailAddress = "";
        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement1 = connection.prepareStatement(getUserEmail);
            preparedStatement1.setString(1, userName);
            ResultSet resultSet = preparedStatement1.executeQuery();
            if (resultSet.next()) {
                emailAddress = resultSet.getString("email");
                PreparedStatement preparedStatement = connection.prepareStatement(EDIT_PASSWORD);
                preparedStatement.setString(1, password);
                preparedStatement.setString(2, userName);
                preparedStatement.executeUpdate();
                Controller.infoBox("Password Changed", "Password", "Alert");
                Email email = new Email();
                email.sendEmail(emailAddress, "Your Shelltrack password has been changed", "Your Shelltrack password has been changed! If this wasn't you, the " +
                        "who else would it be?");
            }else{
                Controller.infoBox("User name not found", "Forgot Password", "Alert");
            }

            return true;
        } catch (SQLException e) {
            Controller.infoBox("User name not found", "Forgot Password", "Alert");
        }
        return false;
    }

}

