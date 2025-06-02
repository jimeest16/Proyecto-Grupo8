package ucr.lab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.util.List;

public class PassengerController {

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

            // Verificar duplicado por ID
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
}
