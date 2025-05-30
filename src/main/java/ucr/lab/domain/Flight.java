package ucr.lab.domain;

import java.time.LocalDateTime;

public class Flight {

    private int number;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;

    public Flight(int number, String origin, String destination, LocalDateTime departureTime, int capacity) {
        this.number = number;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = 0;
    }

    // Getters y Setters

    public int getNumber() {
        return number;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
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

    public void addPassenger() {
        if (occupancy < capacity) {
            occupancy++;
        }
    }

    public boolean isFull() {
        return occupancy >= capacity;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "number=" + number +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime=" + departureTime +
                ", capacity=" + capacity +
                ", occupancy=" + occupancy +
                '}';
    }
}
