package ucr.lab.domain;

public class Destination {
    private int airportCode;
    private double distance;

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
        return "Destino: " + airportCode + " (" + distance + " km)";
    }
}
