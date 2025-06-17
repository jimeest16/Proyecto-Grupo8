
package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Destination {
    @JsonProperty("airport_code")
    private int airportCode;

    @JsonProperty("distance")
    private double distance;

    public Destination() {

    }

    public Destination(int airportCode, double distance) {
        this.airportCode = airportCode;
        this.distance = distance;
    }

    // Getters y Setters
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