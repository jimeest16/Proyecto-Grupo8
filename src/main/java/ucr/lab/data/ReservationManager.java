package ucr.lab.data;

import com.fasterxml.jackson.core.type.TypeReference;
import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.domain.Reservation;

import java.io.IOException;
import java.util.List;

public class ReservationManager {
    private static SinglyLinkedList reservations = new SinglyLinkedList();

    private static final String filePath = "src/main/resources/data/reservation.json";

    public static void loadReservations() throws IOException {
        List<Reservation> list = JsonManager.load(filePath, new TypeReference<>() {});
        reservations.clear();
        for (Reservation reservation : list)
            reservations.add(reservation);
    }

    public static void saveReservations() throws IOException {
        List<Reservation> list = reservations.toList();
        JsonManager.save(filePath, list);
    }

    public static void add(Reservation r) throws IOException {
        reservations.add(r);
        saveReservations();
    }

    public static void remove(Reservation r) throws IOException, ListException {
        reservations.remove(r);
        saveReservations();
    }

    public static SinglyLinkedList getReservations() {
        return reservations;
    }

    public static void setReservations(SinglyLinkedList reservations) {
        ReservationManager.reservations = reservations;
    }
}
