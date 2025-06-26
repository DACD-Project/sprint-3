package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import ulpgc.dacd.businessunit.model.Destination;

import java.util.Map;
import java.util.stream.Collectors;

public class CliView implements View {
    @Override
    public void display(Map<String, CityData> datamart) {
        System.out.println("\n=== Weather and Destinations Summary ===");
        System.out.printf("%-12s | %-10s | %-12s | %-12s | %-12s | %-50s | %-6s%n",
                "City", "Temp (°C)", "Humidity (%)", "Wind Speed (m/s)", "Precipitation (%)", "Nearby Destinations", "Score");
        System.out.println("-".repeat(110));
        for (CityData data : datamart.values()) {
            String weatherInfo = data.getWeather() != null
                    ? String.format("%.1f | %d | %.1f | %.0f",
                    data.getWeather().getTemperature(),
                    data.getWeather().getHumidity(),
                    data.getWeather().getWindSpeed(),
                    data.getWeather().getPop() * 100)
                    : "N/A | N/A | N/A | N/A";
            String destinationInfo = data.getDestinations() != null && !data.getDestinations().isEmpty()
                    ? data.getDestinations().stream()
                    .map(dest -> String.format("%s (%.1f km, %d pop.)",
                            dest.getName(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining("; "))
                    : "N/A";
            System.out.printf("%-12s | %-29s | %-50s | %-6.1f%n",
                    data.getCity(), weatherInfo, destinationInfo, data.getScore());
        }
        System.out.println("Recommendation: " + getRecommendation(datamart));
    }

    private String getRecommendation(Map<String, CityData> datamart) {
        CityData bestCity = datamart.values().stream()
                .max((a, b) -> Double.compare(a.getScore(), b.getScore()))
                .orElse(null);
        return bestCity != null
                ? bestCity.getCity() + " (Score: " + String.format("%.1f", bestCity.getScore()) + ")"
                : "No sufficient data available";
    }
}