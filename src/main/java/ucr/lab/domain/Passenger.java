package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.utility.Reader.SinglyReader;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("nationality")
    private String nationality;


    @JsonDeserialize(using = SinglyReader.class)
    @JsonProperty("flightHistory")
    private SinglyLinkedList flightHistory;

    @JsonProperty("state")
    private String state;

    // Constructor por defecto para la deserialización de Jackson
    public Passenger() {
        this.flightHistory = new SinglyLinkedList(); // Asegura que la lista siempre esté inicializada
    }

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = new SinglyLinkedList(); // Inicializa la lista
        this.state = "N/A"; // Estado por defecto si no se proporciona
    }

    public Passenger(int id, String name, String nationality, SinglyLinkedList flightHistory, String state) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        // Asegura que flightHistory no sea nulo; si es nulo, crea una nueva
        this.flightHistory = flightHistory != null ? flightHistory : new SinglyLinkedList();
        this.state = state != null ? state : "N/A"; // Asigna el estado proporcionado o por defecto
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public SinglyLinkedList getFlightHistory() { return flightHistory; }
    public void setFlightHistory(SinglyLinkedList flightHistory) { this.flightHistory = flightHistory; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }



    public void addFlight(String flight) {
        if (flight != null && !flight.isEmpty()) {
            this.flightHistory.add(flight);
        }
    }

    public void clearFlightHistory() {
        this.flightHistory.clear();
    }

    @Override
    public String toString() {

        return "\uD83D\uDC64  ID: " + id + ", Nombre: " + name + ", Nacionalidad: " + nationality + ", Vuelos: " + flightHistory.toString() + ", Estado: " + state;
    }
}