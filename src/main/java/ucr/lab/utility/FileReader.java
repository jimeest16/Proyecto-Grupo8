package ucr.lab.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.CircularDoublyLinkedList;
import ucr.lab.TDA.list.CircularLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;

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


    public static CircularLinkedList loadUsers() throws ListException {
        CircularLinkedList userList = new CircularLinkedList();
        try {
            File file = new File(FILE_USER);
            if (!file.exists()) return userList;
            List<User> tempUsers = mapper.readValue(file, new TypeReference<List<User>>() {});
            for (User u : tempUsers) {
                userList.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static void saveUsers(CircularLinkedList users) throws ListException {
        List<User> tempUsers = new ArrayList<>();
        if (!users.isEmpty()) {
            Object currentObj = users.getFirst();
            Object startObj = currentObj;
            do {
                tempUsers.add((User) currentObj);
                currentObj = users.getNext();
            } while (currentObj != startObj);
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_USER), tempUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User newUser) throws ListException {
        CircularLinkedList users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }


    public static SinglyLinkedList loadAirports() {
        SinglyLinkedList airportsSinglyList = new SinglyLinkedList();
        try {
            File file = new File(FILE_AIRPORT);
            if (!file.exists()) {
                System.err.println("Advertencia: Archivo de aeropuertos no encontrado en: " + FILE_AIRPORT);
                return airportsSinglyList;
            }
            List<AirPort> tempAirports = mapper.readValue(file, new TypeReference<List<AirPort>>() {});
            for (AirPort airport : tempAirports) {
                // Añadir try-catch para ListException en el loop si add() lo lanza
                airportsSinglyList.add(airport);
            }
        } catch (IOException e) {
            System.err.println("Error de I/O o JSON al cargar aeropuertos: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return airportsSinglyList;
    }

    public static void saveAirports(List<AirPort> airports) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_AIRPORT), airports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAirport(AirPort newAirport) {

        List<AirPort> airports = loadAirportsAsList();
        airports.add(newAirport);
        saveAirports(airports);

        System.out.println("Total aeropuertos guardados: " + airports.size());
        airports.forEach(a -> System.out.println(a.getName()));
    }


    public static SinglyLinkedList loadPassengers() {
        SinglyLinkedList passengersSinglyList = new SinglyLinkedList();
        try {
            File file = new File(FILE_PASSENGER);
            if (!file.exists()) {
                System.err.println("Advertencia: Archivo de pasajeros no encontrado en: " + FILE_PASSENGER);
                return passengersSinglyList;
            }
            List<Passenger> tempPassengers = mapper.readValue(file, new TypeReference<List<Passenger>>() {});
            for (Passenger passenger : tempPassengers) {

                passengersSinglyList.add(passenger);
            }
        } catch (IOException e) {
            System.err.println("Error de I/O o JSON al cargar pasajeros: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return passengersSinglyList;
    }

    public static void savePassengers(List<Passenger> passengers) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PASSENGER), passengers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPassenger(Passenger newPassenger) {

        List<Passenger> passengers = loadPassengersAsList();
        passengers.add(newPassenger);
        savePassengers(passengers);
    }


    public static SinglyLinkedList loadDepartures() {
        SinglyLinkedList departuresSinglyList = new SinglyLinkedList();
        try {
            File file = new File(FILE_DEPARTURES);
            if (!file.exists()) {
                System.err.println("Advertencia: Archivo de salidas no encontrado en: " + FILE_DEPARTURES);
                return departuresSinglyList;
            }
            List<Departures> tempDepartures = mapper.readValue(file, new TypeReference<List<Departures>>() {});
            for (Departures departure : tempDepartures) {
                // Añadir try-catch para ListException en el loop si add() lo lanza
                departuresSinglyList.add(departure);
            }
        } catch (IOException e) {
            System.err.println("Error de I/O o JSON al cargar salidas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return departuresSinglyList;
    }

    public static void saveDepartures(List<Departures> departures) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_DEPARTURES), departures);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDepartures(Departures newDeparture) {

        List<Departures> departures = loadDeparturesAsList();
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

    public static void saveFlights(CircularDoublyLinkedList flights) throws ListException {
        List<Flight> tempFlights = new ArrayList<>();
        if (!flights.isEmpty()) {
            Object currentObj = flights.getFirst();
            Object startObj = currentObj;
            do {
                tempFlights.add((Flight) currentObj);
                currentObj = flights.getNext();
            } while (currentObj != startObj);
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_FLIGHTS), tempFlights);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFlight(Flight newFlight) throws ListException {
        CircularDoublyLinkedList flights = loadFlights();
        flights.add(newFlight);
        saveFlights(flights);
    }

    public static List<Flight> loadFlightsAsListForInternalUse() {
        try {
            File file = new File(FILE_FLIGHTS);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Flight>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static SinglyLinkedList loadRoutes() {
        SinglyLinkedList routesSinglyList = new SinglyLinkedList();
        try {
            File file = new File(FILE_ROUTES);

            if (!file.exists()) {
                System.err.println("Advertencia: Archivo de rutas NO ENCONTRADO en: " + file.getAbsolutePath());
                return routesSinglyList;
            } else {
                System.out.println("INFO: Archivo de rutas encontrado en: " + file.getAbsolutePath());
            }


            List<Route> tempRoutes = mapper.readValue(file, new TypeReference<List<Route>>() {});
            for (Route route : tempRoutes) {


                routesSinglyList.add(route);
            }
        } catch (IOException e) {
            System.err.println("Error de I/O o JSON al cargar rutas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList(); // Retorna una lista vacía en caso de error
        } catch (Exception e) { // Captura cualquier otra excepción que pueda ocurrir durante la deserialización que se hizo en la clase Singlyreader o el add()
            System.err.println("Error inesperado al cargar rutas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return routesSinglyList;
    }

    public static void saveRoutes(List<Route> routes) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_ROUTES), routes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static List<AirPort> loadAirportsAsList() {
        try {
            File file = new File(FILE_AIRPORT);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<AirPort>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<Passenger> loadPassengersAsList() {
        try {
            File file = new File(FILE_PASSENGER);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Passenger>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<Departures> loadDeparturesAsList() {
        try {
            File file = new File(FILE_DEPARTURES);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Departures>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}