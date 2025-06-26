package ulpgc.dacd.weather.model;

import java.time.Instant;
import java.util.List;

public class WeatherEvent {
    public Instant ts;
    public String ss;
    public String city;
    public List<ForecastEntry> forecast;

    public WeatherEvent(String ss, String city, List<ForecastEntry> forecast) {
        this.ts = Instant.now();
        this.ss = ss;
        this.city = city;
        this.forecast = forecast;
    }

    public static class ForecastEntry {
        private final double temperature;
        private final int humidity;
        private final double windSpeed;
        private final double pop;
        private final long timestamp;
        private final String dateTime;

        public ForecastEntry(double temperature, int humidity, double windSpeed, double pop, long timestamp, String dateTime) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.pop = pop;
            this.timestamp = timestamp;
            this.dateTime = dateTime;
        }

        public double getTemperature() { return temperature; }
        public int getHumidity() { return humidity; }
        public double getWindSpeed() { return windSpeed; }
        public double getPop() { return pop; }
        public long getTimestamp() { return timestamp; }
        public String getDateTime() { return dateTime; }
    }
}