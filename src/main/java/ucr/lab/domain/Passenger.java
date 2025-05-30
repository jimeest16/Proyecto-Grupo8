package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

public class Passenger {

    private int id;
    private String name;
    private String nationality;
    private SinglyLinkedList flightHistory;

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = new SinglyLinkedList();
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public SinglyLinkedList getFlightHistory() {
        return flightHistory;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", flightHistory=" + flightHistory +
                '}';
    }
}
