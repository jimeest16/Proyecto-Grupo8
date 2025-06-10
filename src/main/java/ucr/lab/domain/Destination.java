package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Destination {

    @JsonProperty("airport_code")
    private int airportCode;

    @JsonProperty("distance")
    private double distance;

    // Constructor, getters y setters

    public Destination() {
    }

    public Destination(int airportCode, double distance) {
        this.airportCode = airportCode;
        this.distance = distance;
    }

    public int getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(int airportCode) {
        this.airportCode = airportCode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "airportCode=" + airportCode +
                ", distance=" + distance +
                '}';
    }
}
