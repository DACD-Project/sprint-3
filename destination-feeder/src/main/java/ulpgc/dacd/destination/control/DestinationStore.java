package ulpgc.dacd.destination.control;

import ulpgc.dacd.destination.model.Destination;

public interface DestinationStore {
    void storeDestination(String name, Destination destination);
}