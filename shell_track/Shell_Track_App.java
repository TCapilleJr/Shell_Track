package shell_track;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Shell_Track_App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
            Parent root = FXMLLoader.load(getClass().getResource("view/login_menu.fxml"));
            primaryStage.setTitle("Shell Track");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}

