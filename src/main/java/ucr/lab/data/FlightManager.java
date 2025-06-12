package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.Flight;

import java.io.IOException;
import java.util.List;

public class FlightManager {
    private static CircularDoublyLinkedList flights = new CircularDoublyLinkedList();

    private static final String filePath = "src/main/resources/data/flights.json";

    public static void loadFlights() throws IOException {
        List<Flight> list = JsonManager.load(filePath, new TypeReference<>() {});
        flights.clear();
        for (Flight flight : list)
            flights.add(flight);
    }

    public static void saveFlights() throws IOException {
        List<Flight> list = flights.toList();
        JsonManager.save(filePath, list);
    }

    public static void add(Flight f) throws IOException {
        flights.add(f);
        saveFlights();
    }

    public static void remove(Flight f) throws IOException, ListException {
        flights.remove(f);
        saveFlights();
    }

    public static CircularDoublyLinkedList getFlights() {
        return flights;
    }

    public static void setFlights(CircularDoublyLinkedList flights) {
        FlightManager.flights = flights;
    }
}
