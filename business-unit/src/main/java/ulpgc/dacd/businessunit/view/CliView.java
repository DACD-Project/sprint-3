package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import ulpgc.dacd.businessunit.model.Destination;
<<<<<<< HEAD
=======

import java.util.Map;
import java.util.stream.Collectors;
>>>>>>> 40f50de282937bfb02d449423a40b8531e5cf4b4

import java.util.Map;
import java.util.stream.Collectors;

public class CliView implements View {
    @Override
    public void display(Map<String, CityData> datamart) {
<<<<<<< HEAD
        System.out.println("\n=== Weather and Destinations Summary ===");
        System.out.printf("%-12s | %-10s | %-12s | %-12s | %-12s | %-50s | %-6s%n",
                "City", "Temp (°C)", "Humidity (%)", "Wind Speed (m/s)", "Precipitation (%)", "Nearby Destinations", "Score");
        System.out.println("-".repeat(110));
=======
        System.out.println("\n=== Resumen del clima y destinos ===");
<<<<<<< HEAD
        System.out.printf("%-12s | %-10s | %-8s | %-10s | %-10s | %-50s | %-6s%n",
                "Ciudad", "Temp (°C)", "Humedad", "Viento (m/s)", "Lluvia (%)", "Destinos Cercanos", "Puntuación");
        System.out.println("-".repeat(110));
=======
        System.out.printf("%-12s | %-10s | %-8s | %-50s | %-6s%n",
                "Ciudad", "Temp (°C)", "Humedad", "Destinos Cercanos", "Puntuación");
        System.out.println("-".repeat(90));
>>>>>>> e092e7441a9e0cee25b613a3665319c118dba247
>>>>>>> 40f50de282937bfb02d449423a40b8531e5cf4b4
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
<<<<<<< HEAD
                    .map(dest -> String.format("%s (%.1f km, %d pop.)",
                            dest.getName(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining("; "))
                    : "N/A";
            System.out.printf("%-12s | %-29s | %-50s | %-6.1f%n",
=======
                    .map(dest -> String.format("%s, %s (%.1f km, %d hab.)",
                            dest.getName(), dest.getCountry(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining("; "))
                    : "N/A";
<<<<<<< HEAD
            System.out.printf("%-12s | %-29s | %-50s | %-6.1f%n",
=======
            System.out.printf("%-12s | %-10s | %-50s | %-6.1f%n",
>>>>>>> e092e7441a9e0cee25b613a3665319c118dba247
>>>>>>> 40f50de282937bfb02d449423a40b8531e5cf4b4
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