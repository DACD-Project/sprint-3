package ulpgc.dacd.weather;

import ulpgc.dacd.weather.control.ActiveMQWeatherPublisher;
import ulpgc.dacd.weather.control.OpenWeatherMapProvider;
import ulpgc.dacd.weather.control.WeatherCoordinator;
import ulpgc.dacd.weather.control.WeatherPublisher;

public class Main {
    public static void main(String[] args) {
        if (args.length != 7) {
            System.err.println("Usage: <api_key> <db_file> <latitude> <longitude> <broker_url> <weather_topic> <client_id>");
            System.exit(1);
        }

        String apiKey = args[0];
        String apiUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=metric";
        String brokerUrl = args[4];
        String weatherTopic = args[5];
        String clientId = args[6];

        OpenWeatherMapProvider provider = new OpenWeatherMapProvider(apiKey, apiUrl);
        WeatherPublisher publisher = new ActiveMQWeatherPublisher(brokerUrl, weatherTopic, clientId);
        WeatherCoordinator coordinator = new WeatherCoordinator(provider, publisher);
        coordinator.run();
    }
}