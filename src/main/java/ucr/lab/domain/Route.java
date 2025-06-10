package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ucr.lab.domain.Destination;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    @JsonProperty("origin_airport")
    private int originAirportCode;

    @JsonProperty("destinationList")  
    private List<Destination> destinationList = new ArrayList<>();


    // Constructor, getters y setters

    public Route() {
    }

    public Route(int originAirportCode, List<Destination> destinationList) {
        this.originAirportCode = originAirportCode;
        this.destinationList = destinationList != null ? destinationList : new ArrayList<>();
    }

    public int getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(int originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    public List<Destination> getDestinationList() {
        return destinationList;
    }

    public void setDestinationList(List<Destination> destinationList) {
        this.destinationList = destinationList;
    }

    @Override
    public String toString() {
        return "Route{" +
                "originAirportCode=" + originAirportCode +
                ", destinationList=" + destinationList +
                '}';
    }
}
