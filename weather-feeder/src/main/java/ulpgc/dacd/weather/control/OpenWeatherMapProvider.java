package ulpgc.dacd.weather.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ulpgc.dacd.weather.model.WeatherEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OpenWeatherMapProvider implements WeatherProvider {
    private static final Logger logger = Logger.getLogger(OpenWeatherMapProvider.class.getName());
    private final String apiKey;
    private final String apiUrl;

    public OpenWeatherMapProvider(String apiKey, String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    public WeatherEvent getWeather(double latitude, double longitude, String cityName) {
        try {
            String url = String.format(apiUrl, latitude, longitude, apiKey);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status code for " + cityName + ": " + response.statusCode());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            if (!jsonObject.get("cod").getAsString().equals("200")) {
                logger.warning("Error in API response for " + cityName + ": " + jsonObject.get("message").getAsString());
                return null;
            }

            JsonArray forecastList = jsonObject.getAsJsonArray("list");
            List<WeatherEvent.ForecastEntry> forecast = new ArrayList<>();
            for (var element : forecastList) {
                JsonObject forecastData = element.getAsJsonObject();
                JsonObject main = forecastData.getAsJsonObject("main");
                JsonObject wind = forecastData.getAsJsonObject("wind");
                double temperature = main.get("temp").getAsDouble();
                int humidity = main.get("humidity").getAsInt();
                double windSpeed = wind.get("speed").getAsDouble();
                double pop = forecastData.get("pop").getAsDouble();
                long timestamp = forecastData.get("dt").getAsLong();
                String dateTime = forecastData.get("dt_txt").getAsString();
                forecast.add(new WeatherEvent.ForecastEntry(temperature, humidity, windSpeed, pop, timestamp, dateTime));
            }

            return new WeatherEvent("weather-feeder", cityName, forecast);
        } catch (IOException | InterruptedException e) {
            logger.severe("Error fetching weather for " + cityName + ": " + e.getMessage());
            return null;
        }
    }
}