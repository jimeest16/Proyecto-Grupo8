package ucr.lab.domain;

import java.util.Objects;

public class Climate {
    private Place place;
    private Weather weather;

    public Climate(Place place, Weather weather) {
        this.place = place;
        this.weather = weather;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Climate { Place: " + place + " - Weather: " + weather +'}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Climate climate = (Climate) o;
        return Objects.equals(place, climate.place) && Objects.equals(weather, climate.weather);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, weather);
    }
}
