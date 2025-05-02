package ucr.lab.domain;

import java.util.Objects;

public class Weather {
private String weatherDescription;

    public Weather(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weather weather = (Weather) o;
        return weatherDescription != null && weather.weatherDescription != null &&
                weatherDescription.equalsIgnoreCase(weather.weatherDescription);
    }

    @Override
    public int hashCode() {
        return weatherDescription == null ? 0 : weatherDescription.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return weatherDescription;
    }
}
