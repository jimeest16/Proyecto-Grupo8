package ucr.lab.domain;

import org.junit.jupiter.api.Test;
import ucr.lab.utility.FileReader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    public void testMostrarAeropuertosYRutas() {
        // ------------------ Aeropuertos ------------------
        List<AirPort> aeropuertos = FileReader.loadAirports();
        assertNotNull(aeropuertos, "La lista de aeropuertos es null");
        assertFalse(aeropuertos.isEmpty(), "La lista de aeropuertos está vacía");

        System.out.println("Aeropuertos cargados:");
        for (AirPort aeropuerto : aeropuertos) {
            System.out.println("- " + aeropuerto.getName());
        }

        // ------------------ Rutas ------------------
        List<Route> rutas = FileReader.loadRoutes();
        assertNotNull(rutas, "La lista de rutas es null");
        assertFalse(rutas.isEmpty(), "La lista de rutas está vacía");

        System.out.println("\nRutas cargadas:");
        for (Route ruta : rutas) {

                System.out.println(ruta);
            }
        }

}



