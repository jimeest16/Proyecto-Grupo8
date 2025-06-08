package ucr.lab.TDA;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Departures;
import ucr.lab.domain.Flight;
import ucr.lab.domain.User;
import ucr.lab.utility.FileReader;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void addAirport() {

        // Crear AEROPUERTOS y lista de salidas
        List<Departures> departuresList = FileReader.loadDepartures();
        Departures departures = new Departures();
        departuresList.add(new Departures((LocalDate.now().plusDays(4)), "Tokyo", "C05", "CANCELLED"));
        departuresList.add(new Departures(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED"));

//new Flight(1, "CR", "USA", LocalDateTime.now().minusDays(30), 30)
//new Flight(2, "USA", "CR", LocalDateTime.now().minusDays(31), 35))
        // Crear aeropuertos
        AirPort airport1 = new AirPort(1, "Aeropuerto Internacional de Los Ángeles", "USA", "Activo", departures);
        AirPort airport2 = new AirPort(2, "Aeropuerto Internacional John F.Kennedy", "USA", "Activo", departures);


        // Agregar aeropuertos
        FileReader.addAirport(airport1);
        FileReader.addAirport(airport2);

        // Cargar lista después de agregar
        List<AirPort> airportList = FileReader.loadAirports();

        // Verificar resultados


    }
}