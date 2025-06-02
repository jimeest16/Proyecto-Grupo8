package ucr.lab.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    public void handleLoginUser(ActionEvent event) {
        cargarLoginConRol(event, "user");
    }

    @FXML
    public void handleLoginAdmin(ActionEvent event) {
        cargarLoginConRol(event, "administrator");
    }
    private static void cargarLoginConRol(ActionEvent event, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/ucr/lab/LoginView.fxml"));
            Parent root = loader.load();


            LoginController loginController = loader.getController();
            loginController.setRolEscogido(rol);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión - " + rol);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exitButton(ActionEvent actionEvent) {
        Platform.exit();
    }
}
