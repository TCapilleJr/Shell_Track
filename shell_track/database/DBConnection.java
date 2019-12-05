package shell_track.database;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteException;
import org.sqlite.SQLiteJDBCLoader;
import shell_track.view.Controller;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class DBConnection {
    /**
     * Connection to Database
     */
    private Connection connection;
    /**
     * Database URl
     */
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";

//    private static final String DATABASE_URL = "jdbc:sqlite:/Users/timothycapille/Shell_Track/sqlite/shelltrack.db";

    /**
     * Database username -- used for MySql connection
     */
    private static final String DATABASE_USERNAME = "root";
    /**
     * Database password -- used for MySql connection
     */
    private static final String DATABASE_PASSWORD = "rootrootroot";


    /**
     * Uses try catch statement to create connection. Creates a statement using connection object.
     * Sets user access to that of related column
     *
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {

        try {
//            SQLiteConfig config = new SQLiteConfig();
//            config.enforceForeignKeys(true);
//            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            Controller.infoBox("No Database Connection","Error","Alert");
        }
        return connection;
    }
}
