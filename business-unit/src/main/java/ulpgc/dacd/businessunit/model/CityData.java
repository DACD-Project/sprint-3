package ulpgc.dacd.businessunit.model;

import java.util.List;

public class CityData {
    private final String city;
    private WeatherEvent.ForecastEntry weather;
    private List<DestinationEvent.Destination> destinations;
    private double score;

    public CityData(String city) {
        this.city = city;
    }

    public String getCity() { return city; }
    public WeatherEvent.ForecastEntry getWeather() { return weather; }
    public List<DestinationEvent.Destination> getDestinations() { return destinations; }
    public double getScore() { return score; }

    public void setWeather(WeatherEvent.ForecastEntry weather) { this.weather = weather; }
    public void setDestinations(List<DestinationEvent.Destination> destinations) { this.destinations = destinations; }
    public void setScore(double score) { this.score = score; }
}
