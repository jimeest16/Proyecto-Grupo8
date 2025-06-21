package ucr.lab.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.TDA.Node;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;
import ucr.lab.domain.Flight;
import ucr.lab.domain.Passenger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirPortDatosTest {

    String rutaArchivo = "src/main/resources/data/airports.json";
    private File tempFile = new File(rutaArchivo);
    private AirPortDatos airportDatos;


    @BeforeEach
    void setUp() throws IOException {
        File tempFile = new File(rutaArchivo);
        airportDatos = new AirPortDatos(tempFile);
    }

    //@AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void cargarAirports() throws IOException, QueueException {
        File tempFile = new File(rutaArchivo);
        airportDatos = new AirPortDatos(tempFile);

        Flight flight1 = new Flight(101, 1, 2, LocalDate.now().atTime(10, 30), 150, 0, "ON TIME");

        Flight flight2 = new Flight(102, 1, 3, LocalDate.now().plusDays(2).atTime(14, 0), 200, 0, "DELAYED");

        Flight flight3 = new Flight(103, 1, 4, LocalDate.now().plusDays(4).atTime(8, 45), 180, 0, "CANCELLED");

        Flight flight4 = new Flight(104, 1, 5, LocalDate.now().plusDays(1).atTime(9, 15), 120, 0, "BOARDING");

        Flight flight5 = new Flight(105, 1, 6, LocalDate.now().plusDays(3).atTime(16, 20), 130, 0, "FINAL CALL");

        SinglyLinkedList lista = new SinglyLinkedList();
        lista.add(flight1);
        lista.add(flight2);
        lista.add(flight3);
        lista.add(flight4);
        lista.add(flight5);

        LinkedQueue colaPasajeros1 = new LinkedQueue();
        LinkedQueue colaPasajeros2 = new LinkedQueue();
        LinkedQueue colaPasajeros3 = new LinkedQueue();
        Passenger p1 = new Passenger(1,"Camila","Costarricense");
        Passenger p2 = new Passenger(2, "Mateo", "Mexicano");
        Passenger p3 = new Passenger(3, "Sara", "Colombiana");
        Passenger p4 = new Passenger(4, "John", "Estadounidense");
        Passenger p5 = new Passenger(5, "Ana", "Argentina");
        Passenger p6 = new Passenger(6, "Hiroshi", "Japonés");
        Passenger p7 = new Passenger(7, "Marie", "Francesa");
        Passenger p8 = new Passenger(8, "Mohamed", "Egipcio");
        Passenger p9 = new Passenger(9, "Julia", "Italiana");
        Passenger p10 = new Passenger(10, "David", "Alemán");
        colaPasajeros1.enQueue(p1);
        colaPasajeros1.enQueue(p2);
        colaPasajeros1.enQueue(p3);
        colaPasajeros1.enQueue(p4);
        colaPasajeros2.enQueue(p5);
        colaPasajeros2.enQueue(p6);
        colaPasajeros2.enQueue(p7);
        colaPasajeros3.enQueue(p8);
        colaPasajeros3.enQueue(p9);
        colaPasajeros3.enQueue(p10);
        airportDatos.insert(new AirPort(1, "Los Angeles International Airport", "USA", "Activo", lista, colaPasajeros1));

        airportDatos.insert(new AirPort(2, "John F. Kennedy International Airport", "USA", "Inactivo", lista, colaPasajeros1));

        airportDatos.insert(new AirPort(3, "Aeropuerto Internacional Juan Santamaría", "COSTA RICA", "Activo", lista, colaPasajeros1));

        airportDatos.insert(new AirPort(4, "Ciudad Celeste International Airport", "TERRANOVA", "Inactivo", lista, colaPasajeros2));

        airportDatos.insert(new AirPort(5, "Narita International Airport", "JAPAN", "Activo", lista, colaPasajeros2));

        airportDatos.insert(new AirPort(6, "Frankfurt Airport", "GERMANY", "Activo", lista, colaPasajeros2));

        airportDatos.insert(new AirPort(7, "Aeropuerto de Buenos Aires", "ARGENTINA", "Inactivo", lista, colaPasajeros2));

        airportDatos.insert(new AirPort(8, "Dragon City AirHub", "JAPAN", "Activo", lista, colaPasajeros2));

        airportDatos.insert(new AirPort(9, "Aeropuerto Internacional Arturo Merino Benítez", "CHILE", "Activo",
               lista, colaPasajeros1));

        airportDatos.insert(new AirPort(10, "Beijing Capital International Airport", "CHINA", "Activo",
                lista, colaPasajeros1));

        airportDatos.insert(new AirPort(11, "Argelia Airport", "ARGELIA", "Inactivo",
                lista, colaPasajeros1));

        airportDatos.insert(new AirPort(12, "Aeropuerto Internacional de Monterrey", "MEXICO", "Activo",
                lista, colaPasajeros1));

        airportDatos.insert(new AirPort(13, "Cape Town International Airport", "SOUTH AFRICA", "Activo",
                lista, colaPasajeros1));

        airportDatos.insert(new AirPort(14, "Aeropuerto de Nimbus Prime", "FRANCE", "Activo",
                lista, colaPasajeros1));

        airportDatos.insert(new AirPort(15, "Aeropuerto de Lima", "PERU", "Inactivo",
                lista, colaPasajeros3));

        airportDatos.insert(new AirPort(16, "Istanbul Airport", "TURKEY", "Activo",
                lista, colaPasajeros3));

        airportDatos.insert(new AirPort(17, "Barcelona–El Prat Airport", "SPAIN", "Activo",
                lista, colaPasajeros3));

        airportDatos.insert(new AirPort(18, "Aeropuerto Internacional Simón Bolívar", "VENEZUELA", "Inactivo",
                lista, colaPasajeros3));

        airportDatos.insert(new AirPort(19, "Zürich Airport", "SWITZERLAND", "Activo",
                lista, colaPasajeros3));

        airportDatos.insert(new AirPort(20, "Cielo Alto Air Station", "ESTELIA", "Activo",
                lista, colaPasajeros3));

    }
    @Test
    void testInsertAndGetAll() throws IOException {
        Flight dep1 = new Flight(101, 1, 2, LocalDate.now().atTime(10, 30), 150, 0, "ON TIME");
        SinglyLinkedList lista = new SinglyLinkedList();
        AirPort airport = new AirPort(1, "LAX", "USA", "Activo", lista);
        airportDatos.insert(airport);

        List<AirPort> all = airportDatos.getAllAirPorts("activos");
        assertEquals(1, all.size());
        assertEquals("LAX", all.get(0).getName());
    }

    @Test
    void testBuscar() throws IOException {
        AirPort airport = new AirPort(2, "JFK", "USA", "Activo", null);
        airportDatos.insert(airport);

        assertTrue(airportDatos.buscar(2));
        assertFalse(airportDatos.buscar(999));
    }


    @Test
    void testActualizar() throws IOException {
        AirPort oldAirport = new AirPort(4, "CDG", "France", "Activo", null);
        AirPort newAirport = new AirPort(4, "CDG Updated", "France", "Inactivo", null);

        airportDatos.insert(oldAirport);
        boolean updated = airportDatos.actualizar(oldAirport, newAirport);
        assertTrue(updated);

        AirPort found = airportDatos.buscarAirPort(4);
        assertEquals("CDG Updated", found.getName());
       // assertFalse(found.isActive());
    }

    @Test
    void testBorrar() throws IOException {
        AirPort airport = new AirPort(5, "Narita", "Japan", "Activo", null);
        airportDatos.insert(airport);

        boolean deleted = airportDatos.borrar(5);
        assertTrue(deleted);
        assertNull(airportDatos.buscarAirPort(5));
    }
    @Test
    void testColaPasajeros() throws QueueException {
        LinkedQueue cola = new LinkedQueue();
        cola.enQueue(new Passenger(1, "Test", "Pais"));
        cola.enQueue(new Passenger(2, "Otro", "Pais2"));
        Node nodo = (Node) cola.frontN();
        while (nodo != null) {
            System.out.println("En cola: " + nodo.data);
            nodo = nodo.next;
        }
    }
    @Test
    void testTop5Aeropuertos() throws IOException {
        List<AirPort> all1 = airportDatos.getAllAirPorts("activos");
        List<AirPort> all2 = airportDatos.getAllAirPorts("inactivos");
        List<AirPort> all3 = airportDatos.getAllAirPorts("todos");

        System.out.println("----------Aeropuertos ACTIVOS------------");
        System.out.println(all1);
        System.out.println("----------Aeropuertos INACTIVOS----------");
        System.out.println(all2);
        System.out.println("----------TODOS LOS Aeropuertos-----------");
        System.out.println(all3);
    }
}