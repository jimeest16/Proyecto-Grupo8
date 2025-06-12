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
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.ArrayList; // Needed for the convertSinglyLinkedListToList helper method
import java.util.List;      // Needed for the convertSinglyLinkedListToList helper method

public class PassengerAddController {

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;

    // USO DE AVLTree: se esta utilizando para manejar los ID'S de los pasajeros
    // en el metodo initialize se cargan todos los pasajeros desde el archivo y inserta cada pasajero por su ID
    // primero verifica si el ID ya existe con constains
    // de igual forma con search porque primero se busca segun su ID

    // Este árbol AVL sólo guarda los IDs (enteros).
    // Los datos completos de cada pasajero siguen guardándose en la lista que se carga y guarda en el archivo.
    // El árbol sirve para hacer búsquedas y evitar duplicados.

    private AVLTree avlTree = new AVLTree();

    @FXML
    public void initialize() {
        try {

            SinglyLinkedList passengers = FileReader.loadPassengers();


            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                avlTree.add(p.getId());
            }
        } catch (ListException e) {
            appendOutput("Error al cargar pasajeros desde SinglyLinkedList (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (TreeException e) { // Catch TreeException if avlTree.add() throws it
            appendOutput("Error al agregar ID de pasajero al árbol AVL (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al cargar pasajeros en el árbol (initialize): " + e.getMessage());
            e.printStackTrace();
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


            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }


            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
                passenger.addFlight(history);
            }

            SinglyLinkedList passengers = FileReader.loadPassengers();
            passengers.add(passenger); // Add to SinglyLinkedList

            FileReader.savePassengers(convertSinglyLinkedListToList(passengers));


            avlTree.add(id);

            appendOutput("Pasajero agregado: " + passenger + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { // Catch ListException if SinglyLinkedList.add() throws it
            appendOutput("Error al añadir pasajero a SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al agregar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
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

            // Load passengers as SinglyLinkedList to get the full passenger object
            SinglyLinkedList passengers = FileReader.loadPassengers();
            Passenger foundPassenger = null;


            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    foundPassenger = p;
                    break;
                }
            }

            if (foundPassenger != null) {
                appendOutput("Pasajero encontrado: " + foundPassenger + "\n");
                // Optional: Populate fields with found passenger's data
                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());
                txtHistory.setText(String.valueOf(foundPassenger.getFlightHistory()));
            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " (posible inconsistencia).\n");
            }
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (ListException e) { // Catch ListException if getNode() or size() throw it
            appendOutput("Error al buscar pasajero en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al buscar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }


    @FXML
    public void handleListPassengers() {
        try {
            SinglyLinkedList passengers = FileReader.loadPassengers();

            if (passengers.isEmpty()) {
                appendOutput("No hay pasajeros registrados.\n");
            } else {
                appendOutput("=== Lista de Pasajeros ===");

                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger p = (Passenger) passengers.getNode(i).data;
                    appendOutput(p.toString());
                }
            }
        } catch (ListException e) {
            appendOutput("Error al listar pasajeros desde SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al listar pasajeros: " + e.getMessage() + "\n");
            e.printStackTrace();
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


    private List<Passenger> convertSinglyLinkedListToList(SinglyLinkedList singlyLinkedList) throws ListException {
        List<Passenger> list = new ArrayList<>();
        if (singlyLinkedList != null && !singlyLinkedList.isEmpty()) {
            for (int i = 1; i <= singlyLinkedList.size(); i++) {
                list.add((Passenger) singlyLinkedList.getNode(i).data);
            }
        }
        return list;
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