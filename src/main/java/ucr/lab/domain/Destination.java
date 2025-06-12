package ucr.lab.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Destination {
    @JsonProperty("airport_code") // Ensure this matches your JSON exactly
    private int airportCode;

    @JsonProperty("distance") // Ensure this matches your JSON exactly
    private double distance;

    // Default constructor for Jackson
    public Destination() {
    }

    public Destination(int airportCode, double distance) {
        this.airportCode = airportCode;
        this.distance = distance;
    }

    // Getters and Setters
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