package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

public class AirPort {
    private int code;
    private String name;
    private String country;
    private boolean active;
    private SinglyLinkedList departuresBoard;

    public AirPort(int code, String name, String country) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.active = true;
        this.departuresBoard = new SinglyLinkedList(); // Lista de vuelos
    }

    // Getters y Setters

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public boolean isActive() {
        return active;
    }

    public SinglyLinkedList getDeparturesBoard() {
        return departuresBoard;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "AirPort{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", active=" + active +
                ", departuresBoard=" + departuresBoard +
                '}';
    }
}
