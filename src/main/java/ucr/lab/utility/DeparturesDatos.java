package ucr.lab.utility;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;

import java.io.*;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class DeparturesDatos {
    private final File file;
    private Gson gson = new Gson();
    private final List<Departures> departuresList;

    public DeparturesDatos(File file) throws IOException {
        this.file = file;
        if (file.exists()) {
            this.departuresList = loadFromFile();
        } else {
            this.departuresList = new ArrayList<>();
            saveToFile();
        }
    }

    public void insert(Departures departure) throws IOException {
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
    private List<Departures> loadFromFile() {
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo no existe o está vacío, creando lista vacía.");
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<AirPort>>() {}.getType();
            List<Departures> loaded = gson.fromJson(reader, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Error cargando datos de aeropuertos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveToFile() throws IOException {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(departuresList, writer);
        }
    }

    public List<Departures> getAllDepartures() {
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

    public List<Departures> findAll() {
        return new ArrayList<>(departuresList);
    }
}
