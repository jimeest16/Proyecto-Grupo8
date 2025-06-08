package ucr.lab.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Departures {
    private LocalDate departureTime;
    private String origin;
    private String gateId;
    private String status;

    public Departures() {
    }

    //Constructor
    public Departures(LocalDate departureTime, String origin, String gateId, String status) {
        this.departureTime = departureTime;
        this.origin = origin;
        this.gateId = gateId;
        this.status = status;
    }

    public LocalDate getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDate departureTime) {
        this.departureTime = departureTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "Departure Time=" + departureTime +
                ", origin='" + origin + '\'' +
                ", Gate='" + gateId + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }
}
