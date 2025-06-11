package ucr.lab.domain;

public class Passenger {
    private int id;
    private String name;
    private String nationality;

    public Passenger() {
    }

    public Passenger(int id, String name, String nationality) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
    }

    public Passenger(int id) {
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return "\uD83D\uDC64  ID: " + id + ", Nombre: " + name + ", Nacionalidad: " + nationality;
    }
}
