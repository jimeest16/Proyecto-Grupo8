package ucr.lab.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ucr.lab.domain.Airport;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AirPortDatos {
    private File file = new File("src/main/resources/data/airports.json");

    private final List<Airport> aeropuertos;

    public AirPortDatos(File file) throws IOException {
        this.file = file;
        if (file.exists()) {
            this.aeropuertos = loadFromFile();
        } else {
            this.aeropuertos = new ArrayList<>();
            saveToFile();
        }
    }

    public void insert(Airport airport) throws IOException {
        aeropuertos.add(airport);
        saveToFile();
    }

    public boolean buscar(int id) {
        return aeropuertos.stream().anyMatch(a -> a.getCode()==id);
    }


    public boolean actualizar(Airport original, Airport nuevo) throws IOException {
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

    private List<Airport> loadFromFile() {
        if (!file.exists() || file.length() == 0) {
            System.out.println("Archivo no existe o está vacío, creando lista vacía.");
            return new ArrayList<>();
        }
        return null;
    }

    private void saveToFile() throws IOException {

    }

    public List<Airport> getAllAirPorts(String filtro) throws IOException {
        List<Airport> result = new ArrayList<>();
        AirPortDatos data = new AirPortDatos(file); // tu archivo binario de hoteles
        List<Airport> listaDesdeArchivo = data.findAll();
        for (Airport airport : listaDesdeArchivo) {
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



    public Airport buscarAirPort(int id) {
        return aeropuertos.stream()
                .filter(a -> a.getCode()==id)
                .findFirst()
                .orElse(null);
    }
    public void close() {
        // No resources to close when using Gson + FileWriter
    }



    public List<Airport> findAll() throws IOException {
        ObjectMapper mapper = JacksonProvider.get();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return mapper.readValue(file, new TypeReference<List<Airport>>() {});
    }
}
