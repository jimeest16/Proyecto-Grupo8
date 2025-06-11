package ucr.lab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private static BorderPane root = new BorderPane();
    public static BorderPane getRoot() {
    return root;
    }

    @Override

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ucr/lab/MainView.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}