package ulpgc.dacd.eventstore.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.logging.Logger;

public class EventProcessor implements Runnable {
    private final String brokerUrl;
    private final String topic;
    private final String clientId;
    private final String subscriptionName;
    private static final Logger logger = Logger.getLogger(EventProcessor.class.getName());

    public EventProcessor(String brokerUrl, String topic, String clientId, String subscriptionName) {
        this.brokerUrl = brokerUrl;
        this.topic = topic;
        this.clientId = clientId;
        this.subscriptionName = subscriptionName;
    }

    @Override
    public void run() {
        try {
            EventSubscriber subscriber = new EventSubscriber(brokerUrl, topic, clientId, subscriptionName);
            logger.info("Subscribed to topic: " + topic);

            while (true) {
                Message message = subscriber.receive();
                if (message instanceof TextMessage) {
                    String jsonString = ((TextMessage) message).getText();
                    JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
                    EventWriter.saveEvent(topic, json);
                }
            }
        } catch (Exception e) {
            logger.severe("Subscription error for topic " + topic + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
