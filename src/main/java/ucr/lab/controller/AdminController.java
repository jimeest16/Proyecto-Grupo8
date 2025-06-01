package ucr.lab.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
        List<Passenger> passengers = FileReader.loadPassenger();
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
    private void addPassenger() {
        try {
            String idText = txtPassengerId.getText();
            if (idText == null || idText.isEmpty()) {
                appendOutput("Ingrese un ID válido");
                return;
            }
            int id = Integer.parseInt(idText.trim());

            String name = txtPassengerName.getText();
            String nationality = txtPassengerNationality.getText();
            String flightHistory = txtPassengerFlightHistory.getText();

            if (name == null || name.isEmpty()) {
                appendOutput("Ingrese un nombre válido");
                return;
            }
            if (nationality == null) nationality = "";
            if (flightHistory == null) flightHistory = "";

            Passenger newPassenger = new Passenger(id, name, nationality);

            if (passengerTree.contains(newPassenger)) {
                appendOutput("Pasajero con ID " + id + " ya existe.");
                return;
            }

            passengerTree.add(newPassenger);
            FileReader.addPassenger(newPassenger);

            appendOutput("Pasajero agregado exitosamente: " + newPassenger.toString());
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número entero válido.");
        } catch (TreeException e) {
            e.printStackTrace();
            appendOutput("Error al agregar pasajero: " + e.getMessage());
        }
    }

    @FXML
    private void searchPassenger() {
        try {
            String idText = txtPassengerId.getText();
            if (idText == null || idText.isEmpty()) {
                appendOutput("Ingrese un ID válido");
                return;
            }
            int id = Integer.parseInt(idText.trim());

            Passenger searchKey = new Passenger(id, "", "");
            BTreeNode foundNode = passengerTree.search(searchKey);
            if (foundNode != null) {
                Passenger foundPassenger = (Passenger) foundNode.data;
                appendOutput("Pasajero encontrado: " + foundPassenger.toString());
            } else {
                appendOutput("Pasajero con ID " + id + " no encontrado.");
            }
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número entero válido.");
        } catch (TreeException e) {
            e.printStackTrace();
            appendOutput("Error buscando pasajero: " + e.getMessage());
        }
    }

    @FXML
    private void showFlightHistory() {
        try {
            String idText = txtPassengerId.getText();
            if (idText == null || idText.isEmpty()) {
                appendOutput("Ingrese un ID válido");
                return;
            }
            int id = Integer.parseInt(idText.trim());

            Passenger searchKey = new Passenger(id, "", "");
            BTreeNode foundNode = passengerTree.search(searchKey);
            if (foundNode != null) {
                Passenger foundPassenger = (Passenger) foundNode.data;
                appendOutput("Historial de vuelo para " + foundPassenger.getName() + ": " + foundPassenger.getFlightHistory());
            } else {
                appendOutput("Pasajero con ID " + id + " no encontrado.");
            }
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número entero válido.");
        } catch (TreeException e) {
            e.printStackTrace();
            appendOutput("Error mostrando historial: " + e.getMessage());
        }
    }

    @FXML
    private void addFlight() {
        System.out.println("Agregar vuelo...");
        // Implementar lógica para agregar vuelo
    }

    @FXML
    private void editFlight() {
        System.out.println("Modificar vuelo...");
        // Implementar lógica para modificar vuelo
    }

    @FXML
    private void deleteFlight() {
        System.out.println("Eliminar vuelo...");
        // Implementar lógica para eliminar vuelo
    }

    @FXML
    private void addUser () {
        try {
            String idText = txtPassengerId.getText();
            if (idText == null || idText.isEmpty()) {
                appendOutput("Ingrese un ID válido");
                return;
            }
            int id = Integer.parseInt(idText.trim());

            String name = txtPassengerName.getText();
            String nationality = txtPassengerNationality.getText();
            String flightHistory = txtPassengerFlightHistory.getText();

            if (name == null || name.isEmpty()) {
                appendOutput("Ingrese un nombre válido");
                return;
            }
            if (nationality == null) nationality = "";
            if (flightHistory == null) flightHistory = "";

            Passenger newPassenger = new Passenger(id, name, nationality);

            if (passengerTree.contains(newPassenger)) {
                appendOutput("Pasajero con ID " + id + " ya existe.");
                return;
            }

            passengerTree.add(newPassenger);
            FileReader.addPassenger(newPassenger);

            appendOutput("Pasajero agregado exitosamente: " + newPassenger.toString());
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número entero válido.");
        } catch (TreeException e) {
            e.printStackTrace();
            appendOutput("Error al agregar pasajero: " + e.getMessage());
        }







    }

    @FXML
    private void editUser () {
        System.out.println("Modificar usuario...");
        // Implementar lógica para modificar usuario
    }

    @FXML
    private void deleteUser () {
        System.out.println("Eliminar usuario...");
        // Implementar lógica para eliminar usuario
    }

    @FXML
    private void logout() {
        Platform.exit();
    }
}
