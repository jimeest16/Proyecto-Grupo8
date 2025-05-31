package ucr.lab.TDA;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ucr.lab.domain.AirPort;
import ucr.lab.domain.Flight;
import ucr.lab.domain.User;
import ucr.lab.utility.FileReader;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    void addAirport() {

        // Crear vuelos y lista de salidas
        SinglyLinkedList departures = new SinglyLinkedList();
        departures.add(new Flight(1, "CR", "USA", LocalDateTime.now().minusDays(30), 30));
        departures.add(new Flight(2, "USA", "CR", LocalDateTime.now().minusDays(31), 35));

        // Crear aeropuertos
        AirPort airport1 = new AirPort(1, "Aeropuerto Internacional de Los Ángeles", "USA", true, departures);
        AirPort airport2 = new AirPort(2, "Aeropuerto Internacional John F.Kennedy", "USA", false, departures);


        // Agregar aeropuertos
        FileReader.addAirport(airport1);
        FileReader.addAirport(airport2);

        // Cargar lista después de agregar
        List<AirPort> airportList = FileReader.loadAirports();

        // Verificar resultados


    }
}