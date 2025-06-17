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
       // datos = new DeparturesDatos(testFile);
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

            Flight flight1 = new Flight(101, 1, 2,
                    LocalDate.now().atTime(10, 30), 150, 0, "ON TIME");

            Flight flight2 = new Flight(102, 1, 3,
                    LocalDate.now().plusDays(2).atTime(14, 0), 200, 0, "DELAYED");

            Flight flight3 = new Flight(103, 1, 4,
                    LocalDate.now().plusDays(4).atTime(8, 45), 180, 0, "CANCELLED");

            Flight flight4 = new Flight(104, 1, 5,
                    LocalDate.now().plusDays(1).atTime(9, 15), 120, 0, "BOARDING");

            Flight flight5 = new Flight(105, 1, 6,
                    LocalDate.now().plusDays(3).atTime(16, 20), 130, 0, "FINAL CALL");

            departuresList.add(flight1);
            departuresList.add(flight2);
            departuresList.add(flight3);
            departuresList.add(flight4);
            departuresList.add(flight5);


            FileReader.saveDepartures(convertSinglyLinkedListToList(departuresList));

            if (departuresList.isEmpty()) {
                System.out.println("No se han agregado vuelos/salidas al documento");
            } else {
                System.out.println("Vuelos/Salidas agregados:");
                // Iterate over SinglyLinkedList to print departures
                for (int i = 1; i <= departuresList.size(); i++) {
                    Flight departure = (Flight) departuresList.getNode(i).data;
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
/*
        Departures departure1 = new Departures(LocalDate.now(), "London", "A01", "ON TIME");
        Departures departure2 = new Departures(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED");
        Departures departure3 = new Departures(LocalDate.now().plusDays(4), "Tokyo", "C05", "CANCELLED");
        Departures departure4 = new Departures(LocalDate.now().plusDays(1), "Paris", "D07", "BOARDING");
        Departures departure5 = new Departures(LocalDate.now().plusDays(3), "Berlin", "E03", "FINAL CALL");

 */
        Flight flight1 = new Flight(101, 1, 2,
                LocalDate.now().atTime(10, 30), 150, 0, "ON TIME");

        Flight flight2 = new Flight(102, 1, 3,
                LocalDate.now().plusDays(2).atTime(14, 0), 200, 0, "DELAYED");

        Flight flight3 = new Flight(103, 1, 4,
                LocalDate.now().plusDays(4).atTime(8, 45), 180, 0, "CANCELLED");

        Flight flight4 = new Flight(104, 1, 5,
                LocalDate.now().plusDays(1).atTime(9, 15), 120, 0, "BOARDING");

        Flight flight5 = new Flight(105, 1, 6,
                LocalDate.now().plusDays(3).atTime(16, 20), 130, 0, "FINAL CALL");
        DeparturesDatos data = new DeparturesDatos(testFile);
        data.insert(flight1);
        data.insert(flight2);
        data.insert(flight3);
        data.insert(flight4);
        data.insert(flight5);

    }

    private List<Flight> convertSinglyLinkedListToList(SinglyLinkedList singlyLinkedList) throws ListException {
        List<Flight> list = new ArrayList<>();
        if (singlyLinkedList != null && !singlyLinkedList.isEmpty()) {
            for (int i = 1; i <= singlyLinkedList.size(); i++) {
                list.add((Flight) singlyLinkedList.getNode(i).data);
            }
        }
        return list;
    }
}