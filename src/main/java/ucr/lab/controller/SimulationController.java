package ucr.lab.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.TDA.stack.LinkedStack;
import ucr.lab.TDA.stack.StackException;
import ucr.lab.data.AirportManager;
import ucr.lab.data.FlightManager;
import ucr.lab.data.RoutesManager;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.User;
import ucr.lab.utility.FXUtil;
import ucr.lab.utility.GraphUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SimulationController {
    @FXML
    private ComboBox<String> flightCBox;
    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane ap;
    private Alert alert;
    private List<Flight> flightsList;
    private LinkedQueue bitacora = new LinkedQueue(); // Bitacora
    private final String RUTA_BITACORA = "bitacora.txt"; // Log
    private User loggedInAdmin;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @FXML
    private TextArea textArea;

    private void registrarEnBitacora(String mensaje) {
        String nombre = (loggedInAdmin != null) ? loggedInAdmin.getName() : "System"; // system para la compu
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entrada = "[" + timestamp + "] " + nombre + ": " + mensaje;
        try {
            bitacora.enQueue(entrada);
            try (FileWriter fw = new FileWriter(RUTA_BITACORA, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(entrada);
            }
        } catch (IOException | QueueException e) {
            System.err.println(LocalDateTime.now().format(FORMATTER) + " Error al registrar en bitácora: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() throws ListException {
        try {
            FlightManager.loadFlights();
            RoutesManager.loadRoutes();
            AirportManager.loadAirports();
        } catch (IOException e) {
            registrarEnBitacora("Error durante la inicialización de SimulationController: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (GraphException e) {
            registrarEnBitacora("Error al cargar rutas durante la inicialización de SimulationController: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.flightsList = FlightManager.getFlights().toList();

        registrarEnBitacora("SimulationController inicializado.");

        loadComboBox();

        this.alert = FXUtil.alert("Simulation Flights - Error","");
    }

    private void loadComboBox() {
        try {
            flightCBox.getItems().clear();
            if (flightsList.isEmpty()) {
                registrarEnBitacora("No hay vuelos para mostrar en la interfaz.");
                return;
            }
            registrarEnBitacora("Cargando lista de vuelos.");
            for (Flight flight : flightsList)
                if (!flight.getStatus().equals("Complete"))
                    flightCBox.getItems().add("" + flight.getNumber());
            registrarEnBitacora("Vuelos cargados en el ComboBox.");
        } catch (ClassCastException e) {
            String errorMessage = "Error de tipo de dato. Asegúrese que SinglyLinkedList contiene objetos Flight. " + e.getMessage();
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        } catch (Exception e) {
            String errorMessage = "Error inesperado al poblar ComboBox de vuelos: " + e.getMessage();
            registrarEnBitacora(errorMessage);
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSimulate() {
        if (flightsList.isEmpty()) {
            alert.setContentText("No hay vuelos disponibles para iniciar la simulacion.");
            alert.show();
            registrarEnBitacora("No hay vuelos disponibles para iniciar la simulacion.");
        } else if (flightCBox.getSelectionModel().getSelectedItem().isEmpty()) {
            alert.setContentText("Selecione un vuelo para iniciar la simulacion.");
            alert.show();
            registrarEnBitacora("Vuelo no seleccionado para iniciar la simulacion.");
        } else {
            int flightNumber = Integer.parseInt(flightCBox.getSelectionModel().getSelectedItem());
            try {
                int index = FlightManager.getFlights().indexOf(new Flight(flightNumber));
                Flight flight = FlightManager.getFlights().getFlight(index-1);
                if (GraphUtil.isReachable(RoutesManager.getRoutesGraph(), flight.getOriginAirportCode(), flight.getDestinationAirportCode())) {
                    SinglyLinkedList path = GraphUtil.dijkstra(flight.getOriginAirportCode(), flight.getDestinationAirportCode(), RoutesManager.getRoutesGraph());
                    int size = path.size();
                    AirportManager.loadAirports();
                    for (int i = 1; i < size; i++) {
                        int index2 = AirportManager.getAirports().indexOf(new AirPort((Integer) path.get(i)));
                        AirPort airPort = (AirPort) AirportManager.getAirports().getNode(index2).data;
                        if (airPort.getStatus().equals("Inactivo")){
                            alert.setContentText("Aeropuerto inactivo [" + airPort.getCode() + "] " + airPort.getName() + " necesario para iniciar la simulacion.");
                            alert.show();
                            registrarEnBitacora("Aeropuerto inactivo [" + airPort.getCode() + "] " + airPort.getName() + " necesario para iniciar la simulacion.");
                            return;
                        }
                    }
                    registrarEnBitacora("Iniciando simulacion de vuelo.");
                    textArea.setText("Iniciando simulacion de vuelo [" + flight.getNumber() + "]");
                    drawPath(path, () -> {
                        registrarEnBitacora("Ruta dibujada.");
                        textArea.appendText("\n\nEl vuelo llegó exitosamente a su destino. Desembarcando pasajeros.");

                        try {
                            LinkedStack disembarks = new LinkedStack();
                            for (Object o : flight.getPassengerIDsAsList()) {
                                disembarks.push(o);
                                textArea.appendText("\nPasajero [" + o.toString() + "] bajó del avión.");
                            }

                            flight.setStatus("Complete");
                            StringBuilder result = new StringBuilder();
                            for (int i = 1; i <= path.size(); i++) {
                                result.append(path.getNode(i).data);
                                if (i < path.size()) result.append(" -> ");
                            }
                            flight.setRoute(result.toString());
                            FlightManager.getFlights().remove(index);
                            FlightManager.getFlights().add(flight);
                            FlightManager.saveFlights();
                            FlightManager.loadFlights();
                        } catch (ListException | StackException | IOException e) {
                            registrarEnBitacora("Error inesperado: " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                        registrarEnBitacora("Estado de vuelo cambiado a completado. Ruta de vuelo ingresada.");
                        textArea.appendText("\n\nVuelo completado!");
                    });
                    loadComboBox();
                } else {
                    alert.setContentText("El aeropuerto origen no tiene ruta posible hacia el aeropuerto destino.");
                    alert.show();
                    registrarEnBitacora("Simulacion detenida: El aeropuerto origen no tiene ruta posible hacia el aeropuerto destino.");
                }
            } catch (ListException e) {
                registrarEnBitacora("Error inesperado al procesar lista: " + e.getMessage());
                throw new RuntimeException(e);
            } catch (ClassCastException e) {
                String errorMessage = "Error de tipo de dato. Asegúrese que SinglyLinkedList contiene objetos adecuados. " + e.getMessage();
                registrarEnBitacora(errorMessage);
                e.printStackTrace();
            } catch (Exception e) {
                registrarEnBitacora("Error ejecutar algoritmo de dijkstra: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void drawPath (SinglyLinkedList path, Runnable onFinished) throws ListException {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image image = new Image(getClass().getResourceAsStream("/ucr/lab/aeropuerto.png"));
        FXUtil.animateDijkstraPath(gc, path, 80, 100, true, image, 60, textArea, onFinished);
    }
}
