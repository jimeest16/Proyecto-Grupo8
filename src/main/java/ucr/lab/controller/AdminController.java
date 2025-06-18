package ucr.lab.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AdminController {

    // Campos FXML para los contenedores de cada pestaña

    @FXML
    private AnchorPane usersTabContentPane;     // Contenedor para el contenido de la pestaña de usuarios
    @FXML
    private AnchorPane flightsTabContentPane;   // Contenedor para el contenido de la pestaña de vuelos
    @FXML
    private AnchorPane routesTabContentPane;    // Contenedor para el contenido de la pestaña de rutas
    @FXML
    private AnchorPane airportsTabContentPane;  // Contenedor para el contenido de la pestaña de aeropuertos


    public AdminController() {

    }

    @FXML
    public void initialize() {

        System.out.println("AdminController inicializado.");
    }


    @FXML
    private void userManager(Event event) {

        if (((Tab) event.getSource()).isSelected()) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/UserManagerView.fxml"));
                Parent userContent = loader.load(); // Carga el contenido del FXML

                if (usersTabContentPane != null) {
                    usersTabContentPane.getChildren().setAll(userContent);
                    System.out.println("Pestaña 'Manage Users' cargada.");
                } else {
                    System.err.println("Error: usersTabContentPane no está inicializado en FXML.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de pasajeros: " + e.getMessage());
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
                } else {
                    System.err.println("Error: flightsTabContentPane no está inicializado en FXML.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de vuelos: " + e.getMessage());
            }
        }
    }


    @FXML
    private void routesManager(Event event) {
        if (((Tab) event.getSource()).isSelected()) {
            try {
                // Especifica la ruta a tu FXML de gestión de rutas (ej. RouteManagerModern.fxml)
                // y su controlador asociado (RouteController)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ucr/lab/RouteManagerView.fxml"));
                Parent routeContent = loader.load();

                if (routesTabContentPane != null) {
                    routesTabContentPane.getChildren().setAll(routeContent);
                    System.out.println("Pestaña 'Manage Routes' cargada.");
                } else {
                    System.err.println("Error: routesTabContentPane no está inicializado en FXML.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de rutas: " + e.getMessage());
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
                } else {
                    System.err.println("Error: airportsTabContentPane no está inicializado en FXML.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error al cargar el FXML de gestión de aeropuertos: " + e.getMessage());
            }
        }
    }

    @FXML
    private void logout() {
        Platform.exit();
    }
}