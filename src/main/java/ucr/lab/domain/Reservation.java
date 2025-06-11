package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ucr.lab.TDA.list.SinglyLinkedList;
import java.util.List;

public class Reservation {
    private int code;
    private int userId;
    private int flightNumber;
    private String status;
    private SinglyLinkedList passengerIDs;

    public Reservation() {
        this.passengerIDs = new SinglyLinkedList();
    }

    public Reservation(int code) {
        this.passengerIDs = new SinglyLinkedList();
        this.code = code;
    }

    public Reservation(int code, int userId, int flightNumber, String status, SinglyLinkedList passengerIDs) {
        this.code = code;
        this.userId = userId;
        this.flightNumber = flightNumber;
        this.status = status;
        this.passengerIDs = passengerIDs;
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

    public void setCode(int code) {
        this.code = code;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPassengerIDs(SinglyLinkedList passengerIDs) {
        this.passengerIDs = passengerIDs;
    }

    public int getCode() {
        return code;
    }

    public int getUserId() {
        return userId;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public String getStatus() {
        return status;
    }

    public SinglyLinkedList listGetPassengerIDs() {
        return passengerIDs;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "code=" + code +
                ", userId=" + userId +
                ", flightNumber=" + flightNumber +
                ", status='" + status + '\'' +
                ", passengerIDs=" + passengerIDs +
                '}';
    }
}
