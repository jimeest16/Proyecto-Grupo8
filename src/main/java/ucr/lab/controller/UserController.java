package ucr.lab.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

// Estructura de la clase:
//Selección de aeropuerto de origen y destino.>combo box
// validar que origen y detino no sean iguales
//si no hya cupo, se encola al sihuinte uelo_ linked queue

public class UserController {

    // Referencias a los componentes de la vista con fx:id
    @FXML
    private Button btnBuscarVuelos;

    @FXML
    private Button btnReservar;

    @FXML
    private Button btnVerHistorial;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblHistorial;

    @FXML
    private Label lblGracias;

    @FXML
    private Text txtInicio;

    @FXML
    private Text txtHistorial;

    @FXML
    private Text txtGracias;

    public void searchFlights() {
        System.out.println("Buscando vuelos...");
        // Aquí va la lógica para buscar vuelos (por ejemplo, validación y acceso a los datos)
    }

    public void makeReservation() {
        System.out.println("Realizando reserva...");
        // Aquí iría la lógica para realizar una reserva
    }

    public void viewHistory() {
        System.out.println("Mostrando historial de vuelos...");
        // Aquí se puede mostrar historial desde una base de datos o estructura en memoria
    }

    public void logout() {
        Platform.exit(); // Cierra la aplicación
    }
}