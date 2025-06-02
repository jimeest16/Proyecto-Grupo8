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
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.List;

public class PassengerAddController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;

    @FXML
    public void handleAddPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            List<Passenger> passengers = FileReader.loadPassengers();


            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                    return;
                }
            }

            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
                passenger.addFlight(history);
            }

            passengers.add(passenger);
            FileReader.savePassengers(passengers);

            appendOutput("Pasajero agregado: " + passenger + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número válido.\n");
        } catch (Exception e) {
            appendOutput("Error al agregar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            List<Passenger> passengers = FileReader.loadPassengers();

            for (Passenger p : passengers) {
                if (p.getId() == id) {
                    appendOutput("Pasajero encontrado: " + p + "\n");
                    return;
                }
            }
            appendOutput("No se encontró pasajero con ID: " + id + "\n");
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (Exception e) {
            appendOutput("Error al buscar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleListPassengers() {
        List<Passenger> passengers = FileReader.loadPassengers();

        if (passengers.isEmpty()) {
            appendOutput("No hay pasajeros registrados.\n");
        } else {
            appendOutput("=== Lista de Pasajeros ===");
            for (Passenger p : passengers) {
                appendOutput(p.toString());
            }
        }
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();
        txtHistory.clear();
    }

    private void appendOutput(String text) {
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
