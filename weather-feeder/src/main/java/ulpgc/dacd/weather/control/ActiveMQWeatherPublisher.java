package ulpgc.dacd.weather.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ulpgc.dacd.weather.model.WeatherEvent;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

public class ActiveMQWeatherPublisher implements WeatherPublisher {
    private static final Logger logger = Logger.getLogger(ActiveMQWeatherPublisher.class.getName());
    private final String brokerUrl;
    private final String topicName;
    private final String clientId;
    private final Gson gson;

    public ActiveMQWeatherPublisher(String brokerUrl, String topicName, String clientId) {
        this.brokerUrl = brokerUrl;
        this.topicName = topicName;
        this.clientId = clientId;
        this.gson = new GsonBuilder()
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
    }

    @Override
    public void publish(WeatherEvent event) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = factory.createConnection();
            connection.setClientID(clientId);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageProducer producer = session.createProducer(topic);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            String json = gson.toJson(event);
            TextMessage message = session.createTextMessage(json);
            producer.send(message);

            logger.info("Published WeatherEvent for " + event.city + ": " + json);

            session.close();
            connection.close();
        } catch (JMSException e) {
            logger.severe("Error publishing WeatherEvent for " + event.city + ": " + e.getMessage());
        }
    }
}