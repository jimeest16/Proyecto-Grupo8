package ucr.lab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.util.ArrayList;
import java.util.List;

public class passengerController {
    @FXML
    private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput; // del user

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
        } catch (TreeException e) {
            appendOutput("Error al agregar ID de pasajero al árbol AVL (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al cargar pasajeros en el árbol (initialize): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

    @FXML
    private void addUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            // Verify if ID exists in AVL
            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            // Create passenger
            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
                passenger.addFlight(history);
            }


            SinglyLinkedList passengers = FileReader.loadPassengers();
            passengers.add(passenger); // Add to SinglyLinkedList

            FileReader.savePassengers(convertSinglyLinkedListToList(passengers));

            // Add ID to AVL
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
    private void editUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            // Load passengers as SinglyLinkedList
            SinglyLinkedList passengers = FileReader.loadPassengers();
            boolean modified = false;


            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
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

                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));
                appendOutput("Pasajero con ID " + id + " modificado exitosamente.\n");
                clearFields();
            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al modificar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());


            if (!avlTree.contains(id)) {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
                return;
            }


            SinglyLinkedList passengers = FileReader.loadPassengers();

            boolean removed = false;

            Passenger passengerToRemove = null;
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    passengerToRemove = p;
                    passengers.remove(passengerToRemove);
                    removed = true;
                    break;
                }
            }

            if (removed) {

                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));
                // Remove the ID from the AVL tree
                avlTree.remove(id);
                appendOutput("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { // Catch ListException if SinglyLinkedList.remove() or getNode() fail
            appendOutput("Error al operar en SinglyLinkedList: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error al eliminar el pasajero: " + e.getMessage() + "\n");
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

                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());
                txtHistory.setText(String.valueOf(foundPassenger.getFlightHistory()));
            } else {
                appendOutput("No se encontró pasajero con ID: " + id + " (posible inconsistencia).\n");
            }
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (ListException e) {
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


    private List<Passenger> convertSinglyLinkedListToList(SinglyLinkedList singlyLinkedList) throws ListException {
        List<Passenger> list = new ArrayList<>();
        if (singlyLinkedList != null && !singlyLinkedList.isEmpty()) {
            for (int i = 1; i <= singlyLinkedList.size(); i++) {
                list.add((Passenger) singlyLinkedList.getNode(i).data);
            }
        }
        return list;
    }
}