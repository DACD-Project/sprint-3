package ulpgc.dacd.businessunit.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ulpgc.dacd.businessunit.model.CityData;
import ulpgc.dacd.businessunit.model.DestinationEvent;
import ulpgc.dacd.businessunit.model.WeatherEvent;
import ulpgc.dacd.businessunit.view.CliView;
import ulpgc.dacd.businessunit.view.GuiView;
import ulpgc.dacd.businessunit.view.View;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BusinessUnitCoordinator {
    private static final Logger logger = Logger.getLogger(BusinessUnitCoordinator.class.getName());
    private final Map<String, CityData> datamart = new HashMap<>();
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
    private final View view;

    public BusinessUnitCoordinator(String interfaceType) {
        this.view = interfaceType.equalsIgnoreCase("gui") ? new GuiView() : new CliView();
    }

    public void run(String[] args) {
        String brokerUrl = args[0];
        String weatherTopic = args[1];
        String destinationTopic = args[2];

        subscribeToTopic(brokerUrl, weatherTopic, "business-unit-weather");
        subscribeToTopic(brokerUrl, destinationTopic, "business-unit-destination");

        new Thread(() -> {
            while (true) {
                view.display(datamart);
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    logger.severe("Display interrupted: " + e.getMessage());
                }
            }
        }).start();
    }

    private void subscribeToTopic(String brokerUrl, String topicName, String clientId) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            Connection connection = factory.createConnection();
            connection.setClientID(clientId);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);
            MessageConsumer consumer = session.createDurableSubscriber(topic, clientId + "-sub");
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        String json = ((TextMessage) message).getText();
                        processEvent(json, topicName);
                    }
                } catch (JMSException e) {
                    logger.severe("Error processing message: " + e.getMessage());
                }
            });
        } catch (JMSException e) {
            logger.severe("Error subscribing to topic " + topicName + ": " + e.getMessage());
        }
    }

    private void processEvent(String json, String topicName) {
        if (topicName.contains("weather")) {
            WeatherEvent event = gson.fromJson(json, WeatherEvent.class);
            CityData cityData = datamart.computeIfAbsent(event.city, k -> new CityData(event.city));
            cityData.setWeather(event.forecast.get(0));
            cityData.setScore(calculateScore(event.forecast.get(0)));
        } else if (topicName.contains("destination")) {
            DestinationEvent event = gson.fromJson(json, DestinationEvent.class);
            CityData cityData = datamart.computeIfAbsent(event.city, k -> new CityData(event.city));
            cityData.setDestinations(event.destinations);
        }
        logger.info("Processed event for " + topicName + ": " + json);
    }

    private double calculateScore(WeatherEvent.ForecastEntry forecast) {
        double temp = forecast.getTemperature();
        int humidity = forecast.getHumidity();
        double windSpeed = forecast.getWindSpeed();
        double pop = forecast.getPop();
        double tempScore = Math.max(0, 100 - Math.abs(temp - 23) * 5);
        double humidityScore = Math.max(0, 100 - Math.abs(humidity - 40) * 2);
        double windScore = Math.max(0, 100 - Math.abs(windSpeed - 2) * 10);
        double popScore = Math.max(0, 100 - pop * 100);
        return (tempScore + humidityScore + windScore + popScore) / 4;
    }
}