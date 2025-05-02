package ucr.lab.domain;

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
    public String toString() {
        return weatherDescription;
    }
}
