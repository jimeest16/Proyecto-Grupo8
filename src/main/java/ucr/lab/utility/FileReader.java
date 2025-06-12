package ucr.lab.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.domain.*;

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
    private static final String FILE_ROUTES = "src/main/resources/data/rutas.json";
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
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
    public static List<AirPort> loadAirports() {
        try {
            File file = new File(FILE_AIRPORT);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<AirPort>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Guardar lista de aeropuertos
    public static void saveAirports(List<AirPort> airports) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_AIRPORT), airports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Añadir un nuevo aeropuerto
    public static void addAirport(AirPort newAirport) {
        List<AirPort> airports = loadAirports();
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
    public static List<Departures> loadDepartures() {
        try {
            File file = new File(FILE_DEPARTURES);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Departures>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void saveDepartures(List<Departures> departures) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_DEPARTURES), departures);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDepartures(Departures newDeparture) {
        List<Departures> departures = loadDepartures();
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

    // RUTAS


    public static List<Route> loadRoutes() {
        try {
            File file = new File(FILE_ROUTES);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Route>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Guardar lista de rutas
    public static void saveRoutes(List<Route> routes) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_ROUTES), routes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Añadir una nueva ruta
    public static void addRoute(Route newRoute) {
        List<Route> routes = loadRoutes();
        routes.add(newRoute);
        saveRoutes(routes);
    }



}