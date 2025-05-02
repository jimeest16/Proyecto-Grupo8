package ucr.lab.domain;

import java.util.Objects;

public class Place {
    private String name;

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return name != null && place.name != null && name.equalsIgnoreCase(place.name);
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return name  ;
    }
}
