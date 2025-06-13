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
import ucr.lab.TDA.list.ListException; // Import ListException
import ucr.lab.TDA.list.SinglyLinkedList; // Import SinglyLinkedList
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.ArrayList; // Needed for the convertSinglyLinkedListToList helper method
import java.util.List;      // Needed for the convertSinglyLinkedListToList helper method

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

            SinglyLinkedList passengers = FileReader.loadPassengers();


            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                avlTree.add(p.getId());
            }
        } catch (ListException e) {
            getMessageText("Error al cargar pasajeros desde SinglyLinkedList (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (TreeException e) {
            getMessageText("Error al agregar ID de pasajero al árbol AVL (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error inesperado al cargar pasajeros en el árbol (initialize): " + e.getMessage());
            e.printStackTrace();
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

            // Verify if ID exists in AVL
            if (!avlTree.contains(id)) {
                getMessageText("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }


            SinglyLinkedList passengers = FileReader.loadPassengers();
            boolean modified = false;


            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);

                    if (!history.isEmpty()) {
                        p.clearFlightHistory();
                        //p.addFlight(history);
                    }
                    modified = true;

                    break;
                }
            }

            if (modified) {

                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));
                getMessageText("Pasajero con ID " + id + " modificado exitosamente.\n");
                clearFields();
            } else {
                getMessageText("No se encontró pasajero con ID: " + id + " para modificar.\n");
            }

        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            getMessageText("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { // Catch ListException if getNode() or size() throw it
            getMessageText("Error al iterar pasajeros en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error al modificar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Use AVL to quickly check for existence
            if (!avlTree.contains(id)) {
                getMessageText("No se encontró pasajero con ID: " + id + "\n");
                return;
            }


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
                getMessageText("Pasajero encontrado: " + foundPassenger + "\n");
                // Optional: Populate fields with found passenger's data
                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());
                txtHistory.setText(String.valueOf(foundPassenger.getFlightHistory()));
            } else {
                getMessageText("No se encontró pasajero con ID: " + id + " (posible inconsistencia).\n");
            }
        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (ListException e) { // Catch ListException if getNode() or size() throw it
            getMessageText("Error al buscar pasajero en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error al buscar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleListPassengers() {
        try {
            SinglyLinkedList passengers = FileReader.loadPassengers();

            if (passengers.isEmpty()) {
                getMessageText("No hay pasajeros registrados.\n");
            } else {
                getMessageText("=== Lista de Pasajeros ===");

                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger p = (Passenger) passengers.getNode(i).data;
                    getMessageText(p.toString());
                }
            }
        } catch (ListException e) {
            getMessageText("Error al listar pasajeros desde SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error al listar pasajeros: " + e.getMessage() + "\n");
            e.printStackTrace();
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