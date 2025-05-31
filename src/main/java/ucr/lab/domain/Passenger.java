package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

import java.util.ArrayList;
import java.util.List;

public class Passenger {
    private int id;
    private String name;
    private String nationality;
    private String flight_history;

    public Passenger() {
        // Constructor requerido por Jackson
    }
    public Passenger(int id, String name, String nationality, String flight_history) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flight_history = flight_history;
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

    public String getFlight_history() {
        return flight_history;
    }

    @Override
    public String toString() {
        return "Passenger{id=" + id + ", name='" + name + '\'' + ", nationality='" + nationality + '\'' + ", flight_history='" + flight_history + '\'' + '}';
    }
}

