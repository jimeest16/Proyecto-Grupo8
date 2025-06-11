package ucr.lab.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.Airport;
import ucr.lab.domain.Departure;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;
import ucr.lab.domain.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final String FILE_USER = "src/main/resources/data/user.json";
    private static final String FILE_AIRPORT = "src/main/resources/data/airports.json";
    private static final String FILE_PASSENGER = "src/main/resources/data/passengers.json";
    private static final String FILE_DEPARTURES = "src/main/resources/data/departures.json";

    private static final String FILE_FLIGHTS = "src/main/resources/data/flights.json";
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Cargar lista de usuarios
    public static List<User> loadUsers() {
        try {
            File file = new File(FILE_USER);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Guardar lista de usuarios
    public static void saveUsers(List<User> users) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_USER), users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveUsersRegister(CircularLinkedList users) throws ListException {
        // 1. Cargar usuarios actuales desde el JSON
        List<User> existingUsers = loadUsers();

        // 2. Recorrer los usuarios nuevos de la lista circular
        if (!users.isEmpty()) {
            User current = (User) users.getFirst();
            User inicio = current;

            do {
                // Verifica si el usuario ya existe
                boolean exists = false;
                for (User u : existingUsers) {
                    if (u.getId() == current.getId()) {
                        exists = true;
                        break;
                    }
                }

                // 3. Si no existe, lo agrega a la lista
                if (!exists) {
                    existingUsers.add(current);
                }

                current = (User) users.getNext();
            } while (current != inicio);
        }

        // 4. Guardar la lista completa actualizada en el JSON
        saveUsers(existingUsers);
    }


    // Añadir un nuevo usuario
    public static void addUser(User newUser) {
        List<User> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }

    // Cargar lista de aeropuertos
    public static List<Airport> loadAirports() {
        try {
            File file = new File(FILE_AIRPORT);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Airport>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Guardar lista de aeropuertos
    public static void saveAirports(List<Airport> airports) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_AIRPORT), airports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Añadir un nuevo aeropuerto
    public static void addAirport(Airport newAirport) {
        List<Airport> airports = loadAirports();
        airports.add(newAirport);
        saveAirports(airports);

        System.out.println("Total aeropuertos guardados: " + airports.size());
        airports.forEach(a -> System.out.println(a.getName()));
    }

    // Cargar lista de usuarios
    public static List<Passenger> loadPassengers() {
        try {
            File file = new File(FILE_PASSENGER);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Passenger>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void savePassengers(List<Passenger> passengers) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PASSENGER), passengers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPassenger(Passenger newPassenger) {
        List<Passenger> passengers = loadPassengers();
        passengers.add(newPassenger);
        savePassengers(passengers);
    }

    // Cargar lista de salidas
    public static List<Departure> loadDepartures() {
        try {
            File file = new File(FILE_DEPARTURES);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Departure>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveDepartures(List<Departure> departures) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_DEPARTURES), departures);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDepartures(Departure newDeparture) {
        List<Departure> departures = loadDepartures();
        departures.add(newDeparture);
        saveDepartures(departures);
    }
    public static CircularDoublyLinkedList loadFlights() {
        CircularDoublyLinkedList flightList = new CircularDoublyLinkedList();
        try {
            File file = new File(FILE_FLIGHTS);
            if (!file.exists()) return flightList;

            List<Flight> flights = mapper.readValue(file, new TypeReference<List<Flight>>() {});
            for (Flight f : flights) {
                flightList.add(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flightList;
    }
    public static void saveFlights(List<Flight> flights) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_FLIGHTS), flights);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addFlight(Flight newFlight) {
        List<Flight> flights = loadFlightsAsList(); // Usa lista para simplificar
        flights.add(newFlight);
        saveFlights(flights);
    }
    public static List<Flight> loadFlightsAsList() {
        try {
            File file = new File(FILE_FLIGHTS);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Flight>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static CircularDoublyLinkedList loadFlightsCircularList() {
        CircularDoublyLinkedList flightList = new CircularDoublyLinkedList();
        List<Flight> flights = loadFlightsAsList();

        for (Flight f : flights) {
            flightList.add(f);
        }
        return flightList;
    }

}