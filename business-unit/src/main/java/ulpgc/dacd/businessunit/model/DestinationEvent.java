package ulpgc.dacd.businessunit.model;

import java.time.Instant;
import java.util.List;

public class DestinationEvent {
    public Instant ts;
    public String ss;
    public String city;
    public List<Destination> destinations;

    public static class Destination {
        private final String name;
        private final String country;
        private final double latitude;
        private final double longitude;
        private final int population;
        private final double distance;

        public Destination(String name, String country, double latitude, double longitude, int population, double distance) {
            this.name = name;
            this.country = country;
            this.latitude = latitude;
            this.longitude = longitude;
            this.population = population;
            this.distance = distance;
        }

        public String getName() { return name; }
        public String getCountry() { return country; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public int getPopulation() { return population; }
        public double getDistance() { return distance; }
    }
}
