package ucr.lab.TDA.list;

import org.junit.jupiter.api.Test;
import ucr.lab.domain.Flight;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SinglyLinkedListTest {

    @Test
    void test() {
        Flight flight1 = new Flight(123,852,896, LocalDateTime.now(),30,25,"ON TIME");
        Flight flight2 = new Flight(123,852,896, LocalDateTime.of(2025,06,16,10,30),30,25,"ON TIME");
        SinglyLinkedList listaVuelos = new SinglyLinkedList();
        listaVuelos.add(flight1);
        listaVuelos.add(flight2);

        System.out.println(listaVuelos);
    }
}