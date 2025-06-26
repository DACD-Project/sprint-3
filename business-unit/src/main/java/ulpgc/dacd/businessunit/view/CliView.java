package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import ulpgc.dacd.businessunit.model.Destination;

import java.util.Map;
import java.util.stream.Collectors;

public class CliView {
    public void display(Map<String, CityData> datamart) {
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
                    .map(dest -> String.format("%s, %s (%.1f km, %d hab.)",
                            dest.getName(), dest.getCountry(), dest.getDistance(), dest.getPopulation()))
                    .collect(Collectors.joining("; "))
                    : "N/A";
<<<<<<< HEAD
            System.out.printf("%-12s | %-29s | %-50s | %-6.1f%n",
=======
            System.out.printf("%-12s | %-10s | %-50s | %-6.1f%n",
>>>>>>> e092e7441a9e0cee25b613a3665319c118dba247
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