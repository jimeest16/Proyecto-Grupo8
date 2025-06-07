package ucr.lab.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import ucr.lab.TDA.CircularDoublyLinkedList;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;

import java.util.List;

// Estructura de la clase:
//Selección de aeropuerto de origen y destino.>combo box
// validar que origen y detino no sean iguales
//si no hya cupo, se encola al sihuinte uelo_ linked queue


public class UserController {

    // Botones
    @FXML private Button btnSearchFlights1;
    @FXML private Button btnMakeReservation1;
    @FXML private Button btnViewHistory;
    @FXML private Button btnLogout;

    // Labels
    @FXML private Label labelWelcome;
    @FXML private Label labelHistory;
    @FXML private Label labelGoodbye;

    // Textos
    @FXML private Text textIntro;
    @FXML private Text textHistory;
    @FXML private Text textThanks;

    // ComboBox para aeropuertos
    @FXML private ComboBox<String> comboOrigin;
    @FXML private ComboBox<String> comboDestination;

    // Tabla para historial o resultados
    @FXML private TableView<?> tableFlightInfo;

    //clases
    private Passenger pasajeroActual;
    private List<AirPort> aeropuertos;
    private CircularDoublyLinkedList<Flight> vuelos;

    @FXML
    public void searchFlights() {
        System.out.println("Buscando vuelos...");
        String origin = comboOrigin.getValue();
        String destination = comboDestination.getValue();

        if (origin == null || destination == null) {
            System.out.println("Debe seleccionar ambos aeropuertos.");
        } else if (origin.equals(destination)) {
            System.out.println("Origen y destino no pueden ser iguales.");
        } else {
            System.out.println("Buscando vuelos desde " + origin + " hasta " + destination + "...");
            // Aquí se podría verificar disponibilidad y, si no hay cupo, encolar al siguiente vuelo
        }
    }

    @FXML
    public void makeReservation() {
        System.out.println("Realizando reserva...");
        // Aquí iría la lógica de reserva
    }

    @FXML
    public void viewHistory() {
        System.out.println("Mostrando historial de vuelos...");
        // Mostrar historial
    }

    @FXML
    public void logout() {
        Platform.exit();
    }

}