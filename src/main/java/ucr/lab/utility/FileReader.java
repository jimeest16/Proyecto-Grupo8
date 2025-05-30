package ucr.lab.utility;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.domain.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final String File_READER="src/main/resources/data/user.json";

    private static final ObjectMapper mapper= new ObjectMapper();

    // lista de user
    public static List<User> loadUsers(){
        try{
            File file= new File(File_READER);
            if(!file.exists()) return new ArrayList<>();
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

}
