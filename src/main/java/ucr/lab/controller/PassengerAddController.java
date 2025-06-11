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

public class PassengerAddController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;

    //USO DE AVLTree: se esta utilizando para manjear los ID"S de los pasajeros
    // en el metodo initialize se cargan todos lsos pasajeros desde el archivo y inserta cada pasajero por su ID
    // primero verifica si el ID ya existe con constains
    // de igual forma con search porque primero se busca segun su ID

    //Este árbol AVL sólo guarda los IDs (enteros).
    // Los datos completos de cada pasajero siguen guardándose en la lista que se carga y guarda en el archivo.
    // El árbol sirve para hacer búsquedas y evitar duplicados.

    private AVLTree avlTree = new AVLTree();

    @FXML
    public void initialize() {
        try {
            List<Passenger> passengers = FileReader.loadPassengers();
            for (Passenger p : passengers) {
                avlTree.add(p.getId());
            }
        } catch (Exception e) {
            appendOutput("Error al cargar pasajeros en el árbol: " + e.getMessage());
        }
    }

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

            // Verificar si existe en AVL
            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            // Crear pasajero, guardar en archivo
            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
               // passenger.addFlight(history);
            }

            List<Passenger> passengers = FileReader.loadPassengers();
            passengers.add(passenger);
            FileReader.savePassengers(passengers);

            // Agregar ID al AVL
            avlTree.add(id);

            appendOutput("Pasajero agregado: " + passenger + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al agregar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + "\n");
                return;
            }
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
