package ucr.lab.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;

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
/*
    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }*/

    @Test
    public void cargarAirports() throws IOException {
        File tempFile = new File(rutaArchivo);
        airportDatos = new AirPortDatos(tempFile);

        airportDatos.insert(new AirPort(1, "Los Angeles International Airport", "USA", "Activo",
                new Departures(LocalDate.now(), "NEW YORK", "A01", "On Time")));

        airportDatos.insert(new AirPort(2, "John F. Kennedy International Airport", "USA", "Inactivo",
                new Departures(LocalDate.now().plusDays(1), "MIAMI", "B02", "Delayed")));

        airportDatos.insert(new AirPort(3, "Aeropuerto Internacional de San José", "COSTA RICA", "Activo",
                new Departures(LocalDate.now().plusDays(2), "GUANACASTE", "C03", "On Time")));

        airportDatos.insert(new AirPort(4, "Ciudad Celeste International Airport", "TERRANOVA", "Inactivo",
                new Departures(LocalDate.now().plusDays(3), "NOVA LUMEN", "D04", "Cancelled")));

        airportDatos.insert(new AirPort(5, "Narita International Airport", "JAPAN", "Activo",
                new Departures(LocalDate.now().plusDays(4), "TOKYO", "E05", "Boarding")));

        airportDatos.insert(new AirPort(6, "Frankfurt Airport", "GERMANY", "Activo",
                new Departures(LocalDate.now().plusDays(5), "BERLIN", "F06", "On Time")));

        airportDatos.insert(new AirPort(7, "Aeropuerto de Buenos Aires", "ARGENTINA", "Inactivo",
                new Departures(LocalDate.now().plusDays(6), "CORDOBA", "G07", "Delayed")));

        airportDatos.insert(new AirPort(8, "Dragon City AirHub", "FANTASIA", "Activo",
                new Departures(LocalDate.now().plusDays(7), "ELDERON", "H08", "On Time")));

        airportDatos.insert(new AirPort(9, "Aeropuerto Internacional Arturo Merino Benítez", "CHILE", "Activo",
                new Departures(LocalDate.now().plusDays(8), "VALPARAISO", "I09", "On Time")));

        airportDatos.insert(new AirPort(10, "Beijing Capital International Airport", "CHINA", "Activo",
                new Departures(LocalDate.now().plusDays(9), "SHANGHAI", "J10", "Cancelled")));

        airportDatos.insert(new AirPort(11, "Aeropuerto de Atlantis", "MITICA", "Inactivo",
                new Departures(LocalDate.now().plusDays(10), "POSEIDONIA", "K11", "On Time")));

        airportDatos.insert(new AirPort(12, "Aeropuerto Internacional de Monterrey", "MEXICO", "Activo",
                new Departures(LocalDate.now().plusDays(11), "CDMX", "L12", "Boarding")));

        airportDatos.insert(new AirPort(13, "Cape Town International Airport", "SOUTH AFRICA", "Activo",
                new Departures(LocalDate.now().plusDays(12), "JOHANNESBURG", "M13", "On Time")));

        airportDatos.insert(new AirPort(14, "Aeropuerto de Nimbus Prime", "AETHER", "Activo",
                new Departures(LocalDate.now().plusDays(13), "ZEPHIR", "N14", "Delayed")));

        airportDatos.insert(new AirPort(15, "Aeropuerto de Lima", "PERU", "Inactivo",
                new Departures(LocalDate.now().plusDays(14), "CUSCO", "O15", "Boarding")));

        airportDatos.insert(new AirPort(16, "Istanbul Airport", "TURKEY", "Activo",
                new Departures(LocalDate.now().plusDays(15), "ANKARA", "P16", "On Time")));

        airportDatos.insert(new AirPort(17, "Barcelona–El Prat Airport", "SPAIN", "Activo",
                new Departures(LocalDate.now().plusDays(16), "MADRID", "Q17", "On Time")));

        airportDatos.insert(new AirPort(18, "Aeropuerto Internacional Simón Bolívar", "VENEZUELA", "Inactivo",
                new Departures(LocalDate.now().plusDays(17), "MARACAIBO", "R18", "Delayed")));

        airportDatos.insert(new AirPort(19, "Zürich Airport", "SWITZERLAND", "Activo",
                new Departures(LocalDate.now().plusDays(18), "GENEVA", "S19", "On Time")));

        airportDatos.insert(new AirPort(20, "Cielo Alto Air Station", "ESTELIA", "Activo",
                new Departures(LocalDate.now().plusDays(19), "LUNARIA", "T20", "Cancelled")));

    }
    @Test
    void testInsertAndGetAll() throws IOException {
        Departures dep1 = new Departures(LocalDate.now(),"LONDON", "A01","On Time");
        AirPort airport = new AirPort(1, "LAX", "USA", "Activo", dep1);
        airportDatos.insert(airport);

        List<AirPort> all = airportDatos.getAllAirPorts("active");
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
}