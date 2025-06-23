package ucr.lab.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminController {
    @FXML
    private TabPane tabPanePrincipal;

    @FXML
    private Tab airportsTab;

    @FXML private AnchorPane usersTabContentPane;
    @FXML private AnchorPane flightsTabContentPane;
    @FXML private AnchorPane routesTabContentPane;
    @FXML private AnchorPane airportsTabContentPane;
    @FXML private AnchorPane simulationTabContentPane;

    private LinkedQueue bitacora = new LinkedQueue();
    private final String RUTA_BITACORA = "bitacora.txt";
    private User loggedInAdmin;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formatter for date and time

    public AdminController(User loggedInAdmin) {
        this.loggedInAdmin = loggedInAdmin;
    }

    public void setAirportsTabContentPane(AnchorPane airportsTabContentPane) {
        this.airportsTabContentPane = airportsTabContentPane;
    }
    public AnchorPane getAirportsTabContentPane() {
        return airportsTabContentPane;
    }

    public AdminController() {
    }

    private void registrarEnBitacora(String mensaje) {
        String nombre = (loggedInAdmin != null) ? loggedInAdmin.getName() : "Admin";
        String timestamp = LocalDateTime.now().format(FORMATTER); // Get current date and time
        String entrada = "[" + timestamp + "] " + nombre + ": " + mensaje; // Include timestamp
        try {
            bitacora.enQueue(entrada);
            try (FileWriter fw = new FileWriter(RUTA_BITACORA, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(entrada);
            }
        } catch (IOException | QueueException e) {
            System.err.println("Error al registrar en bitácora: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        System.out.println(LocalDateTime.now().format(FORMATTER) + " AdminController initialized.");
        registrarEnBitacora("Panel de administración iniciado.");
    }

    @FXML
    private void userManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/UserManagerView.fxml"));
                Parent userContent = loader.load();

                if (usersTabContentPane != null) {
                    usersTabContentPane.getChildren().setAll(userContent);
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " Pestaña 'Manage Users' cargada.");
                    registrarEnBitacora("Pestaña 'Gestionar Usuarios' cargada.");
                } else {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " usersTabContentPane no inicializado.");
                    registrarEnBitacora("Error: usersTabContentPane no inicializado.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                registrarEnBitacora("Error al cargar 'Gestionar Usuarios': " + e.getMessage());
            }
        }
    }

    @FXML
    private void flightManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/FlightManagerView.fxml"));
                Parent flightContent = loader.load();

                if (flightsTabContentPane != null) {
                    flightsTabContentPane.getChildren().setAll(flightContent);
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " Pestaña 'Manage Flights' cargada.");
                    registrarEnBitacora("Pestaña 'Gestionar Vuelos' cargada.");
                } else {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " flightsTabContentPane no inicializado.");
                    registrarEnBitacora("Error: flightsTabContentPane no inicializado.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                registrarEnBitacora("Error al cargar 'Gestionar Vuelos': " + e.getMessage());
            }
        }
    }

    @FXML
    private void routesManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/RouteManagerView.fxml"));
                Parent routeContent = loader.load();

                if (routesTabContentPane != null) {
                    routesTabContentPane.getChildren().setAll(routeContent);
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " Pestaña 'Manage Routes' cargada.");
                    registrarEnBitacora("Pestaña 'Gestionar Rutas' cargada.");
                } else {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " routesTabContentPane no inicializado.");
                    registrarEnBitacora("Error: routesTabContentPane no inicializado.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                registrarEnBitacora("Error al cargar 'Gestionar Rutas': " + e.getMessage());
            }
        }
    }

    @FXML
    private void airportManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/AirPortView.fxml"));
                Parent airportContent = loader.load();

                if (airportsTabContentPane != null) {
                    airportsTabContentPane.getChildren().setAll(airportContent);
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " Pestaña 'Manage Airports' cargada.");
                    registrarEnBitacora("Pestaña 'Gestionar Aeropuertos' cargada.");
                } else {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " airportsTabContentPane no inicializado.");
                    registrarEnBitacora("Error: airportsTabContentPane no inicializado.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                registrarEnBitacora("Error al cargar 'Gestionar Aeropuertos': " + e.getMessage());
            }
        }
    }

    @FXML
    private void logout() {
        registrarEnBitacora("Cierre de sesión del administrador.");
        Platform.exit();
    }

    public void mostrarTabDeAirports() {
        if (tabPanePrincipal != null && airportsTab != null) {
            tabPanePrincipal.getSelectionModel().select(airportsTab);
        } else {
            System.out.println("No se encontró el TabPane o la pestaña de aeropuertos.");
        }
    }

    @FXML
    public void simulationManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/simulation.fxml"));
                Parent simulationContent = loader.load();

                if (simulationTabContentPane != null) {
                    simulationTabContentPane.getChildren().setAll(simulationContent);
                    System.out.println(LocalDateTime.now().format(FORMATTER) + " Pestaña 'Simulate Flight' cargada.");
                    registrarEnBitacora("Pestaña 'Simulate Flight' cargada.");
                } else {
                    System.err.println(LocalDateTime.now().format(FORMATTER) + " simulationsTabContentPane no inicializado.");
                    registrarEnBitacora("Error: simulationsTabContentPane no inicializado.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                registrarEnBitacora("Error al cargar 'Simulate Flight': " + e.getMessage());
            }
        }
    }
}