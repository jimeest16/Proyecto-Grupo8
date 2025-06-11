package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.tree.AVLTree;
import ucr.lab.TDA.tree.TreeException;
import ucr.lab.domain.Passenger;

import java.io.IOException;
import java.util.List;

public class PassengerManager {
    private static AVLTree passengers = new AVLTree();

    private static final String filePath = "src/main/resources/data/passengers.json";

    public static void loadPassengers() throws IOException, TreeException {
        List<Passenger> list = JsonManager.load(filePath, new TypeReference<>() {});
        passengers.clear();
        for (Passenger passenger : list)
            passengers.add(passenger);
    }

    public static void savePassengers() throws IOException {
        List<Passenger> list = passengers.toList();
        JsonManager.save(filePath, list);
    }

    public static void add(Passenger p) throws IOException, TreeException {
        passengers.add(p);
        savePassengers();
    }

    public static void remove(Passenger p) throws IOException, TreeException {
        passengers.remove(p);
        savePassengers();
    }

    public static AVLTree getPassengers() {
        return passengers;
    }

    public static void setPassengers(AVLTree passengers) {
        PassengerManager.passengers = passengers;
    }
}
