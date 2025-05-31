package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

import java.util.Objects;

public class AirPort {
    private int code;
    private String name;
    private String country;
    private boolean active;// IF FALSE NO ACTIVE
    private SinglyLinkedList departuresBoard;

    public AirPort() {
    }

    public AirPort(int code, String name, String country, boolean active, SinglyLinkedList departuresBoard) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.active = active;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirPort airPort = (AirPort) o;
        return code == airPort.code && active == airPort.active && Objects.equals(name, airPort.name) && Objects.equals(country, airPort.country) && Objects.equals(departuresBoard, airPort.departuresBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, country, active, departuresBoard);
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
