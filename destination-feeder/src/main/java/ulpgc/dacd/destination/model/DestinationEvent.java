package ulpgc.dacd.destination.model;

import java.time.Instant;
import java.util.List;

public class DestinationEvent {
    public Instant ts;
    public String ss;
    public String city;
    public List<Destination> destinations;

    public DestinationEvent(String ss, String city, List<Destination> destinations) {
        this.ts = Instant.now();
        this.ss = ss;
        this.city = city;
        this.destinations = destinations;
    }
}
