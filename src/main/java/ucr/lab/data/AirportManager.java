package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.AirPort;

import java.io.IOException;
import java.util.List;

public class AirportManager {
    private static DoublyLinkedList airports = new DoublyLinkedList();

    private static final String filePath = "src/main/resources/data/airports.json";

    public static void loadAirports() throws IOException {
        List<AirPort> list = JsonManager.load(filePath, new TypeReference<>() {});
        airports.clear();
        for (AirPort airport : list)
            airports.add(airport);
    }

    public static void saveAirports() throws IOException {
        List<AirPort> list = airports.toList();
        JsonManager.save(filePath, list);
    }

    public static void add(AirPort a) throws IOException {
        airports.add(a);
        saveAirports();
    }

    public static void remove(AirPort a) throws IOException, ListException {
        airports.remove(a);
        saveAirports();
    }

    public static DoublyLinkedList getAirports() {
        return airports;
    }

    public static void setAirports(DoublyLinkedList airports) {
        AirportManager.airports = airports;
    }
}
