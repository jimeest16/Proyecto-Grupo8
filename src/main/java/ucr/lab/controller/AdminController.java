package ucr.lab.controller;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.BTreeNode;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.util.ArrayList; 
import java.util.List;     

public class AdminController {

    private AVLTree passengerTree;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextField txtHistory;
    @FXML private TextArea txtOutput; // del user

    private AVLTree avlTree = new AVLTree();


    public AdminController() {
        passengerTree = new AVLTree();
        loadPassengers();
    }

    @FXML
    public void initialize() {
       
        if (avlTree == null) avlTree = new AVLTree();
        if (passengerTree == null) passengerTree = new AVLTree();

        try {
           
            SinglyLinkedList passengers = FileReader.loadPassengers(); 

           
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data; 
                avlTree.add(p.getId()); 
            }
        } catch (ListException e) { 
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList (initialize): " + e.getMessage());
            e.printStackTrace(); 
        } catch (TreeException e) { 
            appendOutput("Error al agregar ID de pasajero al árbol AVL (initialize): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { 
            appendOutput("Error inesperado al cargar pasajeros en el árbol (initialize): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPassengers() {
        // Cargar pasajeros como SinglyLinkedList
        SinglyLinkedList passengers = FileReader.loadPassengers();
        int passengerCount = 0;

        try {
            
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data; 
                try {
                    passengerTree.add(p); 
                    passengerCount++; 
                } catch (TreeException e) {
                    e.printStackTrace();
                    appendOutput("Error cargando pasajero en passengerTree: " + p.getName() + " (ID: " + p.getId() + ")");
                }
            }
            appendOutput("Pasajeros cargados en passengerTree: " + passengerCount); 
        } catch (ListException e) { // Captura ListException si getNode() o size() la lanzan
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList (loadPassengers): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Captura cualquier otra excepción inesperada
            appendOutput("Error inesperado al cargar pasajeros en passengerTree: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

    @FXML
    private void addUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            // Verificar si existe en AVL (avlTree guarda IDs)
            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            // Crear pasajero
            Passenger passenger = new Passenger(id, name, nationality);
            if (!history.isEmpty()) {
                passenger.addFlight(history);
            }

            // Cargar pasajeros desde FileReader (ahora devuelve SinglyLinkedList)
            SinglyLinkedList passengers = FileReader.loadPassengers();
            passengers.add(passenger); // Añadir al SinglyLinkedList

            
            FileReader.savePassengers(convertSinglyLinkedListToList(passengers));


            // Agregar ID al AVL (avlTree de IDs) y objeto Passenger al passengerTree
            avlTree.add(id);
            passengerTree.add(passenger); // Añadir al árbol de objetos Passenger

            appendOutput("Pasajero agregado: " + passenger + "\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID debe ser un número válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { // Captura si SinglyLinkedList.add() falla
            appendOutput("Error al añadir pasajero a SinglyLinkedList: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al agregar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void editUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();
            String history = txtHistory.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            // Verificar si existe el ID en el AVL
            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            // Cargar pasajeros como SinglyLinkedList
            SinglyLinkedList passengers = FileReader.loadPassengers();
            boolean modified = false;

            // Iterar sobre SinglyLinkedList para encontrar y modificar el pasajero
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.getNode(i).data;
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);

                    if (!history.isEmpty()) {
                        p.clearFlightHistory(); // Limpia el historial existente
                        p.addFlight(history);   // Añade el nuevo vuelo
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
        } catch (ListException e) { // Captura si SinglyLinkedList.getNode() o size() fallan
            appendOutput("Error al iterar pasajeros en SinglyLinkedList: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al modificar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void deleteUser () {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Verificar si existe el ID en el AVL
            if (!avlTree.contains(id)) {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
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
                    passengers.remove(passengerToRemove);
                   
                    removed = true;
                    break;
                }
            }


            if (removed) {
                
                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));

                
                avlTree.remove(id);
                passengerTree.remove(passengerToRemove); 

                appendOutput("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {
                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar.\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) { 
            appendOutput("Error al operar en SinglyLinkedList: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al eliminar el pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleSearchPassenger() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            // Primero, verificar si el ID existe en el avlTree de IDs
            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + "\n");
                return;
            }

            BTreeNode foundNode = passengerTree.search(new Passenger(id, "", ""));

            if (foundNode != null) {
                // Si el nodo se encontró, extraemos el objeto Passenger de su data
                Passenger foundPassenger = (Passenger) foundNode.data;
                appendOutput("Pasajero encontrado: " + foundPassenger + "\n");
            } else {
                //
                appendOutput("No se encontró pasajero con ID: " + id + " (posible inconsistencia en árboles)\n");
            }
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) { // Captura si avlTree.contains() o search() la lanzan
            appendOutput("Error en árbol AVL durante búsqueda: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error al buscar pasajero: " + e.getMessage() + "\n");
        }
    }

    @FXML
    public void handleListPassengers() {
        try {
            SinglyLinkedList passengers = FileReader.loadPassengers(); // FileReader ahora devuelve SinglyLinkedList

            if (passengers.isEmpty()) {
                appendOutput("No hay pasajeros registrados.\n");
            } else {
                appendOutput("=== Lista de Pasajeros ===");
                // Iterar sobre SinglyLinkedList para mostrar los pasajeros
                for (int i = 1; i <= passengers.size(); i++) {
                    Passenger p = (Passenger) passengers.getNode(i).data; 
                    appendOutput(p.toString());
                }
            }
        } catch (ListException e) { // Captura ListException si getNode() o size() fallan
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList para listar: " + e.getMessage() + "\n");
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

    
    @FXML
    private void userManager(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/PassengerView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Passenger Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para gestionar pasajeros: " + e.getMessage());
        }
    }

    @FXML
    private void airportManager () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AirPortView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Airports Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para gestionar aeropuertos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void logout() {
        Platform.exit();
    }


    public void addRoute(ActionEvent actionEvent) {
    }

    public void modifyRoute(ActionEvent actionEvent) {
    }

    public void shortestPath(ActionEvent actionEvent) {

    }
}