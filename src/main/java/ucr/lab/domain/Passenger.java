package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.ListException;
import ucr.lab.utility.Reader.SinglyListSerializer;
import ucr.lab.utility.Reader.SinglyListDeserializer;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger implements Comparable<Passenger> {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("nationality")
    private String nationality;


    @JsonSerialize(using = SinglyListSerializer.class) // Usa serializador
    @JsonDeserialize(using = SinglyListDeserializer.class) // Usadeserializador
    @JsonProperty("flightHistory")
    private SinglyLinkedList flightHistory;

    @JsonProperty("state")
    private String state;


    public Passenger() {
        this.flightHistory = new SinglyLinkedList();
        this.state = "N/A";
    }

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = new SinglyLinkedList();
        this.state = "Active";
    }

    public Passenger(int id, String name, String nationality, SinglyLinkedList flightHistory, String state) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = (flightHistory != null) ? flightHistory : new SinglyLinkedList();
        this.state = (state != null) ? state : "Active";
    }
 // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public SinglyLinkedList getFlightHistory() {
        return flightHistory;
    }

    public void setFlightHistory(SinglyLinkedList flightHistory) {
        this.flightHistory = flightHistory;
    }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public void addFlight(Flight flight) {
        if (this.flightHistory == null) {
            this.flightHistory = new SinglyLinkedList();
        }
        this.flightHistory.add(flight);
    }

    public void clearFlightHistory() {
        if (this.flightHistory != null) {
            this.flightHistory.clear();
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════\n");
        sb.append("👤 ID: ").append(id).append("\n");
        sb.append("  Nombre: ").append(name).append("\n");
        sb.append("  Nacionalidad: ").append(nationality).append("\n");
        sb.append("  Estado: ").append(state).append("\n");
        sb.append("  ✈️ Historial de Vuelos:\n");

        if (flightHistory != null && !flightHistory.isEmpty()) {
            try {
                for (int i = 1; i <= flightHistory.size(); i++) {

                    Flight flight = (Flight) flightHistory.get(i);
                    sb.append("    - ").append(flight.toString()).append("\n");
                }
            } catch (ListException e) {
                sb.append("    [Error al cargar historial de vuelos: ").append(e.getMessage()).append("]\n");
            } catch (ClassCastException e) {

                sb.append("    [ERROR: Dato inesperado en el historial de vuelos (no es un objeto Flight). ").append(e.getMessage()).append("]\n");
            }
        } else {
            sb.append("    (Este pasajero no tiene vuelos registrados)\n");
        }
        sb.append("══════════════════════════════════════════════════\n");
        return sb.toString();
    }

    @Override
    public int compareTo(Passenger other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id == passenger.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public void removeFlight(Flight flightToRemove) throws ListException {
        if (this.flightHistory == null || this.flightHistory.isEmpty()) {
            throw new ListException("Flight history is empty. Cannot remove flight.");
        }


        for (int i = 1; i <= this.flightHistory.size(); i++) {
            Flight currentFlight = (Flight) this.flightHistory.get(i);

            if (currentFlight.equals(flightToRemove)) {
                this.flightHistory.remove(i); // Remove by index
                return;
            }
        }

        throw new ListException("Flight not found in passenger's history: " + flightToRemove.getNumber());
    }

}