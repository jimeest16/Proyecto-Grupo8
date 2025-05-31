package ucr.lab.utility;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import ucr.lab.domain.AirPort;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AirPortDatos {
    private final File file;
    private final Gson gson = new Gson();
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
        try (Reader reader = new java.io.FileReader(file)) {
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
    public List<AirPort> getAllAirPorts(String status) {
        return aeropuertos.stream()
                .filter(a -> {
                    switch (status.toLowerCase()) {
                        case "activos":
                            return a.isActive(); // true = activo
                        case "inactivos":
                            return !a.isActive(); // false = inactivo
                        case "todos":
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());
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

    public void activeAirport(int id) {
        if (buscar(id)) {
            buscarAirPort(id).setActive(true);
        }
    }

    public void deactiveAirport(int id) {
        if (buscar(id)) {
            buscarAirPort(id).setActive(false);
        }
    }

    public List<AirPort> findAll() {
        return new ArrayList<>(aeropuertos);
    }
}
