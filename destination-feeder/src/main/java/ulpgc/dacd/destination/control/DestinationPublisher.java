package ulpgc.dacd.destination.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import ulpgc.dacd.destination.model.DestinationEvent;

import javax.jms.*;
import java.io.IOException;
import java.time.Instant;

public class DestinationPublisher {
    private final String brokerUrl;
    private final String topicName;
    private final String sourceId;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new TypeAdapter<Instant>() {
                @Override
                public void write(JsonWriter out, Instant value) throws IOException {
                    out.value(value.toString());
                }

                @Override
                public Instant read(JsonReader in) throws IOException {
                    return Instant.parse(in.nextString());
                }
            }).create();

    public DestinationPublisher(String brokerUrl, String topicName, String sourceId) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        this.sourceId = sourceId;
        setupConnection();
    }

    private void setupConnection() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination topic = session.createTopic(topicName);
            producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(DestinationEvent event) {
        try {
            String json = gson.toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);
            System.out.println("Published DestinationEvent: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
