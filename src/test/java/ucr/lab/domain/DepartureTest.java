package ucr.lab.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.utility.DeparturesDatos;
import ucr.lab.utility.FileReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

class DepartureTest {
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
        List<Departure> departureList = FileReader.loadDepartures();

        if (departureList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (Departure departure : departureList) {
                System.out.println(departure);
            }
        }
    }

    @Test
    public void addDepartures() {
        // Crear Gson con el adaptador


        List<Departure> departureList = FileReader.loadDepartures();
     /*   Departure departure1 = new Departure(LocalDate.now(), "London", "A01", "ON TIME");
      //  Departure departure2 = new Departure(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED");
      //  Departure departure3 = new Departure(LocalDate.now().plusDays(4), "Tokyo", "C05", "CANCELLED");
      //  Departure departure4 = new Departure(LocalDate.now().plusDays(1), "Paris", "D07", "BOARDING");
      //  Departure departure5 = new Departure(LocalDate.now().plusDays(3), "Berlin", "E03", "FINAL CALL");

        departureList.add(departure1);
        departureList.add(departure2);
        departureList.add(departure3);
        departureList.add(departure4);
        departureList.add(departure5);*/

        if (departureList.isEmpty()) {
            System.out.println("No se han agregado usuarios al documento");

        } else {
            System.out.println("Usuarios agregados:");
            for (Departure departure : departureList) {
                System.out.println(departure);
            }
        }
    }

    @Test
    public void setDatos() throws IOException {
        //con la clase DeparturesDatos
        /*Departure departure1 = new Departure(LocalDate.now(), "London", "A01", "ON TIME");
        Departure departure2 = new Departure(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED");
        Departure departure3 = new Departure(LocalDate.now().plusDays(4), "Tokyo", "C05", "CANCELLED");
        Departure departure4 = new Departure(LocalDate.now().plusDays(1), "Paris", "D07", "BOARDING");
        Departure departure5 = new Departure(LocalDate.now().plusDays(3), "Berlin", "E03", "FINAL CALL");
        DeparturesDatos data = new DeparturesDatos(testFile);
        data.insert(departure1);
        data.insert(departure2);
        data.insert(departure3);
        data.insert(departure4);
        data.insert(departure5);*/
    }
}