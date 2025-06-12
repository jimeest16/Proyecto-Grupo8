package ucr.lab.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.List;

public class PassengerEditController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;

    private AVLTree avlTree = new AVLTree();

    @FXML
    public void initialize() {
        try {
            List<Passenger> passengers = FileReader.loadPassengers();
            for (Passenger p : passengers) {
                avlTree.add(p.getId());
            }
        } catch (Exception e) {
            getMessageText("Error al cargar pasajeros en el árbol: " + e.getMessage());
        }
    }

    @FXML
    public void handleModifyPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                getMessageText("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (!avlTree.contains(id)) {
                getMessageText("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            List<Passenger> passengers = FileReader.loadPassengers();
            boolean modified = false;

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);

                    if (!history.isEmpty()) {
                        p.clearFlightHistory();
                        p.addFlight(history);
                    }
                    modified = true;
                    break;
                }
            }

            if (modified) {
                FileReader.savePassengers(passengers);
                getMessageText("Pasajero con ID " + id + " modificado exitosamente.\n");
                clearFields();
            } else {
                getMessageText("No se encontró pasajero con ID: " + id + " para modificar.\n");
            }

        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            getMessageText("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            getMessageText("Error al modificar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            if (!avlTree.contains(id)) {
                getMessageText("No se encontró pasajero con ID: " + id + "\n");
                return;
            }

            List<Passenger> passengers = FileReader.loadPassengers();

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    getMessageText("Pasajero encontrado: " + p + "\n");
                    return;
                }
            }
            getMessageText("No se encontró pasajero con ID: " + id + "\n");
        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (Exception e) {
            getMessageText("Error al buscar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleListPassengers() {
        try {
            List<Passenger> passengers = FileReader.loadPassengers();

            if (passengers.isEmpty()) {
                getMessageText("No hay pasajeros registrados.\n");
            } else {
                getMessageText("=== Lista de Pasajeros ===");
                for (Passenger p : passengers) {
                    getMessageText(p.toString());
                }
            }
        } catch (Exception e) {
            getMessageText("Error al listar pasajeros: " + e.getMessage() + "\n");
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();
        txtHistory.clear();
    }

    private void getMessageText(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

    @FXML
    public void accionRetroceder(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AdministratorView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio de Sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla principal.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
