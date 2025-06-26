package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;
import ulpgc.dacd.destination.model.DestinationEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DestinationCoordinator {
    private static final Logger logger = Logger.getLogger(DestinationCoordinator.class.getName());
    private final ScheduledExecutorService scheduler;

    public DestinationCoordinator() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void run(String[] args) {
        String apiKey = args[0];
        String dbPath = "jdbc:sqlite:" + args[1];
        String brokerUrl = args[4];
        String topic = args[5];
        String sourceId = args[6];

        String apiHost = "wft-geo-db.p.rapidapi.com";
        String apiUrl = "https://wft-geo-db.p.rapidapi.com/v1/geo/locations/%s/nearbyCities";

        DestinationProvider provider = new GeoDBCitiesProvider(apiKey, apiHost, apiUrl);
        DestinationStore store = new SqliteDestinationStore(dbPath);
        DestinationPublisher publisher = new DestinationPublisher(brokerUrl, topic, sourceId);

        List<City> cities = List.of(
                new City("Madrid", 40.4168, -3.7038),
                new City("Barcelona", 41.3851, 2.1734),
                new City("Sevilla", 37.3891, -5.9845),
                new City("Valencia", 39.4699, -0.3763),
                new City("Zaragoza", 41.6488, -0.8891)
        );

        scheduler.scheduleAtFixedRate(() -> {
            for (City city : cities) {
                try {
                    List<Destination> destinations = provider.getDestinations(city.latitude, city.longitude, city.name);
                    if (!destinations.isEmpty()) {
                        destinations.forEach(destination -> store.storeDestination(city.name, destination));
                        publisher.publish(new DestinationEvent(sourceId, city.name, destinations));
                    }
                    Thread.sleep(1000); // Retardo para respetar el límite de la API
                } catch (InterruptedException e) {
                    logger.severe("Interrupted while waiting between API requests: " + e.getMessage());
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    logger.severe("Error processing city " + city.name + ": " + e.getMessage());
                }
            }
        }, 0, 6, TimeUnit.HOURS);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private static class City {
        String name;
        double latitude;
        double longitude;

        City(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}