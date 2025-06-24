package ulpgc.dacd.businessunit.view;

import ulpgc.dacd.businessunit.model.CityData;
import java.util.Map;

public class CliView {
    public void display(Map<String, CityData> datamart) {
        System.out.println("\n=== Resumen del clima y destinos ===");
        System.out.printf("%-12s | %-10s | %-8s | %-20s | %-6s%n",
                "Ciudad", "Temp (°C)", "Humedad", "Destino Cercano", "Puntuación");
        System.out.println("-".repeat(60));
        for (CityData data : datamart.values()) {
            String weatherInfo = data.getWeather() != null
                    ? String.format("%.1f", data.getWeather().getTemperature()) + " | " + data.getWeather().getHumidity()
                    : "N/A | N/A";
            String destinationInfo = data.getDestinations() != null && !data.getDestinations().isEmpty()
                    ? data.getDestinations().get(0).getName() + ", " + data.getDestinations().get(0).getCountry()
                    : "N/A";
            System.out.printf("%-12s | %-10s | %-20s | %-6.1f%n",
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
