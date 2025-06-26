package ulpgc.dacd.businessunit.control;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4 || (!args[3].equalsIgnoreCase("cli") && !args[3].equalsIgnoreCase("gui"))) {
            System.err.println("Usage: <broker_url> <weather_topic> <destination_topic> <interface: cli|gui>");
            System.exit(1);
        }

        BusinessUnitCoordinator coordinator = new BusinessUnitCoordinator(args[3]);
        coordinator.run(args);
    }
}