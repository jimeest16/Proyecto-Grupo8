package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ucr.lab.TDA.list.SinglyLinkedList;
import ucr.lab.utility.Reader.SinglyReader;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {


    @JsonProperty("origin_airport")
    private int originAirportCode;


    @JsonDeserialize(using = SinglyReader.class)
    @JsonProperty("destinationList")
    private SinglyLinkedList destinationList;


    public Route() {
        this.destinationList = new SinglyLinkedList();
    }


    public Route(int originAirportCode, SinglyLinkedList destinationList) {
        this.originAirportCode = originAirportCode;
        this.destinationList = destinationList != null ? destinationList : new SinglyLinkedList();
    }



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

        return " \uD83D\uDDFA\uFE0F Route[" +
                "Punto de Origen:=" + originAirportCode +
                "--Destino final: =" + (destinationList != null ? destinationList.toString() : "null") +
                ']';
    }
}