package ucr.lab.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ucr.lab.domain.AirPort;

import java.io.*;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AirPortDatos {
    private File file = new File("src/main/resources/data/airports.json");
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return LocalDate.parse(json.getAsString());
                }
            })
            .setPrettyPrinting()
            .create();

    private final List<AirPort> aeropuertos;

    public AirPortDatos(File file) throws IOException {
        this.file = file;
        if (file.exists()) {
            this.aeropuertos = loadFromFile();
        } else {
            this.aeropuertos = new ArrayList<>();
            saveToFile();
        }
    }

    public void insert(AirPort airport) throws IOException {
        aeropuertos.add(airport);
        saveToFile();
    }

    public boolean buscar(int id) {
        return aeropuertos.stream().anyMatch(a -> a.getCode()==id);
    }


    public boolean actualizar(AirPort original, AirPort nuevo) throws IOException {
        for (int i = 0; i < aeropuertos.size(); i++) {
            if (aeropuertos.get(i).getCode() == original.getCode()){
                aeropuertos.set(i, nuevo);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean borrar(int id) throws IOException {
        boolean removed = aeropuertos.removeIf(a -> a.getCode()==id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    private List<AirPort> loadFromFile() {
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo no existe o está vacío, creando lista vacía.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<AirPort>>() {}.getType();
            List<AirPort> loaded = gson.fromJson(reader, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error cargando datos de aeropuertos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(aeropuertos, writer);
        }
    }

    public List<AirPort> getAllAirPorts(String filtro) throws IOException {
        List<AirPort> result = new ArrayList<>();
        AirPortDatos data = new AirPortDatos(file); // tu archivo binario de hoteles
        List<AirPort> listaDesdeArchivo = data.findAll();
        for (AirPort airport : listaDesdeArchivo) {
            if (filtro.equalsIgnoreCase("activos") && airport.getStatus().equalsIgnoreCase("Activo")) {
                result.add(airport);
            } else if (filtro.equalsIgnoreCase("inactivos") && airport.getStatus().equalsIgnoreCase("Inactivo")) {
                result.add(airport);
            } else if (filtro.equalsIgnoreCase("todos")) {
                result.add(airport);
            }
        }
        return result;
    }



    public AirPort buscarAirPort(int id) {
        return aeropuertos.stream()
                .filter(a -> a.getCode()==id)
                .findFirst()
                .orElse(null);
    }
    public void close() {
        // No resources to close when using Gson + FileWriter
    }



    public List<AirPort> findAll() throws IOException {
        ObjectMapper mapper = JacksonProvider.get();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<AirPort>>() {});
    }
}
