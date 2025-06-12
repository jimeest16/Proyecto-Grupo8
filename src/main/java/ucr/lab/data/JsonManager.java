package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> load(String filePath, TypeReference<List<T>> typeRef) throws IOException {
        return mapper.readValue(new File(filePath), typeRef);
    }

    public static <T> void save(String filePath, List<T> data) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }
}
