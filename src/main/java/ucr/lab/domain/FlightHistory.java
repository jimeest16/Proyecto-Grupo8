package ucr.lab.domain;

import java.util.List;

public class FlightHistory {
    private int flightNumber;
    private List<Integer> passengerIDs;

    public FlightHistory(int flightNumber, List<Integer> passengerIDs) {
        this.flightNumber = flightNumber;
        this.passengerIDs = passengerIDs;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public List<Integer> getPassengerIDs() {
        return passengerIDs;
    }

    public void setPassengerIDs(List<Integer> passengerIDs) {
        this.passengerIDs = passengerIDs;
    }

    @Override
    public String toString() {
        return "FlightHistory{" +
                "flightNumber=" + flightNumber +
                ", passengerIDs=" + passengerIDs +
                '}';
    }
}
