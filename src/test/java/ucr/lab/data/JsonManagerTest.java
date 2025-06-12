package ucr.lab.data;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.User;
import ucr.lab.utility.Util;

import java.io.IOException;
import java.time.LocalDateTime;

class JsonManagerTest {
    @Test
    void addFlight() {
        try {
            AirportManager.loadAirports();
            DoublyLinkedList airports = AirportManager.getAirports();
            int size = airports.size();
            CircularDoublyLinkedList flights = new CircularDoublyLinkedList();
            for (int i = 1; i < 10; i++) {
                AirPort aux = (AirPort) airports.getNode(Util.random(size)).getData();
                int originCode = aux.getCode();
                aux = (AirPort) airports.getNode(Util.random(size)).getData();
                int destinationCode = aux.getCode();
                if (originCode != destinationCode)
                    flights.add(new Flight(i, originCode, destinationCode, LocalDateTime.now(),
                            Util.random(2, 4)*50, 0, "Active"));
            }
            FlightManager.setFlights(flights);
            FlightManager.saveFlights();
        } catch (IOException | ListException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadFlights() {
        System.out.println("Loading Flights");
        try {
            FlightManager.loadFlights();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(FlightManager.getFlights());
    }

    @Test
    void loadAirport() {
        System.out.println("Loading Airport");
        try {
            AirportManager.loadAirports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(AirportManager.getAirports());
    }

    @Test
    void addUsers() {
        try {
            UserManager.getUsers().clear();
            UserManager.saveUsers();
            UserManager.add(new User(1, "usuario", "usuario123", "usuario@correo.com", "user"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadUser() {
        System.out.println("Loading Users");
        try {
            UserManager.loadUsers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(UserManager.getUsers());
    }
}