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
        
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static CircularLinkedList loadUsers() throws ListException {
        CircularLinkedList userList = new CircularLinkedList();
        File file = new File(FILE_USER);
        System.out.println("[PRUEBAS] Cargando usuarios desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de usuarios no encontrado o vacío. Retornando lista vacía.");
                return userList;
            }
            List<User> tempUsers = mapper.readValue(file, new TypeReference<List<User>>() {});
            for (User u : tempUsers) {
                userList.add(u);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + userList.size() + " usuarios cargados.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al cargar usuarios: " + e.getMessage());
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
        File file = new File(FILE_USER);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + tempUsers.size() + " usuarios en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tempUsers);
            System.out.println("[PRUEBAS X CONSOLA:] Usuarios guardados correctamente.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addUser(User newUser) throws ListException {
        CircularLinkedList users = loadUsers();
        users.add(newUser);
        saveUsers(users);
        System.out.println("[PRUEBAS X CONSOLA:] Usuario '" + newUser.getName() + "' agregado y guardado.");
    }


    public static SinglyLinkedList loadAirports() {
        SinglyLinkedList airportsSinglyList = new SinglyLinkedList();
        File file = new File(FILE_AIRPORT);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando aeropuertos desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de aeropuertos no encontrado o vacío. Retornando lista vacía.");
                return airportsSinglyList;
            }
            List<AirPort> tempAirports = mapper.readValue(file, new TypeReference<List<AirPort>>() {});
            for (AirPort airport : tempAirports) {
                airportsSinglyList.add(airport);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + airportsSinglyList.size() + " aeropuertos cargados.");
        } catch (IOException | ListException e) {
            System.err.println("[Errores:] Error de I/O o JSON al cargar aeropuertos: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return airportsSinglyList;
    }

    public static void saveAirports(List<AirPort> airports) {
        File file = new File(FILE_AIRPORT);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + airports.size() + " aeropuertos en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, airports);
            System.out.println("[PRUEBAS X CONSOLA:] Aeropuertos guardados correctamente.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al guardar aeropuertos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addAirport(AirPort newAirport) {
        List<AirPort> airports = loadAirportsAsList();
        airports.add(newAirport);
        saveAirports(airports);
        System.out.println("[PRUEBAS X CONSOLA:] Aeropuerto '" + newAirport.getName() + "' agregado y guardado.");
    }


    public static SinglyLinkedList loadPassengers() {
        SinglyLinkedList passengersSinglyList = new SinglyLinkedList();
        File file = new File(FILE_PASSENGER);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando pasajeros desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) { // Verifica si el archivo existe y no está vacío
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de pasajeros no encontrado o vacío. Retornando lista vacía.");
                return passengersSinglyList;
            }
            List<Passenger> tempPassengers = mapper.readValue(file, new TypeReference<List<Passenger>>() {});
            for (Passenger passenger : tempPassengers) {
                passengersSinglyList.add(passenger);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + passengersSinglyList.size() + " pasajeros cargados.");
        } catch (IOException e) {
            System.err.println("[Errores:] Error de I/O o JSON al cargar pasajeros: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        } catch (Exception e) {
            System.err.println("[Errores:] Error inesperado al cargar pasajeros: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return passengersSinglyList;
    }

    public static void savePassengers(List<Passenger> passengers) {
        File file = new File(FILE_PASSENGER);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + passengers.size() + " pasajeros en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, passengers);
            System.out.println("[PRUEBAS X CONSOLA:] Pasajeros guardados correctamente.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al guardar pasajeros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addPassenger(Passenger newPassenger) {
        List<Passenger> passengers = loadPassengersAsList();
        passengers.add(newPassenger);
        savePassengers(passengers);
        System.out.println("[PRUEBAS X CONSOLA:] Pasajero '" + newPassenger.getName() + "' agregado y guardado.");
    }


    public static SinglyLinkedList loadDepartures() {
        SinglyLinkedList departuresSinglyList = new SinglyLinkedList();
        File file = new File(FILE_DEPARTURES);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando salidas desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de salidas no encontrado o vacío. Retornando lista vacía.");
                return departuresSinglyList;
            }
            List<Departures> tempDepartures = mapper.readValue(file, new TypeReference<List<Departures>>() {});
            for (Departures departure : tempDepartures) {
                departuresSinglyList.add(departure);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + departuresSinglyList.size() + " salidas cargadas.");
        } catch (IOException | ListException e) {
            System.err.println("[Errores:] Error de I/O o JSON al cargar salidas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return departuresSinglyList;
    }

    public static void saveDepartures(List<Departures> departures) {
        File file = new File(FILE_DEPARTURES);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + departures.size() + " salidas en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, departures);
            System.out.println("[PRUEBAS X CONSOLA:] Salidas guardadas correctamente.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al guardar salidas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addDepartures(Departures newDeparture) {
        List<Departures> departures = loadDeparturesAsList();
        departures.add(newDeparture);
        saveDepartures(departures);
        System.out.println("[PRUEBAS X CONSOLA:] Salida para aeropuerto " + newDeparture.getGateId() + " agregada y guardada.");
    }


    public static CircularDoublyLinkedList loadFlights() {
        CircularDoublyLinkedList flightList = new CircularDoublyLinkedList();
        File file = new File(FILE_FLIGHTS);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando vuelos desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de vuelos no encontrado o vacío. Retornando lista vacía.");
                return flightList;
            }
            List<Flight> flights = mapper.readValue(file, new TypeReference<List<Flight>>() {});
            for (Flight f : flights) {
                flightList.add(f);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + flightList.size() + " vuelos cargados.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al cargar vuelos: " + e.getMessage());
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
        File file = new File(FILE_FLIGHTS);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + tempFlights.size() + " vuelos en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tempFlights);
            System.out.println("[PRUEBAS X CONSOLA:] Vuelos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("[Errores:] Error al guardar vuelos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addFlight(Flight newFlight) throws ListException {
        CircularDoublyLinkedList flights = loadFlights();
        flights.add(newFlight);
        saveFlights(flights);
        System.out.println("[PRUEBAS X CONSOLA:] Vuelo " + newFlight.getNumber() + " agregado y guardado.");
    }

    public static List<Flight> loadFlightsAsListForInternalUse() {
        File file = new File(FILE_FLIGHTS);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando vuelos (como lista) desde: " + file.getAbsolutePath());
        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de vuelos (lista) no encontrado o vacío. Retornando lista vacía.");
                return new ArrayList<>();
            }
            List<Flight> flights = mapper.readValue(file, new TypeReference<List<Flight>>() {});
            System.out.println("[PRUEBAS X CONSOLA:] " + flights.size() + " vuelos cargados (como lista).");
            return flights;
        } catch (IOException e) {
            System.err.println("[Errores:] Error de I/O o JSON al cargar vuelos (como lista): " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static SinglyLinkedList loadRoutes() {
        SinglyLinkedList routesSinglyList = new SinglyLinkedList();
        File file = new File(FILE_ROUTES);
        System.out.println("[PRUEBAS X CONSOLA:] Cargando rutas desde: " + file.getAbsolutePath());
        try {
            if (!file.exists()) {
                System.err.println("Advertencia: Archivo de rutas NO ENCONTRADO en: " + file.getAbsolutePath());
                return routesSinglyList;
            } else if (file.length() == 0) {
                System.out.println("[PRUEBAS X CONSOLA:] Archivo de rutas encontrado pero vacío.");
                return routesSinglyList;
            }
            System.out.println("[PRUEBAS X CONSOLA:] INFO: Archivo de rutas encontrado en: " + file.getAbsolutePath());

            List<Route> tempRoutes = mapper.readValue(file, new TypeReference<List<Route>>() {});
            for (Route route : tempRoutes) {
                routesSinglyList.add(route);
            }
            System.out.println("[PRUEBAS X CONSOLA:] " + routesSinglyList.size() + " rutas cargadas.");
        } catch (IOException e) {
            System.err.println("[Errores:] Error de I/O o JSON al cargar rutas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        } catch (Exception e) {
            System.err.println("[Errores:] Error inesperado al cargar rutas: " + e.getMessage());
            e.printStackTrace();
            return new SinglyLinkedList();
        }
        return routesSinglyList;
    }

    public static void saveRoutes(List<Route> routes) {
        File file = new File(FILE_ROUTES);
        System.out.println("[PRUEBAS X CONSOLA:] Guardando " + routes.size() + " rutas en: " + file.getAbsolutePath());
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, routes);
            System.out.println("[PRUEBAS X CONSOLA:] Rutas guardadas correctamente.");
        } catch (Exception e) {
            System.err.println("[Errores:] Error al guardar rutas: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static List<AirPort> loadAirportsAsList() {
        File file = new File(FILE_AIRPORT);
        try {
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<AirPort>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<Passenger> loadPassengersAsList() {
        File file = new File(FILE_PASSENGER);
        try {
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Passenger>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<Departures> loadDeparturesAsList() {
        File file = new File(FILE_DEPARTURES);
        try {
            if (!file.exists() || file.length() == 0) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Departures>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}