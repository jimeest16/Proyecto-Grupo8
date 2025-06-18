package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ucr.lab.TDA.list.ListException;
import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.utility.Reader.SinglyReader;
import ucr.lab.utility.Writer.SinglyWriter;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    @JsonProperty("originAirport")
    private int originAirportCode;

    @JsonDeserialize(using = SinglyReader.class)
    @JsonSerialize(using = SinglyWriter.class)
    @JsonProperty("destinationList")
    private SinglyLinkedList destinationList;

    public Route() {
        this.destinationList = new SinglyLinkedList();
    }

    public Route(int originAirportCode, SinglyLinkedList destinationList) {
        this.originAirportCode = originAirportCode;
        this.destinationList = destinationList != null ? destinationList : new SinglyLinkedList();
    }

    // Getters y Setters...
    public int getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(int originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    public SinglyLinkedList getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(SinglyLinkedList destinationList) {
        this.destinationList = destinationList;
    }

    @Override
    public String toString() {
        StringBuilder dests = new StringBuilder();
        if (destinationList != null && !destinationList.isEmpty()) {
            try {
                for (int i = 1; i <= destinationList.size(); i++) {
                    Object obj = destinationList.get(i);
                    if (obj instanceof Destination) {
                        Destination d = (Destination) obj;
                        dests.append(d.getAirportCode()).append(" (").append(d.getDistance()).append("km)");
                    } else {
                        dests.append("ERROR: ").append(obj.getClass().getSimpleName());
                    }
                    if (i < destinationList.size()) {
                        dests.append(", ");
                    }
                }
            } catch (ListException e) {
                dests.append("Error al listar destinos: ").append(e.getMessage());
            }
        } else {
            dests.append("Ninguno");
        }

        return " \uD83D\uDDFA\uFE0F Route[" +
                "Punto de Origen:=" + originAirportCode +
                "--Destino final: =[" + dests.toString() + "]" +
                ']';
    }
}