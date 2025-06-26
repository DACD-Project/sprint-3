package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import ulpgc.dacd.businessunit.model.Destination;

import java.util.Map;
import java.util.stream.Collectors;

public class CliView {
    public void display(Map<String, CityData> datamart) {
        System.out.println("\n=== Resumen del clima y destinos ===");
        System.out.printf("%-12s | %-10s | %-8s | %-50s | %-6s%n",
                "Ciudad", "Temp (°C)", "Humedad", "Destinos Cercanos", "Puntuación");
        System.out.println("-".repeat(90));
        for (CityData data : datamart.values()) {
            String weatherInfo = data.getWeather() != null
                    ? String.format("%.1f", data.getWeather().getTemperature()) + " | " + data.getWeather().getHumidity()
                    : "N/A | N/A";
            String destinationInfo = data.getDestinations() != null && !data.getDestinations().isEmpty()
                    ? data.getDestinations().stream()
                    .map(dest -> String.format("%s, %s (%.1f km, %d hab.)",
                            dest.getName(), dest.getCountry(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining("; "))
                    : "N/A";
            System.out.printf("%-12s | %-10s | %-50s | %-6.1f%n",
                    data.getCity(), weatherInfo, destinationInfo, data.getScore());
        }
        System.out.println("Recomendación: " + getRecommendation(datamart));
    }

    private String getRecommendation(Map<String, CityData> datamart) {
        CityData bestCity = datamart.values().stream()
                .max((a, b) -> Double.compare(a.getScore(), b.getScore()))
                .orElse(null);
        return bestCity != null
                ? bestCity.getCity() + " (Puntuación: " + String.format("%.1f", bestCity.getScore()) + ")"
                : "No hay datos suficientes";
    }
}