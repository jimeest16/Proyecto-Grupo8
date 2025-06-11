package ucr.lab.data;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.graph.GraphException;
import ucr.lab.TDA.graph.SinglyLinkedListGraph;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Airport;
import ucr.lab.domain.Flight;
import ucr.lab.domain.User;
import ucr.lab.utility.Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

class JsonManagerTest {
    @Test
    void addAirport() {
        DoublyLinkedList airports = new DoublyLinkedList();
        airports.add(new Airport(1, "Los Angeles International Airport", "USA", "Activo"));
        airports.add(new Airport(2, "John F. Kennedy International Airport", "USA", "Inactivo"));
        airports.add(new Airport(3, "Aeropuerto Internacional de San José", "COSTA RICA", "Activo"));
        airports.add(new Airport(4, "Ciudad Celeste International Airport", "TERRANOVA", "Inactivo"));
        airports.add(new Airport(5, "Narita International Airport", "JAPAN", "Activo"));
        airports.add(new Airport(6, "Frankfurt Airport", "GERMANY", "Activo"));
        airports.add(new Airport(7, "Aeropuerto de Buenos Aires", "ARGENTINA", "Inactivo"));
        airports.add(new Airport(8, "Dragon City AirHub", "FANTASIA", "Activo"));
        airports.add(new Airport(9, "Aeropuerto Internacional Arturo Merino Benítez", "CHILE", "Activo"));
        airports.add(new Airport(10, "Beijing Capital International Airport", "CHINA", "Activo"));
        airports.add(new Airport(11, "Aeropuerto de Atlantis", "MITICA", "Inactivo"));
        airports.add(new Airport(12, "Aeropuerto Internacional de Monterrey", "MEXICO", "Activo"));
        airports.add(new Airport(13, "Cape Town International Airport", "SOUTH AFRICA", "Activo"));
        airports.add(new Airport(14, "Aeropuerto de Nimbus Prime", "AETHER", "Activo"));
        airports.add(new Airport(15, "Aeropuerto de Lima", "PERU", "Inactivo"));
        airports.add(new Airport(16, "Istanbul Airport", "TURKEY", "Activo"));
        airports.add(new Airport(17, "Barcelona–El Prat Airport", "SPAIN", "Activo"));
        airports.add(new Airport(18, "Aeropuerto Internacional Simón Bolívar", "VENEZUELA", "Inactivo"));
        airports.add(new Airport(19, "Zürich Airport", "SWITZERLAND", "Activo"));
        airports.add(new Airport(20, "Cielo Alto Air Station", "ESTELIA", "Activo"));
        AirportManager.setAirports(airports);
        try {
            AirportManager.saveAirports();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addFlight() {
        try {
            AirportManager.loadAirports();
            DoublyLinkedList airports = AirportManager.getAirports();
            int size = airports.size();
            CircularDoublyLinkedList flights = new CircularDoublyLinkedList();
            for (int i = 1; i < 10; i++) {
                Airport aux = (Airport) airports.getNode(Util.random(size)).getData();
                int originCode = aux.getCode();
                aux = (Airport) airports.getNode(Util.random(size)).getData();
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
    void addRoutes() {
        try {
            AirportManager.loadAirports();
            List<Airport> airports = AirportManager.getAirports().toList();
            SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
            for (Airport airport : airports)
                graph.addVertex(airport.getCode());
            RoutesManager.setRoutesGraph(graph);
            RoutesManager.saveRoutes();
        } catch (IOException | GraphException | ListException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void loadRoutes() {
        System.out.println("Loading Routes");
        try {
            RoutesManager.loadRoutes();
        } catch (IOException | GraphException | ListException e) {
            throw new RuntimeException(e);
        }
        System.out.println(RoutesManager.getRoutesGraph());
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