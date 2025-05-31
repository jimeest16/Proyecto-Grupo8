package ucr.lab.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final String File_READER="src/main/resources/data/user.json";
    private static final String FILE_AIRPORT="src/main/resources/data/airports.json";

    private static ObjectMapper mapper = new ObjectMapper();

    public FileReader() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    // lista de user
    public static List<User> loadUsers(){
        try{
            File file= new File(File_READER);
            if(!file.exists()) return new ArrayList<>();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return  mapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();

        }
    }

    public static void saveUsers(List<User> users){
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(File_READER), users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User newUser){
        List<User> users = loadUsers();
        users.add(newUser);
        saveUsers(users);
    }

    // lista de airport
    public static List<AirPort> loadAirports(){
        try{
            File file= new File(FILE_AIRPORT);
            if(!file.exists()) return new ArrayList<>();
            return  mapper.readValue(file, new TypeReference<List<AirPort>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();

        }
    }

    public static void saveAirports(List<AirPort> airports){
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_AIRPORT), airports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAirport(AirPort newAirport){
        List<AirPort> airports = loadAirports();
        airports.add(newAirport);
        saveAirports(airports);

        // Verificación
        System.out.println("Total aeropuertos guardados: " + airports.size());
        airports.forEach(a -> System.out.println(a.getName()));
    }

}
