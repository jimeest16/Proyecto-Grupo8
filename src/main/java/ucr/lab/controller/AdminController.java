package ucr.lab.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminController {

    @FXML private Button btnLogout;

    @FXML
    private void addFlight() {
        System.out.println("Agregar vuelo...");

    }

    @FXML
    private void editFlight() {
        System.out.println("Modificar vuelo...");

    }

    @FXML
    private void deleteFlight() {
        System.out.println("Eliminar vuelo...");

    }

    @FXML
    private void addUser() {
        System.out.println("Agregar usuario...");

    }

    @FXML
    private void editUser() {
        System.out.println("Modificar usuario...");

    }

    @FXML
    private void deleteUser() {
        System.out.println("Eliminar usuario...");

    }

    @FXML
    private void logout() {
        Platform.exit();
    }
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/view/Login.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) btnLogout.getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Login - Sistema de Reservas");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
