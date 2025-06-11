package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ucr.lab.TDA.list.SinglyLinkedList;
import java.time.LocalDateTime;
import java.util.List;

public class Flight {
    private int number;
    private int originCode;
    private int destinationCode;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;
    private String status;
    private SinglyLinkedList passengerIDs;
    private String route; //Se añade al terminar un vuelo. Ej: USA -> CR -> SPAIN

    public Flight() {
        this.passengerIDs = new SinglyLinkedList();
    }

    public Flight(int number) {
        this.number = number;
    }

    public Flight(int number, int originCode, int destinationCode, LocalDateTime departureTime, int capacity, int occupancy, String status, SinglyLinkedList passengerIDs) {
        this.number = number;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.status = status;
        this.passengerIDs = passengerIDs;
    }

    public boolean isFull() {
        return occupancy >= capacity;
    }

    @JsonSetter
    public void setPassengerIDs (List<Object> list) {
        SinglyLinkedList linkedList = new SinglyLinkedList();
        for (Object i : list)
            linkedList.add(i);
        this.passengerIDs = linkedList;
    }

    @JsonGetter
    public List<Object> getPassengerIDs () {
        return passengerIDs.toList();
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setOriginCode(int originCode) {
        this.originCode = originCode;
    }

    public void setDestinationCode(int destinationCode) {
        this.destinationCode = destinationCode;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPassengerIDs(SinglyLinkedList passengerIDs) {
        this.passengerIDs = passengerIDs;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getNumber() {
        return number;
    }

    public int getOriginCode() {
        return originCode;
    }

    public int getDestinationCode() {
        return destinationCode;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public String getStatus() {
        return status;
    }

    public SinglyLinkedList listGetPassengerIDs() {
        return passengerIDs;
    }

    public String getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "number=" + number +
                ", originCode=" + originCode +
                ", destinationCode=" + destinationCode +
                ", departureTime=" + departureTime +
                ", capacity=" + capacity +
                ", occupancy=" + occupancy +
                ", status='" + status + '\'' +
                ", passengerIDs=" + passengerIDs +
                ", route='" + route + '\'' +
                '}';
    }
}
