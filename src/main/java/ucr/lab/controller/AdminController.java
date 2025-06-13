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
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.utility.FileReader;

import java.io.IOException; // Importar IOException
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private AVLTree passengerTree; // Árbol para objetos Passenger
    private AVLTree avlTree; // Árbol para IDs de pasajeros
// campos para la info de los passengers
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextArea txtOutput;

    // Campos para la información del vuelo
    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOriginCode;
    @FXML private TextField txtDestinationCode;
    @FXML private TextField txtDepartureTime; // Para LocalDateTime
    @FXML private TextField txtCapacity;
    @FXML private TextField txtOccupancy;
    @FXML private TextField txtFlightStatus;
    @FXML private TextField txtRoute; // Campo para la ruta del vuelo

    // Constructor: Solo para inicializar los árboles, la carga de datos va en initialize()
    public AdminController() {
        this.passengerTree = new AVLTree();
        this.avlTree = new AVLTree();

    }

    @FXML
    public void initialize() {
        // Inicializar árboles si no lo hizo el constructor (aunque ya lo hacen)
        if (passengerTree == null) passengerTree = new AVLTree();
        if (avlTree == null) avlTree = new AVLTree();

        // Carga de pasajeros al iniciar la interfaz
        loadAllPassengersToTrees();
    }

    private void loadAllPassengersToTrees() {
        SinglyLinkedList passengersList = FileReader.loadPassengers();
        int passengerCount = 0;

        try {
            for (int i = 1; i <= passengersList.size(); i++) {
                Passenger p = (Passenger) passengersList.get(i);
                try {
                    // Añadir al árbol de objetos Passenger
                    passengerTree.add(p);
                    // Añadir el ID al árbol de IDs
                    avlTree.add(p.getId());
                    passengerCount++;
                } catch (TreeException e) {
                    appendOutput("Error al cargar pasajero en árbol: " + p.getName() + " (ID: " + p.getId() + "). " + e.getMessage());
                    e.printStackTrace();
                }
            }
            appendOutput("Pasajeros cargados en los árboles: " + passengerCount);
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList (loadAllPassengersToTrees): " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al cargar pasajeros en los árboles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void appendOutput(String text) {
        if (txtOutput != null) { // Siempre verificar nullidad antes de usar elementos @FXML
            txtOutput.appendText(text + "\n");
        }
    }

    @FXML
    private void addUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (avlTree.contains(id)) {
                appendOutput("Ya existe un pasajero con ID: " + id + "\n");
                return;
            }

            Passenger passenger = new Passenger(id, name, nationality);

            if (!txtFlightNumber.getText().trim().isEmpty()) {
                try {
                    int flightNum = Integer.parseInt(txtFlightNumber.getText().trim());
                    int originCode = Integer.parseInt(txtOriginCode.getText().trim());
                    int destinationCode = Integer.parseInt(txtDestinationCode.getText().trim());
                    LocalDateTime departureTime = LocalDateTime.parse(txtDepartureTime.getText().trim());
                    int capacity = Integer.parseInt(txtCapacity.getText().trim());
                    int occupancy = Integer.parseInt(txtOccupancy.getText().trim());
                    String status = txtFlightStatus.getText().trim();
                    String route = txtRoute.getText().trim();

                    Flight newFlight = new Flight(capacity, occupancy, status, route, departureTime,
                            flightNum, originCode, destinationCode, departureTime,
                            new SinglyLinkedList());
                    passenger.addFlight(newFlight); // Añade el objeto Flight al historial del pasajero
                    appendOutput("Vuelo agregado al historial del pasajero.\n");

                } catch (NumberFormatException | DateTimeParseException e) {
                    appendOutput("Error en el formato de los datos del vuelo. Pasajero creado, pero el vuelo no se añadió. Revise el número, códigos, capacidad, ocupación y la hora de salida (yyyy-MM-ddTHH:mm).\n");
                }
            }

            // Cargar la lista actual de pasajeros, añadir el nuevo, y guardar
            SinglyLinkedList currentPassengersInFile = FileReader.loadPassengers();
            currentPassengersInFile.add(passenger);
            FileReader.savePassengers(convertSinglyLinkedListToList(currentPassengersInFile));

            // Añadir al AVL de IDs y al árbol de objetos Passenger
            avlTree.add(id);
            passengerTree.add(passenger);

            appendOutput("Pasajero agregado: " + passenger.getName() + " (ID: " + passenger.getId() + ")\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID, capacidad, ocupación, número de vuelo, códigos de aeropuerto deben ser números válidos.\n");
        } catch (TreeException e) {
            appendOutput("Error al agregar pasajero al árbol AVL: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al operar con la lista de pasajeros: " + e.getMessage() + "\n");
        } catch (Exception e) {
            appendOutput("Error inesperado al agregar pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    @FXML
    private void editUser() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String nationality = txtNationality.getText().trim();

            if (name.isEmpty() || nationality.isEmpty()) {
                appendOutput("Nombre y nacionalidad son obligatorios.\n");
                return;
            }

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + " para modificar.\n");
                return;
            }

            SinglyLinkedList passengers = FileReader.loadPassengers();
            boolean modified = false;

            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i); // Usar .get(i) y castear
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);

                    // Lógica para actualizar el historial de vuelos segun Jime
                    // Si el usuario ingresa datos de vuelo, limpia y añade el nuevo vuelo.
                    // Si no, el historial existente del pasajero se mantiene.
                    if (!txtFlightNumber.getText().trim().isEmpty()) {
                        try {
                            int flightNum = Integer.parseInt(txtFlightNumber.getText().trim());
                            int originCode = Integer.parseInt(txtOriginCode.getText().trim());
                            int destinationCode = Integer.parseInt(txtDestinationCode.getText().trim());
                            LocalDateTime departureTime = LocalDateTime.parse(txtDepartureTime.getText().trim());
                            int capacity = Integer.parseInt(txtCapacity.getText().trim());
                            int occupancy = Integer.parseInt(txtOccupancy.getText().trim());
                            String status = txtFlightStatus.getText().trim();
                            String route = txtRoute.getText().trim();

                            Flight updatedFlight = new Flight(capacity, occupancy, status, route, departureTime, flightNum, originCode, destinationCode, departureTime, new SinglyLinkedList());


                            p.clearFlightHistory(); // Limpia el historial existente del pasajero
                            p.addFlight(updatedFlight); // Añade el nuevo vuelo
                            appendOutput("Historial de vuelo del pasajero con ID " + id + " actualizado.\n");
                        } catch (NumberFormatException | DateTimeParseException e) {
                            appendOutput("Error en el formato de los datos del vuelo. Pasajero modificado, pero el historial de vuelo no se actualizó: " + e.getMessage() + "\n");
                        }
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
            appendOutput("Ingrese un ID de pasajero válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante modificación: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros en SinglyLinkedList: " + e.getMessage() + "\n");
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al modificar pasajero: " + e.getMessage() + "\n");
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
            Passenger passengerToRemove = null;
            boolean removed = false;

            // Encontrar el pasajero en la lista
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i);
                if (p.getId() == id) {
                    passengerToRemove = p;
                    // passengers.remove(passengerToRemove);
                    removed = true; // Marcamos que lo encontramos
                    break;
                }
            }

            if (removed) {
                // Eliminar de la SinglyLinkedList
                passengers.remove(passengerToRemove);

                // Guarda cambios
                FileReader.savePassengers(convertSinglyLinkedListToList(passengers));

                avlTree.remove(id); // Eliminar
                passengerTree.remove(passengerToRemove);

                appendOutput("Pasajero con ID: " + id + " eliminado con éxito.\n");
                clearFields();
            } else {

                appendOutput("No se encontró un pasajero con ID: " + id + " para eliminar (inconsistencia detectada).\n");
            }

        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante eliminación: " + e.getMessage() + "\n");
        } catch (ListException e) {
            appendOutput("Error al operar en SinglyLinkedList durante eliminación: " + e.getMessage() + "\n");
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendOutput("Error inesperado al eliminar el pasajero: " + e.getMessage() + "\n");
            e.printStackTrace();
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

            Passenger searchKey = new Passenger(id, "", ""); // Crea un Passenger con solo el ID para búsqueda
            BTreeNode foundNode = passengerTree.search(searchKey);

            if (foundNode != null && foundNode.data instanceof Passenger) {
                Passenger foundPassenger = (Passenger) foundNode.data;
                appendOutput("Pasajero encontrado:\n" + foundPassenger.toString() + "\n");

                txtId.setText(String.valueOf(foundPassenger.getId()));
                txtName.setText(foundPassenger.getName());
                txtNationality.setText(foundPassenger.getNationality());

            } else {

                appendOutput("No se encontró pasajero con ID: " + id + " (inconsistencia en árboles detectada)\n");
            }
        } catch (NumberFormatException e) {
            appendOutput("Ingrese un ID válido para buscar.\n");
        } catch (TreeException e) {
            appendOutput("Error en árbol AVL durante búsqueda: " + e.getMessage() + "\n");
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
                    Passenger p = (Passenger) passengers.get(i);
                    appendOutput(p.toString());
                }
            }
        } catch (ListException e) {
            appendOutput("Error al iterar pasajeros desde SinglyLinkedList para listar: " + e.getMessage() + "\n");
            e.printStackTrace();
        } catch (ClassCastException e) {
            appendOutput("Error de tipo de dato inesperado en SinglyLinkedList. Asegúrese que solo contiene objetos Passenger. " + e.getMessage());
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



        txtFlightNumber.clear();
        txtOriginCode.clear();
        txtDestinationCode.clear();
        txtDepartureTime.clear();
        txtCapacity.clear();
        txtOccupancy.clear();
        txtFlightStatus.clear();
        txtRoute.clear();
    }

    private List<Passenger> convertSinglyLinkedListToList(SinglyLinkedList singlyLinkedList) throws ListException {
        List<Passenger> list = new ArrayList<>();
        if (singlyLinkedList != null && !singlyLinkedList.isEmpty()) {
            for (int i = 1; i <= singlyLinkedList.size(); i++) {

                list.add((Passenger) singlyLinkedList.get(i));
            }
        }
        return list;
    }

    @FXML
    private void userManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/PassengerView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Passenger Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para gestionar pasajeros: " + e.getMessage());
        }
    }

    @FXML
    private void airportManager() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AirPortView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Airports Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            appendOutput("Error al abrir la ventana para gestionar aeropuertos: " + e.getMessage());
        }
    }


    @FXML
    private void logout() {
        Platform.exit();
    }

    public void addRoute(ActionEvent actionEvent) {
        // Lógica para añadir ruta
        appendOutput("Funcionalidad 'Add Route' no implementada aún.");
    }

    public void modifyRoute(ActionEvent actionEvent) {
        // Lógica para modificar ruta
        appendOutput("Funcionalidad 'Modify Route' no implementada aún.");
    }

    public void shortestPath(ActionEvent actionEvent) {
        // Lógica para encontrar la ruta más corta
        appendOutput("Funcionalidad 'Shortest Path' no implementada aún.");
    }
}