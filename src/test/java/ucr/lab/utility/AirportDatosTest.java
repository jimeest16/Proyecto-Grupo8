package ucr.lab.utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.Airport;
import ucr.lab.domain.Departure;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportDatosTest {
    String rutaArchivo = "src/main/resources/data/airports.json";
    private File tempFile = new File(rutaArchivo);
    private AirPortDatos airportDatos;

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = new File(rutaArchivo);
        airportDatos = new AirPortDatos(tempFile);
    }

    @Test
    void testInsertAndGetAll() throws IOException {
        //Departure dep1 = new Departure(LocalDate.now(),"LONDON", "A01","On Time");
        //Airport airport = new Airport(1, "LAX", "USA", "Activo", dep1);
        //airportDatos.insert(airport);

        List<Airport> all = airportDatos.getAllAirPorts("active");
        assertEquals(1, all.size());
        assertEquals("LAX", all.get(0).getName());
    }

    @Test
    void testBuscar() throws IOException {
        Airport airport = new Airport(2, "JFK", "USA", "Activo", null);
        airportDatos.insert(airport);

        assertTrue(airportDatos.buscar(2));
        assertFalse(airportDatos.buscar(999));
    }


    @Test
    void testActualizar() throws IOException {
        Airport oldAirport = new Airport(4, "CDG", "France", "Activo", null);
        Airport newAirport = new Airport(4, "CDG Updated", "France", "Inactivo", null);

        airportDatos.insert(oldAirport);
        boolean updated = airportDatos.actualizar(oldAirport, newAirport);
        assertTrue(updated);

        Airport found = airportDatos.buscarAirPort(4);
        assertEquals("CDG Updated", found.getName());
       // assertFalse(found.isActive());
    }

    @Test
    void testBorrar() throws IOException {
        Airport airport = new Airport(5, "Narita", "Japan", "Activo", null);
        airportDatos.insert(airport);

        boolean deleted = airportDatos.borrar(5);
        assertTrue(deleted);
        assertNull(airportDatos.buscarAirPort(5));
    }
}