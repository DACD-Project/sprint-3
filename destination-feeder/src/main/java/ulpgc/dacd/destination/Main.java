package ulpgc.dacd.destination;

import ulpgc.dacd.destination.control.DestinationCoordinator;

public class Main {
    public static void main(String[] args) {
        if (args.length < 7) {
            System.err.println("usage: java -jar destination-feeder.jar <api_key> <db_path> <lat> <lon> <broker_url> <topic_name> <source_id>");
            return;
        }
        new DestinationCoordinator().run(args);
    }
}
