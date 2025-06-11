package ucr.lab.TDA;

import org.junit.jupiter.api.Test;
import ucr.lab.TDA.list.DoublyLinkedList;
import ucr.lab.domain.Departure;
import ucr.lab.utility.FileReader;
import java.util.List;

class DoublyLinkedListTest {

    @Test
    void addAirport() {
        DoublyLinkedList list = new DoublyLinkedList();

        // Crear AEROPUERTOS y lista de salidas
        List<Departure> departureList = FileReader.loadDepartures();
        //Departure departure = new Departure();
        //departureList.add(new Departure((LocalDate.now().plusDays(4)), "Tokyo", "C05", "CANCELLED"));
        //departureList.add(new Departure(LocalDate.now().plusDays(2), "New York", "B12", "DELAYED"));

//new Flight(1, "CR", "USA", LocalDateTime.now().minusDays(30), 30)
//new Flight(2, "USA", "CR", LocalDateTime.now().minusDays(31), 35))
        // Crear aeropuertos
        //Airport airport1 = new Airport(1, "Aeropuerto Internacional de Los Ángeles", "USA", "Activo", departure);
        /*Airport airport2 = new Airport(2, "Aeropuerto Internacional John F.Kennedy", "USA", "Activo", departure);
        list.add(airport1);
        list.add(airport2);
        System.out.println(list);
        // Agregar aeropuertos
        FileReader.addAirport(airport1);
        FileReader.addAirport(airport2);

        // Cargar lista después de agregar
        List<Airport> airportList = FileReader.loadAirports();

        // Verificar resultados
*/

    }
}