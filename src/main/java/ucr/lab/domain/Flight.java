package ucr.lab.domain;

import ucr.lab.TDA.LinkedQueue;
import ucr.lab.TDA.QueueException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {

    private int number;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private int capacity;
    private int occupancy;

    private LinkedQueue colaEspera;
    private List pasajerosAsignados;

    public Flight(int number, String origin, String destination, LocalDateTime departureTime, int capacity) {
        this.number = number;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.occupancy = 0;

        this.colaEspera = new LinkedQueue();
        this.pasajerosAsignados = new ArrayList();
    }


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

    public LinkedQueue getColaEspera() {
        return colaEspera;
    }

    public List getPasajerosAsignados() {
        return pasajerosAsignados;
    }


    public void asignarPasajero(Passenger pasajero) throws QueueException {
        if (occupancy < capacity) {
            pasajerosAsignados.add(pasajero);
            occupancy++;
        } else {
            colaEspera.enQueue(pasajero);
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
