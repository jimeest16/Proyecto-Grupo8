package ucr.lab.controller;

import HistorialEventos.Sistema;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


import ucr.lab.domain.User;

public class AdminController {

    @FXML
    private AnchorPane usersTabContentPane;
    @FXML
    private AnchorPane flightsTabContentPane;
    @FXML
    private AnchorPane routesTabContentPane;
    @FXML
    private AnchorPane airportsTabContentPane;

    private Sistema sistemaBitacora;
    private User loggedInAdmin;

    public AdminController(Sistema sistema, User loggedInAdmin) {
        this.sistemaBitacora = sistema;
        this.loggedInAdmin = loggedInAdmin;
    }

    public AdminController() {
    }

    @FXML
    public void initialize() {
        System.out.println("AdminController initialized.");
        if (sistemaBitacora != null) {
            sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin Desconocido", "Panel de administración iniciado.");
        }
    }

    @FXML
    private void userManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/UserManagerView.fxml"));
                Parent userContent = loader.load();

                if (usersTabContentPane != null) {
                    usersTabContentPane.getChildren().setAll(userContent);
                    System.out.println("Pestaña 'Manage Users' cargada.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Pestaña 'Gestionar Usuarios' cargada.");
                    }
                } else {
                    System.err.println("Error: usersTabContentPane no está inicializado en FXML.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error: usersTabContentPane no inicializado al cargar 'Gestionar Usuarios'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de usuarios: " + e.getMessage());
                if (sistemaBitacora != null) {
                    sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error al cargar FXML 'Gestionar Usuarios': " + e.getMessage());
                }
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
                    System.out.println("Pestaña 'Manage Flights' cargada.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Pestaña 'Gestionar Vuelos' cargada.");
                    }
                } else {
                    System.err.println("Error: flightsTabContentPane no está inicializado en FXML.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error: flightsTabContentPane no inicializado al cargar 'Gestionar Vuelos'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de vuelos: " + e.getMessage());
                if (sistemaBitacora != null) {
                    sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error al cargar FXML 'Gestionar Vuelos': " + e.getMessage());
                }
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
                    System.out.println("Pestaña 'Manage Routes' cargada.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Pestaña 'Gestionar Rutas' cargada.");
                    }
                } else {
                    System.err.println("Error: routesTabContentPane no está inicializado en FXML.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error: routesTabContentPane no inicializado al cargar 'Gestionar Rutas'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de rutas: " + e.getMessage());
                if (sistemaBitacora != null) {
                    sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error al cargar FXML 'Gestionar Rutas': " + e.getMessage());
                }
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
                    System.out.println("Pestaña 'Manage Airports' cargada.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Pestaña 'Gestionar Aeropuertos' cargada.");
                    }
                } else {
                    System.err.println("Error: airportsTabContentPane no está inicializado en FXML.");
                    if (sistemaBitacora != null) {
                        sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error: airportsTabContentPane no inicializado al cargar 'Gestionar Aeropuertos'.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de aeropuertos: " + e.getMessage());
                if (sistemaBitacora != null) {
                    sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Error al cargar FXML 'Gestionar Aeropuertos': " + e.getMessage());
                }
            }
        }
    }

    public AnchorPane getAirportsTabContentPane() {
        return airportsTabContentPane;
    }

    public void setAirportsTabContentPane(AnchorPane airportsTabContentPane) {
        this.airportsTabContentPane = airportsTabContentPane;
    }

    @FXML
    private void logout() {
        if (sistemaBitacora != null) {
            sistemaBitacora.registrarEvento(loggedInAdmin != null ? loggedInAdmin.getName() : "Admin", "Cierre de sesión de administrador.");
        }
        Platform.exit();
    }
}
