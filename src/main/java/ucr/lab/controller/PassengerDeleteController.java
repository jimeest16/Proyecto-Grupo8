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
import ucr.lab.TDA.list.ListException; // Importa ListException
import ucr.lab.TDA.list.SinglyLinkedList; // Importa SinglyLinkedList
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException;
import java.util.ArrayList; // Necesario para el método auxiliar convertSinglyLinkedListToList
import java.util.List;      // Necesario para el método auxiliar convertSinglyLinkedListToList

public class PassengerDeleteController {

    @FXML
    private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput;

    private AVLTree avlTree = new AVLTree();

    @FXML
    public void initialize() {
        try {
            // Cargar pasajeros como SinglyLinkedList
            SinglyLinkedList passengers = FileReader.loadPassengers(); // FileReader ahora devuelve SinglyLinkedList

            // Iterar sobre SinglyLinkedList y agregar al avlTree (de IDs)
            for (int i = 1; i <= passengers.size(); i++) { // Las SinglyLinkedLists suelen ser 1-indexed
                Passenger p = (Passenger) passengers.getNode(i).data;
                avlTree.add(p.getId());
            }
        } catch (ListException e) { // Captura ListException si getNode() o size() la lanzan
            getMessageText("Error al cargar pasajeros desde SinglyLinkedList (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (TreeException e) { // Captura TreeException si avlTree.add() la lanza
            getMessageText("Error al agregar ID de pasajero al árbol AVL (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error inesperado al cargar pasajeros en el árbol (initialize): " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeletePassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Verificar si existe el ID en el AVL
            if (!avlTree.contains(id)) {
                getMessageText("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
                return;
            }

            // Cargar pasajeros como SinglyLinkedList
            SinglyLinkedList passengers = FileReader.loadPassengers();

            boolean removed = false;

            Passenger passengerToRemove = null;
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    passengerToRemove = p;
                    // passengers.remove(i);
                    passengers.remove(passengerToRemove);
                    removed = true;
                    break;
                }
            }


            if (removed) {

                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));

                // Remover el ID del árbol AVL
                avlTree.remove(id);

                getMessageText("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {
                getMessageText("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
            }

        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            getMessageText("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { // Captura ListException si SinglyLinkedList.remove() o getNode() fallan
            getMessageText("Error al operar en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            getMessageText("Error al eliminar el pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Usar AVL para verificar si existe (búsqueda rápida por ID)
            if (!avlTree.contains(id)) {
                getMessageText("No se encontró pasajero con ID: " + id + "\n");
                return;
            }

            // Cargar pasajeros como SinglyLinkedList para obtener el objeto completo
            SinglyLinkedList passengers = FileReader.loadPassengers();
            Passenger foundPassenger = null;

            // Iterar sobre SinglyLinkedList para encontrar el pasajero por ID
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    foundPassenger = p;
                    break;
                }
            }

            if (foundPassenger != null) {
                getMessageText("Pasajero encontrado: " + foundPassenger + "\n");
                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());

                txtHistory.setText(String.valueOf(foundPassenger.getFlightHistory()));
            } else {
                getMessageText("No se encontró pasajero con ID: " + id + " (posible inconsistencia).\n");
            }

        } catch (NumberFormatException e) {
            getMessageText("Ingrese un ID válido.\n");
        } catch (ListException e) { // Captura ListException si getNode() o size() fallan
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
            SinglyLinkedList passengers = FileReader.loadPassengers(); // FileReader ahora devuelve SinglyLinkedList

            if (passengers.isEmpty()) {
                getMessageText("No hay pasajeros registrados.\n");
            } else {
                getMessageText("=== Lista de Pasajeros ===");
                // Iterar sobre SinglyLinkedList para mostrar los pasajeros
                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger p = (Passenger) passengers.getNode(i).data;
                    getMessageText(p.toString());
                }
            }
        } catch (ListException e) { // Captura ListException si getNode() o size() fallan
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