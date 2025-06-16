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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class AdminController {

    private AVLTree passengerTree; // Árbol para objetos Passenger
    private AVLTree avlTree; // Árbol para IDs de pasajeros

    // Campos para la información de los passengers (Mantener en Manage Users tab)
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextArea txtOutput; // Used for general output on Manage Users tab

    // Campos para la información del vuelo (Mantener en Manage Users tab for now, but usually in Manage Flights)
    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOriginCode;
    @FXML private TextField txtDestinationCode;
    @FXML private TextField txtDepartureTimeHour; // Changed from txtDepartureTime to reflect FXML name
    @FXML private DatePicker dpDepartureDate; // Added for DatePicker
    @FXML private TextField txtCapacity;
    @FXML private TextField txtOccupancy;
    @FXML private TextField txtFlightStatus;
    @FXML private TextField txtRoute; // Campo para la ruta del vuelo

    // NUEVOS CAMPOS para el tab "Manage Routes"
    @FXML private ComboBox<String> cmbOrigin; // For origin airport selection
    @FXML private ComboBox<String> cmbDestination; // For destination airport selection
    @FXML private TextField txtDistance; // For route distance/duration
    @FXML private TextArea textArea; // For output on the Manage Routes tab (renamed from txtOutput for clarity)


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

        // Inicializar ComboBoxes si existen (para el tab de rutas)
        if (cmbOrigin != null) {
            // Ejemplo de cómo poblar los ComboBoxes (deberías obtener esta data de tus aeropuertos)
            cmbOrigin.getItems().addAll("SJO - San Jose", "LAX - Los Angeles", "MIA - Miami", "MAD - Madrid");
        }
        if (cmbDestination != null) {
            cmbDestination.getItems().addAll("SJO - San Jose", "LAX - Los Angeles", "MIA - Miami", "MAD - Madrid");
        }
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

    // This method is primarily for the 'Manage Users' tab's output
    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

    // This method is specifically for the 'Manage Routes' tab's output
    private void appendRoutesOutput(String text) {
        if (textArea != null) {
            textArea.appendText(text + "\n");
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
                    // Combine date from DatePicker and time from TextField
                    LocalDateTime departureTime = null;
                    if (dpDepartureDate.getValue() != null && !txtDepartureTimeHour.getText().trim().isEmpty()) {
                        departureTime = dpDepartureDate.getValue().atStartOfDay().withHour(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[0]))
                                .withMinute(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[1]));
                    } else if (dpDepartureDate.getValue() != null) {
                        departureTime = dpDepartureDate.getValue().atStartOfDay();
                    } else {
                        throw new DateTimeParseException("Fecha de salida no especificada o incompleta.", "", 0);
                    }

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
                    appendOutput("Error en el formato de los datos del vuelo. Pasajero creado, pero el vuelo no se añadió. Revise el número, códigos, capacidad, ocupación y la fecha/hora de salida (yyyy-MM-dd y HH:mm).\n");
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
                            LocalDateTime departureTime = null;
                            if (dpDepartureDate.getValue() != null && !txtDepartureTimeHour.getText().trim().isEmpty()) {
                                departureTime = dpDepartureDate.getValue().atStartOfDay().withHour(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[0]))
                                        .withMinute(Integer.parseInt(txtDepartureTimeHour.getText().trim().split(":")[1]));
                            } else if (dpDepartureDate.getValue() != null) {
                                departureTime = dpDepartureDate.getValue().atStartOfDay();
                            } else {
                                throw new DateTimeParseException("Fecha de salida no especificada o incompleta.", "", 0);
                            }
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
        txtDepartureTimeHour.clear(); // Corrected field name
        if (dpDepartureDate != null) dpDepartureDate.setValue(null); // Clear DatePicker
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
            // This seems to open a separate PassengerView.fxml, which might not be needed
            // if "Manage Users" tab is handling passenger management directly within AdminController.
            // Consider if this method is still necessary.
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

    @FXML
    public void addRoute(ActionEvent actionEvent) {
        // Implement the logic for adding a route here
        String origin = cmbOrigin.getValue();
        String destination = cmbDestination.getValue();
        String distanceText = txtDistance.getText();

        if (origin == null || destination == null || origin.isEmpty() || destination.isEmpty() || distanceText.isEmpty()) {
            appendRoutesOutput("Please select origin, destination, and enter distance.");
            return;
        }

        try {
            double distance = Double.parseDouble(distanceText);
            appendRoutesOutput("Adding new route: " + origin + " to " + destination + " (Distance: " + distance + ")");
            // Add your route logic here (e.g., call a service/manager to add the route)
            clearRouteFields();
        } catch (NumberFormatException e) {
            appendRoutesOutput("Invalid distance. Please enter a numeric value.");
        }
    }

    @FXML
    public void modifyRoute(ActionEvent actionEvent) {
        // Implement the logic for modifying a route here
        String origin = cmbOrigin.getValue();
        String destination = cmbDestination.getValue();
        String distanceText = txtDistance.getText();

        if (origin == null || destination == null || origin.isEmpty() || destination.isEmpty() || distanceText.isEmpty()) {
            appendRoutesOutput("Please select origin, destination, and enter distance to modify.");
            return;
        }

        try {
            double distance = Double.parseDouble(distanceText);
            appendRoutesOutput("Modifying route: " + origin + " to " + destination + " (New Distance: " + distance + ")");
            // Add your route modification logic here
            clearRouteFields();
        } catch (NumberFormatException e) {
            appendRoutesOutput("Invalid distance. Please enter a numeric value.");
        }
    }

    @FXML
    public void shortestPath(ActionEvent actionEvent) {
        // Implement the logic for finding the shortest path here
        String origin = cmbOrigin.getValue();
        String destination = cmbDestination.getValue();

        if (origin == null || destination == null || origin.isEmpty() || destination.isEmpty()) {
            appendRoutesOutput("Please select both origin and destination airports to find the shortest path.");
            return;
        }
        appendRoutesOutput("Finding shortest path from " + origin + " to " + destination + "...");
        // Add your shortest path algorithm call here, and display results in textArea
    }

    private void clearRouteFields() {
        if (cmbOrigin != null) cmbOrigin.getSelectionModel().clearSelection();
        if (cmbDestination != null) cmbDestination.getSelectionModel().clearSelection();
        if (txtDistance != null) txtDistance.clear();
    }

    // You might want to consider removing these action methods if they are not directly tied
    // to the functionality of the AdminController and are instead handled by separate views.
    // However, if they launch new windows, they are fine here.
    @FXML
    private void modifyFlight(ActionEvent event) {
        appendOutput("Funcionalidad 'Modify Flight' no implementada aún.");
    }

    @FXML
    private void deleteFlight(ActionEvent event) {
        appendOutput("Funcionalidad 'Delete Flight' no implementada aún.");
    }

    @FXML
    private void addFlight(ActionEvent event) {
        appendOutput("Funcionalidad 'Add Flight' no implementada aún.");
    }

}