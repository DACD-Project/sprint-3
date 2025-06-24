package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;
import java.util.List;

public interface DestinationProvider {
    List<Destination> getDestinations(double lat, double lon);
}