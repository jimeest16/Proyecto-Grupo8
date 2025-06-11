package ucr.lab.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.domain.Departure;

import java.io.*;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

public class DeparturesDatos {
    private final File file;
    private final List<Departure> departureList;
    ObjectMapper mapper = JacksonProvider.get();
    public DeparturesDatos(File file) throws IOException {

        List<Departure> list = mapper.readValue(file, new TypeReference<List<Departure>>() {});
        this.file = file;
        if (file.exists()) {
            this.departureList = loadFromFile();
        } else {
            this.departureList = new ArrayList<>();
            saveToFile();
        }
    }

    public void insert(Departure departure) throws IOException {
        departureList.add(departure);
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
private List<Departure> loadFromFile() {
    if (!file.exists() || file.length() == 0) {
        System.out.println("Archivo no existe o está vacío.");
        return new ArrayList<>();
    }

    try (Reader reader = new FileReader(file)) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Departure.class);
        return mapper.readValue(reader, type);
    } catch (IOException e) {
        System.err.println("Error cargando datos: " + e.getMessage());
        return new ArrayList<>();
    }
}


    private void saveToFile() throws IOException {
    }

    public List<Departure> getAllDepartures() {
        return new ArrayList<>(departureList);
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

    public List<Departure> findAll() {
        return new ArrayList<>(departureList);
    }
}
