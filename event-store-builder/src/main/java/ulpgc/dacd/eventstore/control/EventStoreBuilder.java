package ulpgc.dacd.eventstore.control;

import java.util.logging.Logger;

public class EventStoreBuilder {
    private static final Logger logger = Logger.getLogger(EventStoreBuilder.class.getName());

    public static void main(String[] args) {
        if (args.length < 2) {
            logger.warning("Usage: java EventStoreBuilder <broker_url> <topic1,topic2,...>");
            return;
        }

        String brokerUrl = args[0];
        String[] topics = args[1].split(",");

        for (String topic : topics) {
            String trimmedTopic = topic.trim();
            String clientId = "eventstore-" + trimmedTopic;
            String subscriptionName = "subscription-" + trimmedTopic;

            EventProcessor processor = new EventProcessor(brokerUrl, trimmedTopic, clientId, subscriptionName);
            new Thread(processor).start();
        }
    }
}
