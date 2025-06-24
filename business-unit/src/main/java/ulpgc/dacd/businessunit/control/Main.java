package ulpgc.dacd.businessunit.control;

import ulpgc.dacd.businessunit.control.BusinessUnitCoordinator;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("usage: java -jar business-unit.jar <broker_url> <weather_topic> <destination_topic>");
            return;
        }
        new BusinessUnitCoordinator().run(args);
    }
}

