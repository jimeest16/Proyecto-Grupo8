package ucr.lab.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.CircularLinkedList;
import ucr.lab.TDA.ListException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Passenger;
import ucr.lab.domain.User;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final String FILE_USER = "src/main/resources/data/user.json";
    private static final String FILE_AIRPORT = "src/main/resources/data/airports.json";
    private static final String FILE_PASSENGER = "src/main/resources/data/passengers.json";

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
                // Verifica si el usuario ya existe (puedes definir esto por ID o correo, por ejemplo)
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
}