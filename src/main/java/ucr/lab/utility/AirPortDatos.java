package ucr.lab.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;

import java.io.*;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AirPortDatos {
    private File file = new File("src/main/resources/data/airports.json");
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
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

    public List<AirPort> loadFromFile() throws IOException {
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo no existe o está vacío, creando lista vacía.");
            return new ArrayList<>();
        }
            try (Reader reader = new FileReader(file)) {
                List<AirPort> lista = gson.fromJson(reader, new TypeToken<List<AirPort>>(){}.getType());
                for (AirPort ap : lista) {
                    try {
                        ap.afterDeserialization(gson);
                    } catch (QueueException e) {
                        e.printStackTrace();
                    }
                }
                return lista;
            }
    }

    private void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(aeropuertos, writer);
        }
    }

    //obtener todos los aeropuertos por activos, inactivos o todos
    public List<AirPort> getAllAirPorts(String filtro) throws IOException {
        List<AirPort> result = new ArrayList<>();
        AirPortDatos data = new AirPortDatos(file); // tu archivo
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

    //lista para el reporte
    public List<AirPort> getTop5AirportsWithMostFlights() throws IOException, ListException {
        AirPortDatos data = new AirPortDatos(file);
        List<AirPort> allAirports = data.findAll();

        // Mapa para contar vuelos BOARDING por aeropuerto
        Map<AirPort, Integer> boardingCountMap = new HashMap<>();

        for (AirPort airport : allAirports) {
            int count = 0;
            SinglyLinkedList departures = airport.getDeparturesBoard();
            for (Flight flight : departures.toFlightList()) {
                if ("BOARDING".equalsIgnoreCase(flight.getStatus())) {
                    count++;
                }
            }
            boardingCountMap.put(airport, count);
        }

        // Ordenar los aeropuertos por cantidad de BOARDING, descendente
        return boardingCountMap.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<AirPort> get() throws IOException {
        AirPortDatos datos = new AirPortDatos(new File("src/main/resources/data/airports.json"));
        return datos.loadFromFile();  // o usar datos.findAll() si prefieres usar Jackson
    }

    public AirPort buscarAirPort(int id) {
        return aeropuertos.stream()
                .filter(a -> a.getCode()==id)
                .findFirst()
                .orElse(null);
    }

    public List<AirPort> findAll() throws IOException {
        ObjectMapper mapper = JacksonProvider.get();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<AirPort>>() {});
    }
}
