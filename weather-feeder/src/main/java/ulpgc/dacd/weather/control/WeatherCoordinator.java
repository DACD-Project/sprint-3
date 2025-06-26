package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.City;
import ulpgc.dacd.weather.model.WeatherEvent;

import java.util.List;
import java.util.logging.Logger;

public class WeatherCoordinator {
    private static final Logger logger = Logger.getLogger(WeatherCoordinator.class.getName());
    private final WeatherProvider provider;
    private final WeatherPublisher publisher;
    private final List<City> cities;

    public WeatherCoordinator(WeatherProvider provider, WeatherPublisher publisher) {
        this.provider = provider;
        this.publisher = publisher;
        this.cities = List.of(
                new City("Madrid", 40.4168, -3.7038),
                new City("Barcelona", 41.3851, 2.1734),
                new City("Sevilla", 37.3891, -5.9845),
                new City("Valencia", 39.4699, -0.3763),
                new City("Zaragoza", 41.6488, -0.8891)
        );
    }

    public void run() {
        while (true) {
            for (City city : cities) {
                logger.info("Fetching weather for " + city.getName());
                WeatherEvent event = provider.getWeather(city.getLatitude(), city.getLongitude(), city.getName());
                if (event != null) {
                    publisher.publish(event);
                } else {
                    logger.warning("No weather data for " + city.getName());
                }
                try {
                    Thread.sleep(1000); // Evitar superar límites de la API
                } catch (InterruptedException e) {
                    logger.severe("Interrupted while sleeping: " + e.getMessage());
                }
            }
            try {
                Thread.sleep(6 * 60 * 60 * 1000); // Ejecutar cada 6 horas
            } catch (InterruptedException e) {
                logger.severe("Interrupted while waiting for next execution: " + e.getMessage());
            }
        }
    }
}