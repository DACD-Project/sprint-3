package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather;
import ulpgc.dacd.weather.model.WeatherEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherCoordinator {
    public void run(String[] args) {
        String apiKey = args[0];
        String dbPath = "jdbc:sqlite:" + args[1];
        String brokerUrl = args[4];
        String topicName = args[5];
        String sourceId = args[6];

        String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
        WeatherProvider provider = new OpenWeatherMapProvider(apiKey, apiUrl);
        WeatherStore store = new SqliteWeatherStore(dbPath);
        WeatherPublisher publisher = new WeatherPublisher(brokerUrl, topicName, sourceId);

        // Lista de ciudades con coordenadas
        List<City> cities = List.of(
                new City("Madrid", 40.4168, -3.7038),
                new City("Barcelona", 41.3851, 2.1734),
                new City("Sevilla", 37.3891, -5.9845),
                new City("Valencia", 39.4699, -0.3763),
                new City("Zaragoza", 41.6488, -0.8891)
        );

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for (City city : cities) {
                Weather weather = provider.getWeather(city.latitude, city.longitude);
                if (weather != null) {
                    weather.getForecast().forEach(store::storeWeather);
                    publisher.publish(new WeatherEvent(sourceId, city.name, weather.getForecast()));
                }
            }
        }, 0, 6, TimeUnit.HOURS);
    }

    private static class City {
        String name;
        double latitude;
        double longitude;
        City(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
