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

public class RouteController {

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
    public RouteController() {
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



        loadAirportsAndPopulateComboBoxes();



        loadAllRoutes();


        buildDijkstraGraph();
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

}