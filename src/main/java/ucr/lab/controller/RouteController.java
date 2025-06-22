package ucr.lab.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import net.sf.jasperreports.engine.JRException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.data.FlightManager;
import ucr.lab.domain.*;
import ucr.lab.utility.AirPortDatos;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.Dijkstra;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ucr.lab.utility.Util;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.List;

public class RouteController {

    private AVLTree passengerTree;
    private AVLTree avlTree;
    private SinglyLinkedList airportList;
    private SinglyLinkedList routeList;
    private Dijkstra airportGraph;
    private SinglyLinkedList flightsList;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtNationality;
    @FXML private TextArea txtOutput;

    @FXML private TextArea textAreaContent;
    private SinglyLinkedList drawnRoutesInfo;

    @FXML private TextField idFlightNumber;
    @FXML private ComboBox<String> cmbFlightOrigin;
    @FXML private ComboBox<String> cmbFlightDestination;
    @FXML private DatePicker dpFlightDepartureDate;
    @FXML private TextField txtFlightDepartureTime;
    @FXML private TextField txtFlightCapacity;
    @FXML private Label lblFlightStatus;
    @FXML private TextField txtPassengerIdToAssign;
    @FXML private TextArea txtFlightOutput;

    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOriginCode;
    @FXML private TextField txtDestinationCode;
    @FXML private TextField txtDepartureTimeHour;
    @FXML private DatePicker dpDepartureDate;
    @FXML private TextField txtCapacity;
    @FXML private TextField txtOccupancy;
    @FXML private TextField txtFlightStatus;
    @FXML private TextField txtRoute;

    @FXML private ComboBox<String> cmbOrigin;
    @FXML private ComboBox<String> cmbDestination;
    @FXML private TextField txtDistance;
    @FXML private TextArea textArea;

    @FXML private Canvas graphCanvas;
    private Random random;
    private GraphicsContext gc;

    public RouteController() {
        this.passengerTree = new AVLTree();
        this.avlTree = new AVLTree();
        this.airportList = new SinglyLinkedList();
        this.routeList = new SinglyLinkedList();
        this.airportGraph = new Dijkstra();
        this.flightsList = new SinglyLinkedList();
        this.random = new Random();
        this.drawnRoutesInfo = new SinglyLinkedList();
    }

    @FXML
    public void initialize() throws ListException {
        if (passengerTree == null) passengerTree = new AVLTree();
        if (avlTree == null) avlTree = new AVLTree();
        if (airportList == null) airportList = new SinglyLinkedList();
        if (routeList == null) routeList = new SinglyLinkedList();
        if (airportGraph == null) airportGraph = new Dijkstra();
        if (flightsList == null) flightsList = new SinglyLinkedList();
        if (random == null) random = new Random();

        if (graphCanvas != null) {
            gc = graphCanvas.getGraphicsContext2D();

            graphCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    graphCanvas.widthProperty().bind(newScene.widthProperty().multiply(0.4));
                    graphCanvas.heightProperty().bind(newScene.heightProperty().multiply(0.6));
                    graphCanvas.setOnMouseClicked(event -> handleCanvasClick(event));

                    graphCanvas.widthProperty().addListener((o, oldVal, newVal) -> drawGraph(null));
                    graphCanvas.heightProperty().addListener((o, oldVal, newVal) -> drawGraph(null));

                    try {
                        drawGraph(null);
                    } catch (Exception e) {
                        System.err.println("Error al dibujar el grafo inicial después de añadir a la escena: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }

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
                    String airportDisplay = airport.getCode() + " - " + airport.getName();
                    cmbOrigin.getItems().add(airportDisplay);
                    cmbDestination.getItems().add(airportDisplay);
                }
                appendRoutesOutput("Aeropuertos cargados y ComboBoxes poblados.");
            } catch (ListException e) {
                appendRoutesOutput("Error al cargar aeropuertos para los ComboBoxes: " + e.getMessage());
            } catch (ClassCastException e) {
                appendRoutesOutput("Error de tipo de dato inesperado en SinglyLinkedList de aeropuertos: " + e.getMessage());
            } catch (Exception e) {
                appendRoutesOutput("Error inesperado al poblar los ComboBoxes de aeropuertos: " + e.getMessage());
            }
        }
    }

    private String getAirportNameByCode(int code) {
        try {
            for (int i = 1; i <= airportList.size(); i++) {
                AirPort airport = (AirPort) airportList.get(i);
                if (airport.getCode() == code) {
                    return airport.getName();
                }
            }
        } catch (ListException e) {
            System.err.println("Error al buscar nombre de aeropuerto por código: " + e.getMessage());
        }
        return "Desconocido (" + code + ")";
    }

    private void loadAllRoutes() throws ListException {
        routeList = FileReader.loadRoutes();
        textArea.clear();
        appendRoutesOutput("Rutas cargadas desde 'rutas.json': " + routeList.size());
        try {
            if (!routeList.isEmpty()) {
                appendRoutesOutput("=== Rutas Existentes (Origen -> [Destinos, Distancia]) ===");
                for (int i = 1; i <= routeList.size(); i++) {
                    Route r = (Route) routeList.get(i);
                    StringBuilder routeInfo = new StringBuilder();
                    routeInfo.append("Origen: ").append(getAirportNameByCode(r.getOriginAirportCode()));
                    if (r.getDestinationList() != null && !r.getDestinationList().isEmpty()) {
                        routeInfo.append(" -> Destinos: ");
                        for (int j = 1; j <= r.getDestinationList().size(); j++) {
                            Destination d = (Destination) r.getDestinationList().get(j);
                            routeInfo.append(getAirportNameByCode(d.getAirportCode()))
                                    .append(" (").append(String.format("%.0f", d.getDistance())).append("km)");
                            if (j < r.getDestinationList().size()) {
                                routeInfo.append(", ");
                            }
                        }
                    } else {
                        routeInfo.append(" -> Sin destinos directos.");
                    }
                    appendRoutesOutput(routeInfo.toString());
                }
            } else {
                appendRoutesOutput("No hay rutas cargadas.");
            }
        } catch (ListException e) {
            appendRoutesOutput("Error al listar rutas cargadas: " + e.getMessage());
        }
    }

    private void buildDijkstraGraph() {
        airportGraph.clear();

        try {
            SinglyLinkedList uniqueAirportCodes = new SinglyLinkedList();
            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                boolean originExists = false;
                for(int k=1; k<=uniqueAirportCodes.size(); k++) {
                    if ((int)uniqueAirportCodes.get(k) == r.getOriginAirportCode()) {
                        originExists = true;
                        break;
                    }
                }
                if (!originExists) {
                    uniqueAirportCodes.add(r.getOriginAirportCode());
                }

                if (r.getDestinationList() != null) {
                    for (int j = 1; j <= r.getDestinationList().size(); j++) {
                        Destination d = (Destination) r.getDestinationList().get(j);
                        boolean destExists = false;
                        for(int k=1; k<=uniqueAirportCodes.size(); k++) {
                            if ((int)uniqueAirportCodes.get(k) == d.getAirportCode()) {
                                destExists = true;
                                break;
                            }
                        }
                        if (!destExists) {
                            uniqueAirportCodes.add(d.getAirportCode());
                        }
                    }
                }
            }

            for (int i = 1; i <= uniqueAirportCodes.size(); i++) {
                Integer code = (Integer) uniqueAirportCodes.get(i);
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
            appendRoutesOutput("Error al construir el grafo de Dijkstra: " + e.getMessage());
        } catch (GraphException e) {
            appendRoutesOutput("Error en el grafo al construir el grafo: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al construir el grafo de Dijkstra: " + e.getMessage());
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

    private void appendContentOutput(String text) {
        if (textAreaContent != null) {
            textAreaContent.setText(text);
        }
    }

    @FXML
    private void clearFields(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtNationality.clear();

        idFlightNumber.clear();
        if (cmbFlightOrigin != null) cmbFlightOrigin.getSelectionModel().clearSelection();
        if (cmbFlightDestination != null) cmbFlightDestination.getSelectionModel().clearSelection();
        if (dpFlightDepartureDate != null) dpFlightDepartureDate.setValue(null);
        txtFlightDepartureTime.clear();
        txtFlightCapacity.clear();
        lblFlightStatus.setText("");
        txtPassengerIdToAssign.clear();
        txtFlightOutput.clear();

        txtFlightNumber.clear();
        txtOriginCode.clear();
        txtDestinationCode.clear();
        txtDepartureTimeHour.clear();
        if (dpDepartureDate != null) dpDepartureDate.setValue(null);
        txtCapacity.clear();
        txtOccupancy.clear();
        txtFlightStatus.clear();
        txtRoute.clear();
        if (textAreaContent != null) textAreaContent.clear();
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

            if (originAirportCode == -1 || destinationAirportCode == -1) {
                appendRoutesOutput("Error: No se pudo extraer el código numérico del aeropuerto de la selección.");
                return;
            }

            Route existingRoute = null;
            for (int i = 1; i <= routeList.size(); i++) {
                Route r = (Route) routeList.get(i);
                if (r.getOriginAirportCode() == originAirportCode) {
                    existingRoute = r;
                    break;
                }
            }

            Destination newDestination = new Destination(destinationAirportCode, distance);

            if (existingRoute != null) {
                SinglyLinkedList destList = existingRoute.getDestinationList();
                boolean destinationExists = false;
                for (int i = 1; i <= destList.size(); i++) {
                    Destination d = (Destination) destList.get(i);
                    if (d.getAirportCode() == destinationAirportCode) {
                        d.setDistance(distance);
                        destinationExists = true;
                        appendRoutesOutput("Actualizada la distancia para el destino " + getAirportNameByCode(destinationAirportCode) + " desde el origen " + getAirportNameByCode(originAirportCode));
                        break;
                    }
                }
                if (!destinationExists) {
                    destList.add(newDestination);
                }
            } else {
                SinglyLinkedList newDestList = new SinglyLinkedList();
                newDestList.add(newDestination);
                Route newRoute = new Route(originAirportCode, newDestList);
                routeList.add(newRoute);
                appendRoutesOutput("Agregada nueva ruta de origen " + getAirportNameByCode(originAirportCode) + " con destino " + getAirportNameByCode(destinationAirportCode));
            }

            List<Route> routesToSave = FileReader.convertSinglyLinkedListToRouteList(routeList);
            FileReader.saveRoutes(routesToSave);

            clearRouteFields();
            loadAllRoutes();
            buildDijkstraGraph();
            drawGraph(actionEvent);
        } catch (NumberFormatException e) {
            appendRoutesOutput("Distancia inválida o código de aeropuerto no numérico.");
        } catch (ListException e) {
            appendRoutesOutput("Error al manipular la lista de rutas en memoria: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al añadir/modificar la ruta: " + e.getMessage());
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
                    SinglyLinkedList destList = r.getDestinationList();
                    for (int j = 1; j <= destList.size(); j++) {
                        Destination d = (Destination) destList.get(j);
                        if (d.getAirportCode() == destinationAirportCode) {
                            d.setDistance(newDistance);
                            routeModified = true;
                            appendRoutesOutput("Distancia actualizada para la ruta: Origen " + getAirportNameByCode(originAirportCode) + " -> Destino " + getAirportNameByCode(destinationAirportCode) + " a " + newDistance + "km.");
                            break;
                        }
                    }
                    if (routeModified) {
                        break;
                    }
                }
            }

            if (routeModified) {
                List<Route> routesToSave = FileReader.convertSinglyLinkedListToRouteList(routeList);
                FileReader.saveRoutes(routesToSave);
                clearRouteFields();
                loadAllRoutes();
                buildDijkstraGraph();
                drawGraph(actionEvent);
            } else {
                appendRoutesOutput("No se encontró la ruta directa de Origen " + getAirportNameByCode(originAirportCode) + " a Destino " + getAirportNameByCode(destinationAirportCode) + " para modificar.");
                appendRoutesOutput("Considera usar 'Añadir Ruta' si es una conexión nueva.");
            }

        } catch (NumberFormatException e) {
            appendRoutesOutput("Distancia inválida o código de aeropuerto no numérico.");
        } catch (ListException e) {
            appendRoutesOutput("Error al acceder a la lista de rutas para modificar: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al modificar la ruta: " + e.getMessage());
        }
    }

    private int extractAirportCode(String airportString) {
        if (airportString == null || airportString.isEmpty()) return -1;
        try {
            int dashIndex = airportString.indexOf(" - ");
            if (dashIndex != -1) {
                String codeStr = airportString.substring(0, dashIndex).trim();
                return Integer.parseInt(codeStr);
            }
            return -1;
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            System.err.println("Error al extraer el código del aeropuerto de: '" + airportString + "' - " + e.getMessage());
            return -1;
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

            appendRoutesOutput("Encontrando la ruta más corta de " + getAirportNameByCode(originCode) + " a " + getAirportNameByCode(destinationCode) + "...");

            if (airportGraph.isEmpty()) {
                appendRoutesOutput("Error: El grafo de rutas está vacío. No se pueden calcular rutas.");
                return;
            }
            if (!airportGraph.containsVertex(originCode)) {
                appendRoutesOutput("Error: El aeropuerto de origen " + getAirportNameByCode(originCode) + " no existe en el grafo de rutas.");
                return;
            }
            if (!airportGraph.containsVertex(destinationCode)) {
                appendRoutesOutput("Error: El aeropuerto de destino " + getAirportNameByCode(destinationCode) + " no existe en el grafo de rutas.");
                return;
            }

            SinglyLinkedList shortestPathCodes = airportGraph.dijkstra(originCode, destinationCode);
            double totalDistance = airportGraph.getLastCalculatedDistance();

            if (totalDistance != Double.MAX_VALUE) {
                StringBuilder pathString = new StringBuilder();
                if (shortestPathCodes != null && !shortestPathCodes.isEmpty()) {
                    for (int i = 1; i <= shortestPathCodes.size(); i++) {
                        int currentCode = (int) shortestPathCodes.get(i);
                        pathString.append(getAirportNameByCode(currentCode));
                        if (i < shortestPathCodes.size()) {
                            pathString.append(" -> ");
                        }
                    }
                } else {
                    pathString.append("La ruta es directa o no se pudo construir la lista de pasos.");
                }

                appendRoutesOutput("Ruta encontrada: " + pathString.toString());
                appendRoutesOutput("Distancia total: " + String.format("%.2f", totalDistance) + " km.");
            } else {
                appendRoutesOutput("No se encontró una ruta de " + getAirportNameByCode(originCode) + " a " + getAirportNameByCode(destinationCode) + ". No hay conexión posible.");
            }

        } catch (GraphException e) {
            appendRoutesOutput("Error en el grafo al buscar la ruta más corta: " + e.getMessage());
        } catch (ListException e) {
            appendRoutesOutput("Error en la lista al buscar la ruta más corta: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al buscar la ruta más corta: " + e.getMessage());
        }
    }

    @FXML
    public void generateRandomRoutes(ActionEvent actionEvent) {
        appendRoutesOutput("Generando rutas aleatorias...");
        random = new Random();

        try {
            if (airportList.isEmpty()) {
                appendRoutesOutput("No hay aeropuertos cargados para generar rutas.");
                showAlert("Generación Fallida", "No hay aeropuertos disponibles.", "Carga aeropuertos desde un archivo antes de intentar generar rutas aleatorias.");
                return;
            }

            routeList.clear();
            appendRoutesOutput("Rutas existentes limpiadas para la generación aleatoria.");

            SinglyLinkedList shuffledAirports = new SinglyLinkedList();
            for(int i=1; i<=airportList.size(); i++) {
                shuffledAirports.add(airportList.get(i));
            }

            int routesAddedCount = 0;
            int maxRutasPorOrigen = 3;
            double probConexion = 0.4;

            for (int i = 1; i <= airportList.size(); i++) {
                AirPort origenAirport = (AirPort) airportList.get(i);
                int rutasSalientesActuales = 0;

                for (int j = 1; j <= airportList.size(); j++) {
                    if (i == j || rutasSalientesActuales >= maxRutasPorOrigen) {
                        continue;
                    }

                    AirPort destinoAirport = (AirPort) airportList.get(j);

                    if (random.nextDouble() < probConexion) {
                        double distance = 50 + (2000 - 50) * random.nextDouble();
                        distance = Math.round(distance * 100.0) / 100.0;

                        Route existingRoute = null;
                        for (int k = 1; k <= routeList.size(); k++) {
                            Route r = (Route) routeList.get(k);
                            if (r.getOriginAirportCode() == origenAirport.getCode()) {
                                existingRoute = r;
                                break;
                            }
                        }

                        Destination newDestination = new Destination(destinoAirport.getCode(), distance);

                        if (existingRoute != null) {
                            SinglyLinkedList destList = existingRoute.getDestinationList();
                            boolean destinationExists = false;
                            for (int k = 1; k <= destList.size(); k++) {
                                Destination d = (Destination) destList.get(k);
                                if (d.getAirportCode() == destinoAirport.getCode()) {
                                    d.setDistance(distance);
                                    destinationExists = true;
                                    break;
                                }
                            }
                            if (!destinationExists) {
                                destList.add(newDestination);
                            }
                        } else {
                            SinglyLinkedList newDestList = new SinglyLinkedList();
                            newDestList.add(newDestination);
                            Route newRoute = new Route(origenAirport.getCode(), newDestList);
                            routeList.add(newRoute);
                        }
                        routesAddedCount++;
                        rutasSalientesActuales++;
                    }
                }
            }

            List<Route> routesToSave = FileReader.convertSinglyLinkedListToRouteList(routeList);
            FileReader.saveRoutes(routesToSave);

            appendRoutesOutput("Se generaron " + routesAddedCount + " rutas aleatorias y ponderadas.");
            loadAllRoutes();
            buildDijkstraGraph();
            drawGraph(actionEvent);
            appendRoutesOutput("Generación de rutas completada.");

        } catch (ListException e) {
            appendRoutesOutput("Error de lista al generar rutas aleatorias: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al generar rutas aleatorias: " + e.getMessage());
        }
    }

    @FXML
    public void drawGraph(ActionEvent event) {
        if (graphCanvas == null || gc == null) {
            appendRoutesOutput("Error: El Canvas o su GraphicsContext no están inicializados.");
            return;
        }

        if (airportList.isEmpty() || routeList.isEmpty()) {
            appendRoutesOutput("No hay aeropuertos o rutas para dibujar el grafo.");
            showAlert("Grafo Vacío", "No hay datos de aeropuertos o rutas.", "Carga aeropuertos y rutas desde un archivo, o genera rutas primero.");
            return;
        }

        gc.clearRect(0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());
        appendContentOutput("");
        drawnRoutesInfo.clear();

        double canvasWidth = graphCanvas.getWidth();
        double canvasHeight = graphCanvas.getHeight();
        double nodeImageSize = 30;
        double padding = 50;

        Image airplaneImage = null;
        try {
            airplaneImage = new Image(getClass().getResourceAsStream("/ucr/lab/aeropuerto2.png"));
            if (airplaneImage.isError()) {
                appendRoutesOutput("Error al cargar la imagen del avión: " + airplaneImage.getException().getMessage());
                showAlert("Error de Imagen", "No se pudo cargar la imagen del avión.", "Verifica la ruta y el nombre del archivo de imagen (ej. '/ucr/lab/aeropuerto2.png').");
                airplaneImage = null;
            }
        } catch (Exception e) {
            appendRoutesOutput("Excepción al cargar la imagen del avión: " + e.getMessage());
            showAlert("Error de Imagen", "Excepción al cargar la imagen del avión.", "Asegúrate de que la ruta a la imagen sea correcta y exista.");
            airplaneImage = null;
        }

        try {
            class AirportPositionData {
                int code;
                Point2D position;

                public AirportPositionData(int code, Point2D position) {
                    this.code = code;
                    this.position = position;
                }
            }

            SinglyLinkedList airportDrawingData = new SinglyLinkedList();
            double usableWidth = canvasWidth - 2 * padding;
            double usableHeight = canvasHeight - 2 * padding;

            if (usableWidth <= 0 || usableHeight <= 0) {
                appendRoutesOutput("Canvas es demasiado pequeño para dibujar con el padding especificado.");
                return;
            }

            for (int i = 1; i <= airportList.size(); i++) {
                AirPort airport = (AirPort) airportList.get(i);
                double x = padding + usableWidth * random.nextDouble();
                double y = padding + usableHeight * random.nextDouble();
                airportDrawingData.add(new AirportPositionData(airport.getCode(), new Point2D(x, y)));
            }

            gc.setStroke(Color.web("#2196F3"));
            gc.setLineWidth(2.5);

            for (int i = 1; i <= routeList.size(); i++) {
                Route route = (Route) routeList.get(i);
                int originCode = route.getOriginAirportCode();

                AirportPositionData originData = null;
                for (int k = 1; k <= airportDrawingData.size(); k++) {
                    AirportPositionData data = (AirportPositionData) airportDrawingData.get(k);
                    if (data.code == originCode) {
                        originData = data;
                        break;
                    }
                }

                if (originData == null) {
                    continue;
                }
                Point2D originPos = originData.position;

                SinglyLinkedList destinationList = route.getDestinationList();
                if (destinationList != null) {
                    for (int j = 1; j <= destinationList.size(); j++) {
                        Destination destination = (Destination) destinationList.get(j);
                        int destCode = destination.getAirportCode();
                        double distance = destination.getDistance();

                        AirportPositionData destData = null;
                        for (int k = 1; k <= airportDrawingData.size(); k++) {
                            AirportPositionData data = (AirportPositionData) airportDrawingData.get(k);
                            if (data.code == destCode) {
                                destData = data;
                                break;
                            }
                        }

                        if (destData == null) {
                            continue;
                        }
                        Point2D destPos = destData.position;

                        gc.strokeLine(originPos.getX(), originPos.getY(), destPos.getX(), destPos.getY());

                        gc.setFont(Font.font("Arial", 10));
                        gc.setFill(Color.DARKBLUE);
                        gc.fillText(String.format("%.0f km", distance), (originPos.getX() + destPos.getX()) / 2, (originPos.getY() + destPos.getY()) / 2);

                        double angle = Math.atan2(destPos.getY() - originPos.getY(), destPos.getX() - originPos.getX());
                        double arrowSize = 10;
                        double x1 = destPos.getX() - arrowSize * Math.cos(angle - Math.PI / 6);
                        double y1 = destPos.getY() - arrowSize * Math.sin(angle - Math.PI / 6);
                        double x2 = destPos.getX() - arrowSize * Math.cos(angle + Math.PI / 6);
                        double y2 = destPos.getY() - arrowSize * Math.sin(angle + Math.PI / 6);

                        gc.strokeLine(destPos.getX(), destPos.getY(), x1, y1);
                        gc.strokeLine(destPos.getX(), destPos.getY(), x2, y2);

                        drawnRoutesInfo.add(new DrawnRouteData(originPos, destPos, originCode, destCode, distance));
                    }
                }
            }

            for (int i = 1; i <= airportDrawingData.size(); i++) {
                AirportPositionData data = (AirportPositionData) airportDrawingData.get(i);
                int airportCode = data.code;
                Point2D position = data.position;
                String airportName = getAirportNameByCode(airportCode);

                if (airplaneImage != null) {
                    gc.drawImage(airplaneImage, position.getX() - nodeImageSize / 2, position.getY() - nodeImageSize / 2, nodeImageSize, nodeImageSize);
                } else {
                    gc.setFill(Color.web("#BBDEFB"));
                    gc.setStroke(Color.web("#0D47A1"));
                    gc.setLineWidth(2);
                    gc.fillOval(position.getX() - nodeImageSize / 2, position.getY() - nodeImageSize / 2, nodeImageSize, nodeImageSize);
                    gc.strokeOval(position.getX() - nodeImageSize / 2, position.getY() - nodeImageSize / 2, nodeImageSize, nodeImageSize);
                }

                gc.setFont(Font.font("Arial", 11));
                gc.setFill(Color.NAVY);
                gc.fillText(airportName, position.getX() - (airportName.length() * 3), position.getY() - nodeImageSize / 2 - 5);
            }

        } catch (ListException e) {
            appendRoutesOutput("Error al procesar la lista de aeropuertos o rutas: " + e.getMessage());
        } catch (Exception e) {
            appendRoutesOutput("Error inesperado al dibujar el grafo: " + e.getMessage());
        }
    }

    public void generateReport(ActionEvent actionEvent) throws JRException, IOException {
        showAlert("Cargando el Reporte...", "Espera un momento","");

        String jsonPath = "src/main/resources/data/flight.json";
        String jrxmlPath = "src/main/resources/jasper/routes.jrxml";
        String pdf = "src/main/resources/reportes/routes_report.pdf";

        List<Flight>routesList = FileReader.loadFlightsAsListForInternalUse(); //METODO PARA OBTENER las rutas más usadas

        Util.generarReporte(jsonPath,jrxmlPath,pdf, routesList);

    }

    private class DrawnRouteData {
        Point2D start;
        Point2D end;
        int originCode;
        int destCode;
        double distance;

        public DrawnRouteData(Point2D start, Point2D end, int originCode, int destCode, double distance) {
            this.start = start;
            this.end = end;
            this.originCode = originCode;
            this.destCode = destCode;
            this.distance = distance;
        }

        public boolean containsPoint(double px, double py, double tolerance) {
            double lineLength = start.distance(end);

            if (lineLength == 0.0) {
                return new Point2D(px, py).distance(start) < tolerance;
            }

            double t = ((px - start.getX()) * (end.getX() - start.getX()) +
                    (py - start.getY()) * (end.getY() - start.getY())) / (lineLength * lineLength);

            t = Math.max(0, Math.min(1, t));

            Point2D closest = new Point2D(start.getX() + t * (end.getX() - start.getX()),
                    start.getY() + t * (end.getY() - start.getY()));

            return new Point2D(px, py).distance(closest) < tolerance;
        }
    }

    private void handleCanvasClick(javafx.scene.input.MouseEvent event) {
        double clickX = event.getX();
        double clickY = event.getY();
        double tolerance = 5.0;

        try {
            for (int i = 1; i <= drawnRoutesInfo.size(); i++) {
                DrawnRouteData routeData = (DrawnRouteData) drawnRoutesInfo.get(i);
                if (routeData.containsPoint(clickX, clickY, tolerance)) {
                    String originName = getAirportNameByCode(routeData.originCode);
                    String destName = getAirportNameByCode(routeData.destCode);
                    String info = "Ruta seleccionada:\n" +
                            "Origen: " + originName + " (" + routeData.originCode + ")\n" +
                            "Destino: " + destName + " (" + routeData.destCode + ")\n" +
                            "Distancia: " + String.format("%.0f", routeData.distance) + " km";
                    appendContentOutput(info);
                    return;
                }
            }
            appendContentOutput("Haz clic en una ruta para ver sus detalles.");
        } catch (ListException e) {
            System.err.println("Error al iterar sobre drawnRoutesInfo: " + e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}