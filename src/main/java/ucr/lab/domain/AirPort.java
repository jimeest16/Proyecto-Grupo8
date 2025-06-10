package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ucr.lab.TDA.SinglyLinkedList;

import java.util.List;
import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)

public class AirPort {
    private int code;
    private String name;
    private String country;
    private String status;// ACTIV0 O INACTIVO
    private Departures departuresBoard;

    public AirPort() {
    }

    public AirPort(int code, String name, String country, String status, Departures departuresBoard) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.departuresBoard = departuresBoard; // Lista de vuelos
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

    public String getStatus() {
        return status;
    }

    public Departures getDeparturesBoard() {
        return departuresBoard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirPort airPort = (AirPort) o;
        return code == airPort.code && Objects.equals(name, airPort.name) && Objects.equals(country, airPort.country) && Objects.equals(status, airPort.status) && Objects.equals(departuresBoard, airPort.departuresBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, country, status, departuresBoard);
    }

    @Override
    public String toString() {
        return "AirPort{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", active=" + status +
                ", departuresBoard=" + departuresBoard +
                '}';
    }
}
