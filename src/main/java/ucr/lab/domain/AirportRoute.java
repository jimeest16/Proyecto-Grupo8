// AirportRoute.java
package ucr.lab.domain; // Asegúrate de que el paquete sea el correcto

import ucr.lab.TDA.list.SinglyLinkedList; // Asumiendo que tu TDA SinglyLinkedList no es genérico

public class AirportRoute {
    private Integer originAirportCode; // Coincide con 'code' de AirPort
    // Aquí usamos tu SinglyLinkedList que no es genérica y asumimos que contendrá objetos Destination
    private SinglyLinkedList destinationList; // No genérica, contendrá objetos Destination

    public AirportRoute(Integer originAirportCode) {
        this.originAirportCode = originAirportCode;
        this.destinationList = new SinglyLinkedList(); // Inicializa la lista no genérica
    }

    // Getters y Setters
    public Integer getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(Integer originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    // El getter devuelve SinglyLinkedList (no genérica)
    public SinglyLinkedList getDestinationList() {
        return destinationList;
    }

    // El setter acepta SinglyLinkedList (no genérica)
    public void setDestinationList(SinglyLinkedList destinationList) {
        this.destinationList = destinationList;
    }

    // Método para añadir un destino, asumiendo que es un objeto Destination
    public void addDestination(Destination destination) {
        this.destinationList.add(destination); // Añade el objeto Destination a la lista no genérica
    }

    @Override
    public String toString() {
        return "AirportRoute{" +
                "originAirportCode=" + originAirportCode +
                ", destinationList=" + destinationList +
                '}';
    }
}