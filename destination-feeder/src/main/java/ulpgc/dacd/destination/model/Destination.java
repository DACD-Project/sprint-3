package ulpgc.dacd.destination.model;

public class Destination {
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
