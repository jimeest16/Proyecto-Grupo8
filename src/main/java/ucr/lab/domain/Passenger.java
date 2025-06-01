package ucr.lab.domain;

import ucr.lab.TDA.ListException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Passenger {
    private int id;
    private String name;
    private String nationality;
    private LinkedList<String> flightHistory;

    public Passenger() {
        // Constructor requerido por Jackson
    }

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public void addFlight(String flightNumber) {
        flightHistory.addLast(flightNumber);
    }

    public LinkedList getFlightHistory() {
        return flightHistory;
    }

    @Override
    public String toString() {
        return "Passenger{id=" + id + ", name='" + name + '\'' + ", nationality='" + nationality + '\'' + ", flight_history='" + flightHistory + '\'' + '}';
    }

    public PassengerToJson toDTO() throws ListException {
        List<String> history = new ArrayList<>();
        for (int i = 1; i <= flightHistory.size(); i++) {
            history.add((String) flightHistory.get(i));
        }
        return new PassengerToJson(id, name, nationality, history);
    }

    public static Passenger fromDTO(PassengerToJson dto) {
        Passenger p = new Passenger(dto.id, dto.name, dto.nationality);
        for (String flight : dto.flightHistory) {
            p.addFlight(flight);
        }
        return p;
    }

}










