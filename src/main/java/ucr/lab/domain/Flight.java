package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.TDA.list.ListException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    @JsonProperty("flightNumber")
    private int number;
    @JsonProperty("originAirportCode")
    private int originCode;
    @JsonProperty("destinationAirportCode")
    private int destinationCode;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;
    private String status;

    private SinglyLinkedList passengerIDs;
    private String route;

    public Flight() {
        this.passengerIDs = new SinglyLinkedList();
    }

    public Flight(int number) {
        this();
        this.number = number;
    }

    public Flight(int number, int originCode, int destinationCode, LocalDateTime departureTime, int capacity, int occupancy, String status) {
        this();
        this.number = number;
        this.originCode = originCode;
        this.destinationCode = destinationCode;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = occupancy;
        this.status = status;
        this.route = "";
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
        this.route = "";
    }


    public Flight(int capacity, int occupancy, String status, String route, LocalDateTime departureTime,
                  int flightNumber, int originAirportCode, int destinationAirportCode,
                  LocalDateTime departureDate, SinglyLinkedList passengerIDsList) {
        this();

        this.capacity = capacity;
        this.occupancy = occupancy;
        this.status = status;
        this.route = route;
        this.departureTime = departureTime;
        this.number = flightNumber;
        this.originCode = originAirportCode;
        this.destinationCode = destinationCode;

        this.passengerIDs = passengerIDsList != null ? passengerIDsList : new SinglyLinkedList();
    }

    @JsonIgnore
    public boolean isFull() {
        return occupancy >= capacity;
    }

    @JsonSetter("passengerIDs")
    public void setPassengerIDs(List<Object> list) {
        SinglyLinkedList linkedList = new SinglyLinkedList();
        if (list != null) {
            for (Object id : list) {
                linkedList.add(id);
            }
        }
        this.passengerIDs = linkedList;
    }

    @JsonGetter("passengerIDs")
    public List<Object> getPassengerIDsAsList() {
        List<Object> list = new ArrayList<>();
        if (this.passengerIDs != null && !this.passengerIDs.isEmpty()) {
            try {
                for (int i = 1; i <= this.passengerIDs.size(); i++) {
                    list.add(this.passengerIDs.get(i));
                }
            } catch (ListException e) {
                System.err.println("Error convirtiendo SinglyLinkedList a List para serialización JSON: " + e.getMessage());
            }
        }
        return list;
    }

    @JsonSetter("departureDate")
    public void setDepartureTime(String departureTimeStr) {
        if (departureTimeStr != null && !departureTimeStr.isEmpty()) {
            this.departureTime = LocalDateTime.parse(departureTimeStr);
        }
    }

    @JsonGetter("departureDate")
    public String getDepartureDate() {
        return this.departureTime != null ? this.departureTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    public void setNumber(int number) { this.number = number; }
    public void setOriginCode(int originCode) { this.originCode = originCode; }
    public void setDestinationCode(int destinationCode) { this.destinationCode = destinationCode; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setOccupancy(int occupancy) { this.occupancy = occupancy; }
    public void setStatus(String status) { this.status = status; }
    public void setPassengerIDs(SinglyLinkedList passengerIDs) { this.passengerIDs = passengerIDs; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public int getNumber() { return number; }
    public int getOriginCode() { return originCode; }
    public int getDestinationCode() { return destinationCode; }
    @JsonIgnore
    public LocalDateTime getDepartureTimeAsObject() { return departureTime; }
    public int getCapacity() { return capacity; }
    public int getOccupancy() { return occupancy; }
    public String getStatus() { return status; }

    public SinglyLinkedList getPassengerIDs() { return passengerIDs; }

    // darle formato mejor
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Número: ").append(number)
                .append(" | Ruta: ");

        if (route != null && !route.isEmpty()) {
            sb.append(route);
        } else {
            sb.append(originCode).append("->").append(destinationCode);
        }

        if (departureTime != null) {
            sb.append(" | Salida: ").append(departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            sb.append(" | Salida: N/A");
        }

        sb.append(" | Estado: ").append(status)
                .append(" | Ocupación: ").append(occupancy).append("/").append(capacity);

        if (passengerIDs != null && !passengerIDs.isEmpty()) {
            try {
                List<String> ids = new ArrayList<>();
                for (int i = 1; i <= passengerIDs.size(); i++) {
                    Object passengerIdObj = passengerIDs.get(i);
                    if (passengerIdObj != null) {
                        ids.add(passengerIdObj.toString());
                    }
                }
                sb.append(" | Pasajeros (IDs): ").append(String.join(", ", ids));
            } catch (ListException e) {
                sb.append(" | Pasajeros (IDs): [Error al listar]");
            } catch (Exception e) {
                sb.append(" | Pasajeros (IDs): [ERROR: Tipo inesperado de ID - ").append(e.getMessage()).append("]");
            }
        } else {
            sb.append(" | Sin Pasajeros Registrados");
        }
        return sb.toString();
    }
}