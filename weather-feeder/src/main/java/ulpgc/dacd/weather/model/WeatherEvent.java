package ulpgc.dacd.weather.model;

import java.time.Instant;
import java.util.List;

public class WeatherEvent {
    public Instant ts;
    public String ss;
    public String city;
    public List<Weather.ForecastEntry> forecast;

    public WeatherEvent(String ss, String city, List<Weather.ForecastEntry> forecast) {
        this.ts = Instant.now();
        this.ss = ss;
        this.city = city;
        this.forecast = forecast;
    }
}