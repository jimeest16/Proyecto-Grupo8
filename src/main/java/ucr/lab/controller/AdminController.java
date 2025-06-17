package ucr.lab.controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.BTreeNode;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.Dijkstra;
import java.util.HashSet;
import java.util.Set;

public class AdminController {

    private AVLTree passengerTree; // Árbol para objetos Passenger
    private AVLTree avlTree;     // Árbol para IDs de pasajeros
    private SinglyLinkedList airportList; // Para almacenar los datos de los aeropuertos cargados
    private SinglyLinkedList routeList; // Lista de objetos Route
    private Dijkstra airportGraph; // Instancia de tu grafo Dijkstra para cálculo de rutas
    private SinglyLinkedList flightsList; // To store Flight objects
    // Campos para la información de los passengers (Mantener en Manage Users tab bien organizados )
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtNationality;
    @FXML
    private TextArea txtOutput;

    // Campos para el tab "Manage Flights"
    @FXML
    private TextField idFlightNumber;
    @FXML
    private ComboBox<String> cmbFlightOrigin;
    @FXML
    private ComboBox<String> cmbFlightDestination;
    @FXML
    private DatePicker dpFlightDepartureDate;
    @FXML
    private TextField txtFlightDepartureTime;
    @FXML
    private TextField txtFlightCapacity;
    @FXML
    private Label lblFlightStatus;
    @FXML
    private TextField txtPassengerIdToAssign;
    @FXML
    private TextArea txtFlightOutput;

    @FXML
    private TextField txtFlightNumber;
    @FXML
    private TextField txtOriginCode;
    @FXML
    private TextField txtDestinationCode;
    @FXML
    private TextField txtDepartureTimeHour;
    @FXML
    private DatePicker dpDepartureDate;
    @FXML
    private TextField txtCapacity;
    @FXML
    private TextField txtOccupancy;
    @FXML
    private TextField txtFlightStatus;
    @FXML
    private TextField txtRoute;

    // Campos para el tab "Manage Routes"
    @FXML
    private ComboBox<String> cmbOrigin; // Para selección de aeropuerto de origen
    @FXML
    private ComboBox<String> cmbDestination; // Para selección de aeropuerto de destino
    @FXML
    private TextField txtDistance; // Para distancia/duración de la ruta
    @FXML
    private TextArea textArea; // Para salida en la pestaña de gestión de rutas


    // Constructor: Solo para inicializar los árboles, la carga de datos va en initialize()
    public AdminController() {
        this.passengerTree = new AVLTree();
        this.avlTree = new AVLTree();
        this.airportList = new SinglyLinkedList();
        this.routeList = new SinglyLinkedList(); // Inicializa la lista de rutas
        this.airportGraph = new Dijkstra();
    }

    @FXML
    public void initialize() throws ListException {
        // Inicializar árboles si no lo hizo el constructor (aunque ya lo hacen)
        if (passengerTree == null) passengerTree = new AVLTree();
        if (avlTree == null) avlTree = new AVLTree();
        if (airportList == null) airportList = new SinglyLinkedList();
        if (routeList == null) routeList = new SinglyLinkedList();
        if (airportGraph == null) airportGraph = new Dijkstra();
        if(flightsList== null ) flightsList= new SinglyLinkedList();
       // load todo
        loadAllPassengersToTrees();


        loadAirportsAndPopulateComboBoxes();

     loadAirportsAndPopulateComboBox();

        loadAllRoutes();


        buildDijkstraGraph();
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

    private void loadAirportsAndPopulateComboBox() {
        airportList = FileReader.loadAirports();

        if (cmbFlightOrigin != null && cmbFlightDestination != null) {
            try {

                cmbFlightOrigin.getItems().clear();
                cmbFlightDestination.getItems().clear();

                for (int i = 1; i <= airportList.size(); i++) {
                    AirPort airport = (AirPort) airportList.get(i);
                    // Mostrar una representación legible en el ComboBox, incluyendo el código
                    String airportDisplay = airport.getCode() + " - " + airport.getName();
                    cmbFlightOrigin.getItems().add(airportDisplay);
                    cmbFlightDestination.getItems().add(airportDisplay);
                }
                appendRoutesOutput("Aeropuertos cargados en los ComboBoxes.");
            } catch (ListException e) {
                appendRoutesOutput("Error al cargar aeropuertos desde la lista para los ComboBoxes: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassCastException e) {
                appendRoutesOutput("Error de tipo de dato inesperado en SinglyLinkedList de aeropuertos. Asegúrese que solo contiene objetos AirPort. " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                appendRoutesOutput("Error inesperado al poblar los ComboBoxes de aeropuertos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private void loadAirportsAndPopulateComboBoxes() {
        airportList = FileReader.loadAirports();

        if (cmbOrigin != null && cmbDestination != null) {
            try {

                cmbOrigin.getItems().clear();
                cmbDestination.getItems().clear();

                for (int i = 1; i <= airportList.size(); i++) {
                    AirPort airport = (AirPort) airportList.get(i);
                    // Mostrar una representación legible en el ComboBox, incluyendo el código
                    String airportDisplay = airport.getCode() + " - " + airport.getName();
                    cmbOrigin.getItems().add(airportDisplay);
                    cmbDestination.getItems().add(airportDisplay);
                }
                appendRoutesOutput("Aeropuertos cargados en los ComboBoxes.");
            } catch (ListException e) {
                appendRoutesOutput("Error al cargar aeropuertos desde la lista para los ComboBoxes: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassCastException e) {
                appendRoutesOutput("Error de tipo de dato inesperado en SinglyLinkedList de aeropuertos. Asegúrese que solo contiene objetos AirPort. " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                appendRoutesOutput("Error inesperado al poblar los ComboBoxes de aeropuertos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void loadAllRoutes() throws ListException {
        routeList = FileReader.loadRoutes();
        appendRoutesOutput("Rutas cargadas desde 'rutas.json': " + routeList.size());
        try {
            if (!routeList.isEmpty()) {
                appendRoutesOutput("=== Rutas Existentes (Origen -> [Destinos, Distancia]) ===");
                for (int i = 1; i <= routeList.size(); i++) {
                    Route r = (Route) routeList.get(i);
                    StringBuilder routeInfo = new StringBuilder();
                    routeInfo.append("Origen: ").append(r.getOriginAirportCode());
                    if (r.getDestinationList() != null && !r.getDestinationList().isEmpty()) {
                        routeInfo.append(" -> Destinos: ");
                        for (int j = 1; j <= r.getDestinationList().size(); j++) {
                            Destination d = (Destination) r.getDestinationList().get(j);
                            routeInfo.append(d.getAirportCode())
                                    .append(" (").append(d.getDistance()).append("km)");
                            if (j < r.getDestinationList().size()) {
                                routeInfo.append(", ");
                            }
                        }
                        routeInfo.append("");
                    } else {
                        routeInfo.append(" -> Sin destinos directos.");
                    }
                    appendRoutesOutput(routeInfo.toString());
                }
            }
        } catch (ListException e) {
            appendRoutesOutput("Error al listar rutas cargadas: " + e.getMessage());
        }
    }

    private void buildDijkstraGraph() {
        airportGraph.clear();

        try {

            Set<Integer> uniqueAirportCodes = new HashSet<>();
            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                uniqueAirportCodes.add(r.getOriginAirportCode());
                if (r.getDestinationList() != null) {
                    for (int j = 1; j <= r.getDestinationList().size(); j++) {
                        Destination d = (Destination) r.getDestinationList().get(j);
                        uniqueAirportCodes.add(d.getAirportCode());
                    }
                }
            }

            for (Integer code : uniqueAirportCodes) {
                airportGraph.addVertex(code);
            }

            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                int origin = r.getOriginAirportCode();
                if (r.getDestinationList() != null) {
                    for (int j = 1; j <= r.getDestinationList().size(); j++) {
                        Destination d = (Destination) r.getDestinationList().get(j);
                        int destination = d.getAirportCode();
                        double distance = d.getDistance();
                        airportGraph.addEdgeWeight(origin, destination, distance);
                    }
                }
            }
            appendRoutesOutput("Grafo de rutas de Dijkstra construido con " + airportGraph.size() + " vértices.");
        } catch (ListException e) {
            appendRoutesOutput("Error al construir el grafo de Dijkstra desde routeList: " + e.getMessage());
            e.printStackTrace();
        } catch (GraphException e) {
            appendRoutesOutput("Error en el grafo al construir el grafo de Dijkstra: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al construir el grafo de Dijkstra: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void appendOutput(String text) {
        if (txtOutput != null) {
            txtOutput.appendText(text + "\n");
        }
    }

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
                    passenger.addFlight(newFlight);
                    appendOutput("Vuelo agregado al historial del pasajero.\n");

                } catch (NumberFormatException | DateTimeParseException e) {
                    appendOutput("Error en el formato de los datos del vuelo. Pasajero creado, pero el vuelo no se añadió. Revise el número, códigos, capacidad, ocupación y la fecha/hora de salida (yyyy-MM-dd y HH:mm).\n");
                }
            }

            // Usando  FileReader para añadir pasajero
            FileReader.addPassenger(passenger);

            // Añadir al AVL de IDs y al árbol de objetos Passenger
            avlTree.add(id);
            passengerTree.add(passenger);

            appendOutput("Pasajero agregado: " + passenger.getName() + " (ID: " + passenger.getId() + ")\n");
            clearFields();
        } catch (NumberFormatException e) {
            appendOutput("ID, capacidad, ocupación, número de vuelo, códigos de aeropuerto deben ser números válidos.\n");
        } catch (TreeException e) {
            appendOutput("Error al agregar pasajero al árbol AVL: " + e.getMessage() + "\n");
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
            Passenger passengerToModify = null;

            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i);
                if (p.getId() == id) {
                    p.setName(name);
                    p.setNationality(nationality);
                    passengerToModify = p; // Guarda la referencia al pasajero modificado
                    modified = true;

                    // Lógica para actualizar el historial de vuelos segun Jime
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

                            p.clearFlightHistory();
                            p.addFlight(updatedFlight);
                            appendOutput("Historial de vuelo del pasajero con ID " + id + " actualizado.\n");
                        } catch (NumberFormatException | DateTimeParseException e) {
                            appendOutput("Error en el formato de los datos del vuelo. Pasajero modificado, pero el historial de vuelo no se actualizó: " + e.getMessage() + "\n");
                        }
                    }
                    break;
                }
            }

            if (modified) {
                // Para guardar pasajeros, necesitas convertirlos a List<Passenger>
                List<Passenger> passengersToSave = new ArrayList<>();
                for (int i = 1; i <= passengers.size(); i++) {
                    passengersToSave.add((Passenger) passengers.get(i));
                }
                FileReader.savePassengers(passengersToSave);
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
            boolean removedFromList = false;

            // Encontrar el pasajero en la lista y eliminarlo
            for (int i = 1; i <= passengers.size(); i++) {
                Passenger p = (Passenger) passengers.get(i);
                if (p.getId() == id) {
                    passengerToRemove = p;
                    passengers.remove(p); // Remueve el objeto Passenger
                    removedFromList = true;
                    break;
                }
            }

            if (removedFromList) {
                // Guarda cambios
                List<Passenger> passengersToSave = new ArrayList<>();
                for (int i = 1; i <= passengers.size(); i++) {
                    passengersToSave.add((Passenger) passengers.get(i));
                }
                FileReader.savePassengers(passengersToSave);

                // Eliminar de los árboles en memoria
                avlTree.remove(id);
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

            if (!avlTree.contains(id)) {
                appendOutput("No se encontró pasajero con ID: " + id + "\n");
                return;
            }

            Passenger searchKey = new Passenger(id, "", "");
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
            SinglyLinkedList passengers = FileReader.loadPassengers(); // Carga la lista más reciente

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
        txtDepartureTimeHour.clear();
        if (dpDepartureDate != null) dpDepartureDate.setValue(null);
        txtCapacity.clear();
        txtOccupancy.clear();
        txtFlightStatus.clear();
        txtRoute.clear();
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


    @FXML
    public void addRoute(ActionEvent actionEvent) {
        String originDisplay = cmbOrigin.getValue();
        String destinationDisplay = cmbDestination.getValue();
        String distanceText = txtDistance.getText();

        if (originDisplay == null || destinationDisplay == null || originDisplay.isEmpty() || destinationDisplay.isEmpty() || distanceText.isEmpty()) {
            appendRoutesOutput("Por favor, selecciona origen, destino e ingresa la distancia.");
            return;
        }

        try {
            double distance = Double.parseDouble(distanceText);

            int originAirportCode = extractAirportCode(originDisplay);
            int destinationAirportCode = extractAirportCode(destinationDisplay);

            if (originAirportCode == -1 || destinationAirportCode == -1) { // -1 indica error en extracción
                appendRoutesOutput("Error: No se pudo extraer el código numérico del aeropuerto de la selección.");
                return;
            }

            // 1. Busca si ya existe una 'Route' para este 'originAirportCode'
            Route existingRoute = null;
            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                if (r.getOriginAirportCode() == originAirportCode) {
                    existingRoute = r;
                    break;
                }
            }

            // Crea el nuevo objeto Destination
            Destination newDestination = new Destination(destinationAirportCode, distance);

            if (existingRoute != null) {
                // Si la ruta de origen ya existe, añade o actualiza el destino en su lista
                SinglyLinkedList destList = existingRoute.getDestinationList();
                boolean destinationExists = false;
                for (int i = 1; i <= destList.size(); i++) {
                    Destination d = (Destination) destList.get(i);
                    if (d.getAirportCode() == destinationAirportCode) {
                        d.setDistance(distance); // Actualiza la distancia si ya existe
                        destinationExists = true;
                        appendRoutesOutput("Actualizada la distancia para el destino " + destinationAirportCode + " desde el origen " + originAirportCode);
                        break;
                    }
                }
                if (!destinationExists) {
                    destList.add(newDestination); // Añade un nuevo destino a la lista existente
                    appendRoutesOutput("Agregado nuevo destino " + destinationAirportCode + " al origen existente " + originAirportCode);
                }
            } else {
                // Si la ruta de origen no existe, crea una nueva 'Route' con el nuevo destino
                SinglyLinkedList newDestList = new SinglyLinkedList();
                newDestList.add(newDestination);
                Route newRoute = new Route(originAirportCode, newDestList);
                routeList.add(newRoute); // Añade la nueva ruta completa a la lista principal
                appendRoutesOutput("Agregada nueva ruta de origen " + originAirportCode + " con destino " + destinationAirportCode);
            }


            List<Route> routesToSave = FileReader.convertSinglyLinkedListToRouteList(routeList);
            FileReader.saveRoutes(routesToSave);

            clearRouteFields();
            loadAllRoutes();
            buildDijkstraGraph(); // Reconstruir el grafo después de modificar rutas
        } catch (NumberFormatException e) {
            appendRoutesOutput("Distancia inválida o código de aeropuerto no numérico. Por favor, ingresa valores válidos.");
        } catch (ListException e) {
            appendRoutesOutput("Error al manipular la lista de rutas en memoria: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al añadir/modificar la ruta: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void modifyRoute(ActionEvent actionEvent) {

        String originDisplay = cmbOrigin.getValue();
        String destinationDisplay = cmbDestination.getValue();
        String distanceText = txtDistance.getText();

        if (originDisplay == null || destinationDisplay == null || originDisplay.isEmpty() || destinationDisplay.isEmpty() || distanceText.isEmpty()) {
            appendRoutesOutput("Por favor, selecciona origen, destino e ingresa la nueva distancia para modificar.");
            return;
        }

        try {
            double newDistance = Double.parseDouble(distanceText);
            int originAirportCode = extractAirportCode(originDisplay);
            int destinationAirportCode = extractAirportCode(destinationDisplay);

            if (originAirportCode == -1 || destinationAirportCode == -1) {
                appendRoutesOutput("Error: No se pudo extraer el código numérico del aeropuerto de la selección.");
                return;
            }

            boolean routeModified = false;

            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                if (r.getOriginAirportCode() == originAirportCode) {
                    // Si encuentra la ruta de origen, busca el destino en su lista
                    SinglyLinkedList destList = r.getDestinationList();
                    for (int j = 1; j <= destList.size(); j++) {
                        Destination d = (Destination) destList.get(j);
                        if (d.getAirportCode() == destinationAirportCode) {
                            d.setDistance(newDistance); // Modifica la distancia
                            routeModified = true;
                            appendRoutesOutput("Distancia actualizada para la ruta: Origen " + originAirportCode + " -> Destino " + destinationAirportCode + " a " + newDistance + "km.");
                            break; // Se encontró y modificó el destino
                        }
                    }
                    break; // Se encontró la ruta de origen
                }
            }

            if (routeModified) {
                List<Route> routesToSave = FileReader.convertSinglyLinkedListToRouteList(routeList);
                FileReader.saveRoutes(routesToSave);
                clearRouteFields();
                loadAllRoutes();
                buildDijkstraGraph(); // Reconstruir el grafo después de modificar rutas
            } else {
                appendRoutesOutput("No se encontró la ruta directa de Origen " + originAirportCode + " a Destino " + destinationAirportCode + " para modificar.");
                appendRoutesOutput("Considera usar 'Añadir Ruta' si es una conexión nueva.");
            }

        } catch (NumberFormatException e) {
            appendRoutesOutput("Distancia inválida o código de aeropuerto no numérico. Por favor, ingresa valores válidos.");
        } catch (ListException e) {
            appendRoutesOutput("Error al acceder a la lista de rutas para modificar: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al modificar la ruta: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private int extractAirportCode(String airportString) {
        if (airportString == null || airportString.isEmpty()) return -1;
        try {
            // Divide el string por " - " y toma la primera parte
            String codeStr = airportString.split(" - ")[0].trim();
            return Integer.parseInt(codeStr);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error al extraer el código del aeropuerto de: '" + airportString + "' - " + e.getMessage());
            return -1; // Indica un error
        }
    }

    @FXML
    private void clearRouteFields() {
        cmbOrigin.getSelectionModel().clearSelection();
        cmbDestination.getSelectionModel().clearSelection();
        txtDistance.clear();
    }


    @FXML
    public void shortestPath(ActionEvent actionEvent) {
        String originDisplay = cmbOrigin.getValue();
        String destinationDisplay = cmbDestination.getValue();

        if (originDisplay == null || destinationDisplay == null || originDisplay.isEmpty() || destinationDisplay.isEmpty()) {
            appendRoutesOutput("Por favor, selecciona tanto el aeropuerto de origen como el de destino para encontrar la ruta más corta.");
            return;
        }

        try {

            int originCode = extractAirportCode(originDisplay);
            int destinationCode = extractAirportCode(destinationDisplay);

            if (originCode == -1 || destinationCode == -1) {
                appendRoutesOutput("Error: No se pudieron extraer los códigos de aeropuerto válidos de la selección.");
                return;
            }

            appendRoutesOutput("Encontrando la ruta más corta de " + originCode + " a " + destinationCode + "...");


            if (airportGraph.isEmpty()) {
                appendRoutesOutput("Error: El grafo de rutas está vacío. No se pueden calcular rutas.");
                return;
            }
            if (!airportGraph.containsVertex(originCode)) {
                appendRoutesOutput("Error: El aeropuerto de origen " + originCode + " no existe en el grafo de rutas.");
                return;
            }
            if (!airportGraph.containsVertex(destinationCode)) {
                appendRoutesOutput("Error: El aeropuerto de destino " + destinationCode + " no existe en el grafo de rutas.");
                return;
            }


            SinglyLinkedList shortestPath = airportGraph.dijkstra(originCode, destinationCode);
            double totalDistance = airportGraph.getLastCalculatedDistance();


            if (totalDistance != Double.MAX_VALUE) {
                StringBuilder pathString = new StringBuilder();
                if (shortestPath != null && !shortestPath.isEmpty()) {

                    for (int i = 1; i <= shortestPath.size(); i++) {
                        pathString.append(shortestPath.get(i));
                        if (i < shortestPath.size()) {
                            pathString.append(" -> ");
                        }
                    }

                } else {
                    pathString.append("No se pudo construir la ruta o la ruta es vacía.");
                }

                appendRoutesOutput("Ruta encontrada: " + pathString.toString());
                appendRoutesOutput("Distancia total: " + String.format("%.2f", totalDistance) + " km.");
            } else {
                appendRoutesOutput("No se encontró una ruta de " + originCode + " a " + destinationCode + ". No hay conexión posible.");
            }

        } catch (GraphException e) {
            appendRoutesOutput("Error en el grafo al buscar la ruta más corta: " + e.getMessage());
            e.printStackTrace();
        } catch (ListException e) {
            appendRoutesOutput("Error en la lista al buscar la ruta más corta: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al buscar la ruta más corta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void populateFlightAirportComboBoxes() {
        if (cmbFlightOrigin != null && cmbFlightDestination != null) {
            try {
                cmbFlightOrigin.getItems().clear();
                cmbFlightDestination.getItems().clear();

                for (int i = 1; i <= airportList.size(); i++) {
                    // Assuming airportList contains Airport objects with getCode() and getName()
                    Object airportObj = airportList.get(i);
                    if (airportObj instanceof AirPort) { // Ensure it's an AirPort object
                        AirPort airport = (AirPort) airportObj;
                        String airportDisplay = airport.getCode() + " - " + airport.getName();
                        cmbFlightOrigin.getItems().add(airportDisplay);
                        cmbFlightDestination.getItems().add(airportDisplay);
                    }
                }
                appendFlightOutput("Aeropuertos cargados en los ComboBoxes de Vuelos.");
            } catch (ListException e) {
                appendFlightOutput("Error al cargar aeropuertos para los ComboBoxes de Vuelos: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                appendFlightOutput("Error inesperado al poblar los ComboBoxes de aeropuertos de Vuelos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @FXML
    public void createFlight(ActionEvent event) {
        String flightNumberText = idFlightNumber.getText();
        String originSelected = cmbFlightOrigin.getSelectionModel().getSelectedItem();
        String destinationSelected = cmbFlightDestination.getSelectionModel().getSelectedItem();
        LocalDate departureDate = dpFlightDepartureDate.getValue();
        String departureTimeText = txtFlightDepartureTime.getText();
        String capacityText = txtFlightCapacity.getText();

        if (flightNumberText.isEmpty() || originSelected == null || destinationSelected == null ||
                departureDate == null || departureTimeText.isEmpty() || capacityText.isEmpty()) {
            appendFlightOutput("Todos los campos obligatorios para crear un vuelo deben ser completados.");
            return;
        }

        try {
            int flightNumber = Integer.parseInt(flightNumberText);
            int originCode = Integer.parseInt(originSelected.split(" - ")[0]);
            int destinationCode = Integer.parseInt(destinationSelected.split(" - ")[0]);
            int capacity = Integer.parseInt(capacityText);

            if (capacity <= 0) {
                appendFlightOutput("La capacidad del vuelo debe ser mayor que 0.");
                return;
            }
            if (originCode == destinationCode) {
                appendFlightOutput("El origen y el destino del vuelo no pueden ser el mismo aeropuerto.");
                return;
            }

            LocalTime departureTime = LocalTime.parse(departureTimeText, DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);

            // Check for duplicate flight number (assuming flight number is unique identifier)
            boolean flightExists = false;
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight existingFlight = (Flight) flightsList.get(i);
                if (existingFlight.getNumber() == flightNumber) { // Use getNumber() as per your Flight class
                    flightExists = true;
                    break;
                }
            }

            if (flightExists) {
                appendFlightOutput("Error: Ya existe un vuelo con el número " + flightNumber + ".");
                return;
            }

            // Create new Flight object. Initialize occupancy to 0 and status to "Programado"
            Flight newFlight = new Flight(flightNumber, originCode, destinationCode, departureDateTime, capacity, 0, "Programado");
            flightsList.add(newFlight);
            FileReader.saveFlights(flightsList); // Save updated flights to file
            appendFlightOutput("Vuelo " + flightNumber + " creado con éxito:\n" + newFlight.toString());
            clearFlightFields(null); // Call the clear method
            loadAllAirports(); // Refresh the list in the output area

        } catch (NumberFormatException e) {
            appendFlightOutput("Error de formato: Asegúrese de que el Número de Vuelo y la Capacidad son números válidos.");
        } catch (DateTimeParseException e) {
            appendFlightOutput("Error de formato de hora: Use HH:mm (ej. 14:30) para la hora de salida.");
        } catch (ListException e) {
            appendFlightOutput("Error al crear vuelo: " + e.getMessage());
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al crear vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void assignPassengerToFlight(ActionEvent event) {
        String flightNumberText = idFlightNumber.getText();
        String passengerIdText = txtPassengerIdToAssign.getText();

        if (flightNumberText.isEmpty() || passengerIdText.isEmpty()) {
            appendFlightOutput("Ingrese el número de vuelo y el ID del pasajero a asignar.");
            return;
        }

        try {
            int flightNumber = Integer.parseInt(flightNumberText);
            int passengerId = Integer.parseInt(passengerIdText);

            Flight targetFlight = null;
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight currentFlight = (Flight) flightsList.get(i);
                if (currentFlight.getNumber() == flightNumber) { // Use getNumber()
                    targetFlight = currentFlight;
                    break;
                }
            }

            if (targetFlight == null) {
                appendFlightOutput("Error: Vuelo con número " + flightNumber + " no encontrado.");
                return;
            }

            if (targetFlight.isFull()) { // Use the isFull() method from your Flight class
                appendFlightOutput("Error: El vuelo " + flightNumber + " está lleno. No se puede asignar más pasajeros.");
                return;
            }

            // Check if passenger exists in the passengerTree
            Passenger passengerToFind = new Passenger(passengerId); // Assuming Passenger has a constructor by ID
            if (!passengerTree.contains(passengerToFind)) {
                appendFlightOutput("Error: No se encontró ningún pasajero con el ID " + passengerId + ".");
                return;
            }
            // No need to retrieve the full passenger object if we only need the ID for the flight's passengerIDs list

            // Check if passenger is already assigned to this flight using getPassengerIDs()
            boolean isPassengerAlreadyAssigned = false;
            if (targetFlight.getPassengerIDs() != null && !targetFlight.getPassengerIDs().isEmpty()) {
                for (int i = 1; i <= targetFlight.getPassengerIDs().size(); i++) {
                    Object idInList = targetFlight.getPassengerIDs().get(i);
                    if (idInList instanceof Integer && (Integer) idInList == passengerId) {
                        isPassengerAlreadyAssigned = true;
                        break;
                    }
                }
            }

            if (isPassengerAlreadyAssigned) {
                appendFlightOutput("Error: El pasajero con ID " + passengerId + " ya está asignado a este vuelo.");
                return;
            }

            targetFlight.addPassengerID(passengerId); // Use addPassengerID from your Flight class
            FileReader.saveFlights(flightsList); // Save updated flights to file

            appendFlightOutput("Pasajero " + passengerId + " asignado con éxito al vuelo " + flightNumber + ".");
            // Update the status label to show current occupancy
            lblFlightStatus.setText("Ocupación: " + targetFlight.getOccupancy() + "/" + targetFlight.getCapacity());
            clearFlightFields(null); // Clear relevant fields after assignment
            loadAllAirports(); // Refresh flight list output to reflect changes

        } catch (NumberFormatException e) {
            appendFlightOutput("Error de formato: El número de vuelo y el ID del pasajero deben ser números válidos.");
        } catch (ListException | TreeException e) { // Catch TreeException if passengerTree.contains can throw it
            appendFlightOutput("Error al asignar pasajero al vuelo: " + e.getMessage());
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al asignar pasajero al vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void simulateFlight(ActionEvent event) {
        String flightNumberText = idFlightNumber.getText();

        if (flightNumberText.isEmpty()) {
            appendFlightOutput("Ingrese el número de vuelo a simular.");
            return;
        }

        try {
            int flightNumber = Integer.parseInt(flightNumberText);
            Flight targetFlight = null;
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight currentFlight = (Flight) flightsList.get(i);
                if (currentFlight.getNumber() == flightNumber) { // Use getNumber()
                    targetFlight = currentFlight;
                    break;
                }
            }

            if (targetFlight == null) {
                appendFlightOutput("Error: Vuelo con número " + flightNumber + " no encontrado para simular.");
                return;
            }

            if (targetFlight.getOccupancy() == 0) { // Use getOccupancy()
                appendFlightOutput("El vuelo " + flightNumber + " no tiene pasajeros asignados. Simulación cancelada.");
                return;
            }

            if (targetFlight.getStatus().equals("Completado")) { // Use getStatus()
                appendFlightOutput("El vuelo " + flightNumber + " ya ha sido completado.");
                return;
            }

            // Simulate flight status change
            targetFlight.setStatus("En Vuelo"); // Use setStatus()
            appendFlightOutput("Iniciando simulación para el vuelo " + flightNumber + "...");
            appendFlightOutput("Estado actual del vuelo: " + targetFlight.getStatus());

            // Simulate some delay or process in a new thread to avoid freezing UI
            Flight finalTargetFlight = targetFlight;
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Simulate flight duration (e.g., 3 seconds)
                    Platform.runLater(() -> {
                        finalTargetFlight.setStatus("Completado"); // Use setStatus()
                        appendFlightOutput("Vuelo " + flightNumber + " ha aterrizado y está " + finalTargetFlight.getStatus() + ".");
                        try {
                            FileReader.saveFlights(flightsList); // Save updated status
                        } catch (ListException e) {
                            throw new RuntimeException(e);
                        }
                        clearFlightFields(null);
                        loadAllAirports(); // Refresh list to show updated status
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Platform.runLater(() -> appendFlightOutput("Simulación de vuelo " + flightNumber + " interrumpida."));
                }
            }).start();

        } catch (NumberFormatException e) {
            appendFlightOutput("Error de formato: El número de vuelo debe ser un número válido.");
        } catch (ListException e) {
            appendFlightOutput("Error al buscar vuelo para simular: " + e.getMessage());
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al simular vuelo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void clearFlightFields(ActionEvent event) {
        idFlightNumber.clear();
        cmbFlightOrigin.getSelectionModel().clearSelection();
        cmbFlightDestination.getSelectionModel().clearSelection();
        dpFlightDepartureDate.setValue(null);
        txtFlightDepartureTime.clear();
        txtFlightCapacity.clear();
        lblFlightStatus.setText("Programado"); // Reset status to default
        txtPassengerIdToAssign.clear();
        txtFlightOutput.clear();
        appendFlightOutput("Campos de gestión de vuelos limpiados.");
    }

    @FXML
    public void showActiveFlights(ActionEvent event) {
        StringBuilder sb = new StringBuilder("=== Vuelos Programados/Activos ===\n");
        boolean found = false;
        try {
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight flight = (Flight) flightsList.get(i);
                // Use getStatus() to check flight status
                if (flight.getStatus().equals("Programado") || flight.getStatus().equals("En Vuelo")) {
                    sb.append(flight.toString()).append("\n");
                    found = true;
                }
            }
            if (!found) {
                sb.append("No hay vuelos programados o actualmente en vuelo.");
            }
            appendFlightOutput(sb.toString());
        } catch (ListException e) {
            appendFlightOutput("Error al listar vuelos activos: " + e.getMessage());
        }
    }

    @FXML
    public void showCompletedFlights(ActionEvent event) {
        StringBuilder sb = new StringBuilder("=== Vuelos Completados ===\n");
        boolean found = false;
        try {
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight flight = (Flight) flightsList.get(i);
                // Use getStatus() to check flight status
                if (flight.getStatus().equals("Completado")) {
                    sb.append(flight.toString()).append("\n");
                    found = true;
                }
            }
            if (!found) {
                sb.append("No hay vuelos completados.");
            }
            appendFlightOutput(sb.toString());
        } catch (ListException e) {
            appendFlightOutput("Error al listar vuelos completados: " + e.getMessage());
        }
    }

    @FXML
    public void listAllFlights(ActionEvent event) {
        if (flightsList.isEmpty()) {
            appendFlightOutput("No hay vuelos registrados en el sistema.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== Lista de Todos los Vuelos ===\n");
        try {
            for (int i = 1; i <= flightsList.size(); i++) {
                Flight flight = (Flight) flightsList.get(i);
                sb.append(flight.toString()).append("\n");
            }
            appendFlightOutput(sb.toString());
        } catch (ListException e) {
            appendFlightOutput("Error al listar todos los vuelos: " + e.getMessage());
        }
    }


    private void appendFlightOutput(String message) {
        Platform.runLater(() -> txtFlightOutput.appendText(message + "\n"));
    }

// --- Initialization and Data Loading (Crucial for functionality) ---
// You MUST ensure these methods exist and are called in your AdminController's initialize() method.
// private void loadAllFlights() {
//     // Example: flightsList = FileReader.loadFlights();
//     // This method should load your flights data into the flightsList SinglyLinkedList.
// }
//
// private void loadAllPassengersToTrees() {
//     // Example: passengerTree = FileReader.loadPassengers();
//     // This method should load your passengers data into the passengerTree AVLTree.
//     // It's essential for the assignPassengerToFlight method.
// }
//


    void loadAllAirports() {
        try {
            // Assuming FileReader.loadAirports() returns a SinglyLinkedList<AirPort>
            airportList = FileReader.loadAirports();
            appendFlightOutput("Aeropuertos cargados: " + airportList.size()); // Use appendFlightOutput for consistency
            // You might want to call populateFlightAirportComboBoxes() here if it depends on airportList
            // populateFlightAirportComboBoxes();
        } catch (ListException e) {
            appendFlightOutput("Error al cargar aeropuertos: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            appendFlightOutput("Error inesperado al cargar aeropuertos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void saveFlights() {
        try {
            // Assuming FileReader.saveFlights() takes a SinglyLinkedList<Flight>
            FileReader.saveFlights(flightsList);
            appendFlightOutput("Vuelos guardados exitosamente.");
        } catch (Exception e) { // Catching a general Exception for broad error handling
            appendFlightOutput("Error al guardar vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}