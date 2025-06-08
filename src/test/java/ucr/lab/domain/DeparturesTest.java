package ucr.lab.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.utility.DeparturesDatos;
import ucr.lab.utility.FileReader;
import ucr.lab.utility.JacksonProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeparturesTest {
    private File testFile= new File("src/main/resources/data/departures.json");
    private DeparturesDatos datos;

    @BeforeEach
    public void setup() throws IOException, IOException {
        // Archivo temporal para pruebas, se borra al finalizar
        //testFile = Files.createTempFile("departuresTest", ".json").toFile();
        datos = new DeparturesDatos(testFile);
    }

    @Test
    public void TESTDepartures() throws IOException {
        List<Departures> departuresList = FileReader.loadDepartures();

        if (departuresList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (Departures departure : departuresList) {
                System.out.println(departure);
            }
        }
    }

    @Test
    public void addDepartures() {
        // Crear Gson con el adaptador


        List<Departures> departuresList = FileReader.loadDepartures();
        Departures departure1 = new Departures(LocalDate.now(), "London", "A01", "ON TIME");
        Departures departure2 = new Departures(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED");
        Departures departure3 = new Departures(LocalDate.now().plusDays(4), "Tokyo", "C05", "CANCELLED");
        Departures departure4 = new Departures(LocalDate.now().plusDays(1), "Paris", "D07", "BOARDING");
        Departures departure5 = new Departures(LocalDate.now().plusDays(3), "Berlin", "E03", "FINAL CALL");

        departuresList.add(departure1);
        departuresList.add(departure2);
        departuresList.add(departure3);
        departuresList.add(departure4);
        departuresList.add(departure5);

        if (departuresList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (Departures departure : departuresList) {
                System.out.println(departure);
            }
        }
    }

    @Test
    public void setDatos() throws IOException {
        //con la clase DeparturesDatos
        Departures departure1 = new Departures(LocalDate.now(), "London", "A01", "ON TIME");
        Departures departure2 = new Departures(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED");
        Departures departure3 = new Departures(LocalDate.now().plusDays(4), "Tokyo", "C05", "CANCELLED");
        Departures departure4 = new Departures(LocalDate.now().plusDays(1), "Paris", "D07", "BOARDING");
        Departures departure5 = new Departures(LocalDate.now().plusDays(3), "Berlin", "E03", "FINAL CALL");
        DeparturesDatos data = new DeparturesDatos(testFile);
        data.insert(departure1);
        data.insert(departure2);
        data.insert(departure3);
        data.insert(departure4);
        data.insert(departure5);
    }
}