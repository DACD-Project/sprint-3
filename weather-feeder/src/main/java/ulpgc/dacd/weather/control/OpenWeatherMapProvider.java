package ulpgc.dacd.weather.control;

import ulpgc.dacd.weather.model.Weather;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapProvider implements WeatherProvider {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiKey;
    private final String apiUrl;

    public OpenWeatherMapProvider(String apiKey, String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    public Weather getWeather(double lat, double lon) {
        try {
            String url = apiUrl + "?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());
            System.out.println("API response: " + response.body());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray forecastList = json.getAsJsonArray("list");

            if (forecastList == null) {
                System.err.println("The 'list' field is null. Full JSON: " + json);
                return null;
            }

            List<Weather.ForecastEntry> forecast = new ArrayList<>();
            for (int i = 0; i < forecastList.size(); i++) {
                JsonObject entry = forecastList.get(i).getAsJsonObject();
                double temp = entry.getAsJsonObject("main").get("temp").getAsDouble();
                int humidity = entry.getAsJsonObject("main").get("humidity").getAsInt();
                long timestamp = entry.get("dt").getAsLong();
                String dateTime = entry.get("dt_txt").getAsString();
                forecast.add(new Weather.ForecastEntry(temp, humidity, timestamp, dateTime));
            }
            return new Weather(forecast);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}