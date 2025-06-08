package ucr.lab.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucr.lab.TDA.AVLTree;
import ucr.lab.TDA.BTreeNode;
import ucr.lab.TDA.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.util.List;

public class AdminController {

    @FXML private Button btnLogout;
    @FXML private TextField txtPassengerId;
    @FXML private TextField txtPassengerName;
    @FXML private TextField txtPassengerNationality;
    @FXML private TextField txtPassengerFlightHistory;
    @FXML private TextArea txtAreaOutput;

    private AVLTree passengerTree;

    public AdminController() {
        passengerTree = new AVLTree();
        loadPassengers();

    }

    private void loadPassengers() {
        List<Passenger> passengers = FileReader.loadPassengers();
        for (Passenger p : passengers) {
            try {
                passengerTree.add(p);
            } catch (TreeException e) {
                e.printStackTrace();
                appendOutput("Error cargando pasajero: " + p);
            }
        }
        appendOutput("Pasajeros cargados: " + passengers.size());
    }

    private void appendOutput(String text) {
        if (txtAreaOutput != null) {
            txtAreaOutput.appendText(text + "\n");
        }
    }


    @FXML
    private void addUser () {
        // una nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/PassengerViewAdd.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Información del Pasajero/Usuario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para agregar usuario: " + e.getMessage());
        }
    }
    @FXML
    private void editUser () {
        // una nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/PassengerViewEdit.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Información del Pasajero/Usuario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void deleteUser () {
        // una nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/PassengerDeleteView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Información del Pasajero/Usuario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para agregar usuario: " + e.getMessage());
        }
    }

    @FXML
    private void airportManager () {
        // una nueva pestaña
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AirPortView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Airports Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para agregar usuario: " + e.getMessage());
        }
    }
    @FXML
    private void logout() {
        Platform.exit();
    }
}
