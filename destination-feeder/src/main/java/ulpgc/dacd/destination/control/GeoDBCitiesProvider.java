package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeoDBCitiesProvider implements DestinationProvider {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiKey;
    private final String apiHost;
    private final String apiUrl;

    public GeoDBCitiesProvider(String apiKey, String apiHost, String apiUrl) {
        this.apiKey = apiKey;
        this.apiHost = apiHost;
        this.apiUrl = apiUrl;
    }

    @Override
    public List<Destination> getDestinations(double lat, double lon) {
        try {
            String latStr = (lat >= 0 ? "+" : "") + String.format(Locale.US, "%.4f", lat);
            String lonStr = (lon >= 0 ? "+" : "") + String.format(Locale.US, "%.4f", lon);
            String fullLocation = latStr + lonStr;
            String url = String.format(apiUrl, fullLocation);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", apiHost)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());
            System.out.println("API response: " + response.body());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray data = json.getAsJsonArray("data");

            if (data == null) {
                System.err.println("The 'data' field is null. Full JSON: " + json);
                return List.of();
            }

            List<Destination> destinations = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                JsonObject obj = data.get(i).getAsJsonObject();
                String name = obj.get("city").getAsString();
                String country = obj.get("country").getAsString();
                double latitude = obj.get("latitude").getAsDouble();
                double longitude = obj.get("longitude").getAsDouble();
                int population = obj.get("population").getAsInt();
                double distance = obj.get("distance").getAsDouble();
                destinations.add(new Destination(name, country, latitude, longitude, population, distance));
            }
            return destinations;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}