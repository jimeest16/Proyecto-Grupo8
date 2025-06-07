package ucr.lab.domain;

import java.util.ArrayList;
import java.util.List;

public class Passenger {
    private int id;
    private String name;
    private String nationality;
    private List<String> flightHistory;

    public Passenger() {
        this.flightHistory = new ArrayList<>();
    }

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = new ArrayList<>();
    }

    public void addFlight(String flight) {
        if (flight != null && !flight.isEmpty()) {
            flightHistory.add(flight);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public List<String> getFlightHistory() { return flightHistory; }
    public void setFlightHistory(List<String> flightHistory) { this.flightHistory = flightHistory; }

    @Override
    public String toString() {
        return "\uD83D\uDC64  ID: " + id + ", Nombre: " + name + ", Nacionalidad: " + nationality + ", Vuelos: " + flightHistory;
    }

    public void clearFlightHistory() {

            this.flightHistory.clear(); // Suponiendo que flightHistory es un List<String>
        }


}
