package ucr.lab.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;
import ucr.lab.domain.Flight;

import java.io.*;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DeparturesDatos {
    //ESTA CLASE MANEJA LOS DATOS DE LA LISTA DE SALIDAS DE CADA AEROPUERTOS QUE CONTIENE VUELOS
    private final File file;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    private final List<Flight> departuresList;
    ObjectMapper mapper = JacksonProvider.get();
    public DeparturesDatos(File file) throws IOException {
        this.file = file;

        if (file.exists() && file.length() > 0) {
            this.departuresList = mapper.readValue(file, new TypeReference<List<Flight>>() {});
            System.out.println("⚙️ Departures cargados: " + this.departuresList.size());
        } else {
            this.departuresList = new ArrayList<>();
            saveToFile();
            System.out.println("📄 Archivo creado: lista vacía de departures");
        }
        System.out.println("🔍 Departures cargados correctamente: " + departuresList.size());

    }

    public void insert(Flight departure) throws IOException {
        departuresList.add(departure);
        saveToFile();
    }
/*
    public boolean buscar(int id) {
        return departuresList.stream().anyMatch(d -> d.getCode() == id);
    }

    public boolean actualizar(Departures original, Departures nuevo) throws IOException {
        for (int i = 0; i < departuresList.size(); i++) {
            if (departuresList.get(i).getCode() == original.getCode()) {
                departuresList.set(i, nuevo);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    public boolean borrar(int id) throws IOException {
        boolean removed = departuresList.removeIf(d -> d.getCode() == id);
        if (removed) {
            saveToFile();
        }
        return removed;
    }
*/
private List<Flight> loadFromFile() {
    if (!file.exists() || file.length() == 0) {
        System.out.println("Archivo no existe o está vacío.");
        return new ArrayList<>();
    }

    try (Reader reader = new FileReader(file)) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, SinglyLinkedList.class);
        return mapper.readValue(reader, type);
    } catch (IOException e) {
        System.err.println("Error cargando datos: " + e.getMessage());
        return new ArrayList<>();
    }
}


    private void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(departuresList, writer);
        }
    }

    public List<Flight> getAllDepartures() {
        return new ArrayList<>(departuresList);
    }



    public void close() {
        // No resources to close when using Gson + FileWriter
    }
/*
    public void activeDeparture(int id) {
        if (buscar(id)) {
            buscarDeparture(id).setActive(true);
        }
    }

    public void deactiveDeparture(int id) {
        if (buscar(id)) {
            buscarDeparture(id).setActive(false);
        }
    }*/

    public List<Flight> findAll() {
        return new ArrayList<>(departuresList);
    }
}
