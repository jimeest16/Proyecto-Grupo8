package ucr.lab.domain;

import ucr.lab.TDA.SinglyLinkedList;

// OBJETIVO:
//agregar rutas
//modificar rutas
//calcular la ruta mas corta disjtra

//IMPORTANTE:
// agregar y modificar va en la parte del admin
// el usuario solo puede ver la ruta mas corta entre dos aeropuertos

public class Route {
    private int originAirportCode;
    private SinglyLinkedList destinationList; // lista de objetos Destination

    public Route(int originAirportCode) {
        this.originAirportCode = originAirportCode;
        this.destinationList = new SinglyLinkedList();
    }

    public int getOriginAirportCode() {
        return originAirportCode;
    }

    public SinglyLinkedList getDestinationList() {
        return destinationList;
    }

    public void addDestination(int destAirportCode, double distance) {
        destinationList.add(new Destination(destAirportCode, distance));
    }

    @Override
    public String toString() {
        return "Ruta desde aeropuerto " + originAirportCode + ": " + destinationList.toString();
    }
    // cada ruta puede ser:
    // se ouede agregar una ruta cn peso en kilometros
    // modificar una ruta-> cambiar distancia
    // calcular al ruta mas corta entre dos aeropuertos utilizando disjsktra
}
