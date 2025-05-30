package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

public class Route {

    private int originAirportCode;
    private SinglyLinkedList destinationList; // Cada nodo contiene destino y peso

    public Route(int originAirportCode) {
        this.originAirportCode = originAirportCode;
        this.destinationList = new SinglyLinkedList();
    }

    // Getters y Setters

    public int getOriginAirportCode() {
        return originAirportCode;
    }

    public SinglyLinkedList getDestinationList() {
        return destinationList;
    }

    @Override
    public String toString() {
        return "Route{" +
                "originAirportCode=" + originAirportCode +
                ", destinationList=" + destinationList +
                '}';
    }
}
