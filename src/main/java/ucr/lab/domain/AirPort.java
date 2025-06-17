package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.utility.Util;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)

public class AirPort {
    private int code;
    private String name;
    private String country;
    private String status;// ACTIV0 O INACTIVO
    //private Departures departuresBoard;
    private SinglyLinkedList departuresBoard; //tipo Flight
    @JsonProperty("waitingQueue")
    private LinkedQueue waitingQueue; // tipo Passenger
    public AirPort() {
    }

    //completo este lo usaria flight
    public AirPort(int code, String name, String country, String status, SinglyLinkedList departuresBoard, LinkedQueue waitingQueue) throws QueueException {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.departuresBoard = departuresBoard; // Lista de vuelos
        LinkedQueue queue = new LinkedQueue();
        for (Passenger p : Util.getPassengerInList()) {
            queue.enQueue(p);
        }
        this.waitingQueue = queue;//Cola de espera de pasajeros

    }
    //este es para el manager Airport
    public AirPort(int code, String name, String country, String status, SinglyLinkedList departuresBoard) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.departuresBoard = departuresBoard; // Lista de vuelos
    }

    public AirPort(int code, String name, String country, String status) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.departuresBoard = null;
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

    public SinglyLinkedList getDeparturesBoard() {
        return departuresBoard;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDeparturesBoard(SinglyLinkedList departuresBoard) {
        this.departuresBoard = departuresBoard;
    }

    public LinkedQueue getWaitingQueue() {
        return waitingQueue;
    }

    public void setWaitingQueue(LinkedQueue waitingQueue) {
        this.waitingQueue = waitingQueue;
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
