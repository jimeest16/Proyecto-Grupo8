package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ucr.lab.TDA.queue.LinkedQueue;
import ucr.lab.TDA.queue.QueueException;
import java.util.ArrayList;
import java.util.List;

public class Airport {
    private int code;
    private String name;
    private String country;
    private String status;// ACTIV0 O INACTIVO
    private List<LinkedQueue> boardingQueues;

    public Airport() {
    }

    public Airport(int code) {
        this.code = code;
    }

    public Airport(int code, String name, String country, String status) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.boardingQueues = new ArrayList<>();
    }

    public Airport(int code, String name, String country, String status, List<LinkedQueue> boardingQueues) {
        this.code = code;
        this.name = name;
        this.country = country;
        this.status = status;
        this.boardingQueues = boardingQueues;
    }

    @JsonSetter
    public void setBoardingQueues(List<List<Object>> boardingQueues) throws QueueException {
        this.boardingQueues = new ArrayList<>();
        for (List<Object> list : boardingQueues) {
            LinkedQueue queue = new LinkedQueue();
            for (Object boarding : list)
                queue.enQueue(boarding);
            this.boardingQueues.add(queue);
        }
    }

    @JsonGetter
    public List<List<Object>> getBoardingQueues() {
        List<List<Object>> boardingQueues = new ArrayList<>();
        for (LinkedQueue queue : this.boardingQueues)
            boardingQueues.add(queue.toList());
        return boardingQueues;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void listSetBoardingQueues(List<LinkedQueue> boardingQueues) {
        this.boardingQueues = boardingQueues;
    }

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

    public List<LinkedQueue> listGetBoardingQueues() {
        return boardingQueues;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", status='" + status + '\'' +
                ", boardingQueues=" + boardingQueues +
                '}';
    }
}
