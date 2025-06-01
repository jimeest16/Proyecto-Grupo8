package ucr.lab.domain;

import java.util.List;

public class PassengerToJson {
    public int id;
    public String name;
    public String nationality;
    public List<String> flightHistory;

    public PassengerToJson(int id, String name, String nationality, List<String> flightHistory) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.flightHistory = flightHistory;
    }
}
