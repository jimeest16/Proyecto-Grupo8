package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import ucr.lab.TDA.Node;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import ucr.lab.utility.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private List<Passenger> waitingPassengers;//para serializar
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
        this.waitingQueue = waitingQueue;//Cola de espera de pasajeros

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

    //para que se lea correctamente la cola de espera
    public void prepareForSerialization() throws QueueException {
        waitingPassengers = new ArrayList<>();
        if (waitingQueue != null) {
            Node nodo = (Node) waitingQueue.frontN();
            while (nodo != null) {
                Object obj = nodo.data;
                if (obj instanceof Passenger) {
                    waitingPassengers.add((Passenger) obj);
                } else {
                    // Opcional: tratar o loggear caso inesperado
                    System.out.println("WARN: elemento en cola no es Passenger en prepareForSerialization: " + obj.getClass());
                }
                nodo = nodo.next;
            }
        }
    }

    // Después de deserializar desde JSON: reconstruye waitingQueue
    public void afterDeserialization(Gson gson) throws QueueException {
        waitingQueue = new LinkedQueue();
        if (waitingPassengers != null) {
            for (Object item : waitingPassengers) {
                if (item instanceof Passenger) {
                    waitingQueue.enQueue((Passenger) item);
                } else if (item instanceof Map) {
                    // En caso de que Gson haya deserializado como LinkedTreeMap: reconvertir
                    // Convierte el Map a JSON y luego a Passenger
                    String json = gson.toJson(item);
                    Passenger p = gson.fromJson(json, Passenger.class);
                    waitingQueue.enQueue(p);
                } else {
                    // Opción: loggear o ignorar
                    System.out.println("WARN: elemento unexpected en waitingPassengers: " + item.getClass());
                }
            }
        }
    }
}
