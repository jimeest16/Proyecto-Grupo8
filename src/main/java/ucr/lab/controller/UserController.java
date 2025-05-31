package ucr.lab.controller;

import javafx.application.Platform;

public class UserController {

    public void searchFlights() {
        System.out.println("Buscando vuelos...");
        // tendria que ir la funcionalidad que va en passenger

    }

    public void makeReservation() {
        System.out.println("Realizando reserva...");
    }

    public void viewHistory() {
        System.out.println("Mostrando historial de vuelos...");
    }

    public void logout() {
        Platform.exit();
    }
}

