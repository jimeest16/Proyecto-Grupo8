package ucr.lab.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.utility.DeparturesDatos;
import ucr.lab.utility.FileReader;

import java.io.File;

import java.io.IOException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;



class DeparturesTest {
    private File testFile = new File("src/main/resources/data/departures.json");
    private DeparturesDatos datos;

    @BeforeEach
    public void setup() throws IOException {
        datos = new DeparturesDatos(testFile);
    }

    @Test
    public void TESTDepartures() {
        try {

            SinglyLinkedList departuresList = FileReader.loadDepartures();

            if (departuresList.isEmpty()) {
                System.out.println("No se han agregado vuelos/salidas al documento");
            } else {
                System.out.println("Vuelos/Salidas agregados:");

                for (int i = 1; i <= departuresList.size(); i++) {
                    Departures departure = (Departures) departuresList.getNode(i).data;
                    System.out.println(departure);
                }
            }
        } catch (ListException e) {
            System.err.println("Error al cargar salidas desde SinglyLinkedList: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado en TESTDepartures: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void addDepartures() {
        try {

            SinglyLinkedList departuresList = FileReader.loadDepartures();

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


            FileReader.saveDepartures(convertSinglyLinkedListToList(departuresList));

            if (departuresList.isEmpty()) {
                System.out.println("No se han agregado vuelos/salidas al documento");
            } else {
                System.out.println("Vuelos/Salidas agregados:");
                // Iterate over SinglyLinkedList to print departures
                for (int i = 1; i <= departuresList.size(); i++) {
                    Departures departure = (Departures) departuresList.getNode(i).data;
                    System.out.println(departure);
                }
            }
        } catch (ListException e) {
            System.err.println("Error al añadir salidas a SinglyLinkedList: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado en addDepartures: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void setDatos() throws IOException {

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

    private List<Departures> convertSinglyLinkedListToList(SinglyLinkedList singlyLinkedList) throws ListException {
        List<Departures> list = new ArrayList<>();
        if (singlyLinkedList != null && !singlyLinkedList.isEmpty()) {
            for (int i = 1; i <= singlyLinkedList.size(); i++) {
                list.add((Departures) singlyLinkedList.getNode(i).data);
            }
        }
        return list;
    }
}