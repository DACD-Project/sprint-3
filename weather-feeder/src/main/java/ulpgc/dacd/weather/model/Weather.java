package ulpgc.dacd.weather.model;

import java.util.List;

public class Weather {
    private List<ForecastEntry> forecast;

    public Weather(List<ForecastEntry> forecast) {
        this.forecast = forecast;
    }

    public List<ForecastEntry> getForecast() {
        return forecast;
    }

    public static class ForecastEntry {
        private double temperature;
        private int humidity;
        private long timestamp;
        private String dateTime;

        public ForecastEntry(double temperature, int humidity, long timestamp, String dateTime) {
            this.temperature = temperature;
            this.humidity = humidity;
            this.timestamp = timestamp;
            this.dateTime = dateTime;
        }

        public double getTemperature() { return temperature; }
        public int getHumidity() { return humidity; }
        public long getTimestamp() { return timestamp; }
        public String getDateTime() { return dateTime; }
    }
}
