package ulpgc.dacd.destination.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ulpgc.dacd.destination.model.Destination;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class GeoDBCitiesProvider implements DestinationProvider {
    private static final Logger logger = Logger.getLogger(GeoDBCitiesProvider.class.getName());
    private final String apiKey;
    private final String apiHost;
    private final String apiUrl;

    public GeoDBCitiesProvider(String apiKey, String apiHost, String apiUrl) {
        this.apiKey = apiKey;
        this.apiHost = apiHost;
        this.apiUrl = apiUrl;
    }

    @Override
    public List<Destination> getDestinations(double latitude, double longitude, String cityName) {
        try {
            String latStr = String.format(Locale.US, "%+07.4f", latitude);
            String lonStr = String.format(Locale.US, "%+08.4f", longitude);
            String location = latStr + lonStr;
            String url = String.format(apiUrl, location) + "?limit=5&minPopulation=50000&radius=100";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", apiHost)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status code: " + response.statusCode());
            logger.info("API response: " + response.body());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray data = jsonObject.getAsJsonArray("data");

            if (data == null || data.isEmpty()) {
                logger.warning("The 'data' field is null or empty for " + cityName + ". Full JSON: " + response.body());
                return new ArrayList<>();
            }

            List<Destination> destinations = new ArrayList<>();
            for (var element : data) {
                JsonObject cityData = element.getAsJsonObject();
                destinations.add(new Destination(
                        cityData.get("name").getAsString(),
                        cityData.get("country").getAsString(),
                        cityData.get("latitude").getAsDouble(),
                        cityData.get("longitude").getAsDouble(),
                        cityData.get("population").getAsInt(),
                        cityData.get("distance").getAsDouble()
                ));
            }

            return destinations;
        } catch (IOException | InterruptedException e) {
            logger.severe("Error fetching destinations for " + cityName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}