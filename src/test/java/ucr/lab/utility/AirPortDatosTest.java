package ucr.lab.utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.AirPort;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirPortDatosTest {

    String rutaArchivo = "src/main/resources/data/airports.json";
    private File tempFile = new File(rutaArchivo);
    private AirPortDatos airportDatos;


    @BeforeEach
    void setUp() throws IOException {
        File tempFile = new File(rutaArchivo);
       // tempFile = File.createTempFile("airports", ".json");
        airportDatos = new AirPortDatos(tempFile);
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testInsertAndGetAll() throws IOException {
        AirPort airport = new AirPort(1, "LAX", "USA", true, null);
        airportDatos.insert(airport);

        List<AirPort> all = airportDatos.getAllAirPorts("active");
        assertEquals(1, all.size());
        assertEquals("LAX", all.get(0).getName());
    }

    @Test
    void testBuscar() throws IOException {
        AirPort airport = new AirPort(2, "JFK", "USA", true, null);
        airportDatos.insert(airport);

        assertTrue(airportDatos.buscar(2));
        assertFalse(airportDatos.buscar(999));
    }


    @Test
    void testActualizar() throws IOException {
        AirPort oldAirport = new AirPort(4, "CDG", "France", true, null);
        AirPort newAirport = new AirPort(4, "CDG Updated", "France", false, null);

        airportDatos.insert(oldAirport);
        boolean updated = airportDatos.actualizar(oldAirport, newAirport);
        assertTrue(updated);

        AirPort found = airportDatos.buscarAirPort(4);
        assertEquals("CDG Updated", found.getName());
        assertFalse(found.isActive());
    }

    @Test
    void testBorrar() throws IOException {
        AirPort airport = new AirPort(5, "Narita", "Japan", true, null);
        airportDatos.insert(airport);

        boolean deleted = airportDatos.borrar(5);
        assertTrue(deleted);
        assertNull(airportDatos.buscarAirPort(5));
    }
}