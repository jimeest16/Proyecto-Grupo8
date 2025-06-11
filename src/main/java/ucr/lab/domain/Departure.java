package ucr.lab.domain;

public class Departure {
    private int flightNumber;
    private String status;

    public Departure(int flightNumber, String status) {
        this.flightNumber = flightNumber;
        this.status = status;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Departure{" +
                "flightNumber=" + flightNumber +
                ", status='" + status + '\'' +
                '}';
    }
}
