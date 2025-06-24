package ulpgc.dacd.weather;

import ulpgc.dacd.weather.control.WeatherCoordinator;

public class Main {
    public static void main(String[] args) {
        if (args.length < 7) {
            System.err.println("usage: java -jar weather-feeder.jar <api_key> <db_path> <lat> <lon> <broker_url> <topic_name> <source_id>");
            return;
        }

        new WeatherCoordinator().run(args);
    }
}
