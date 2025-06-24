package ulpgc.dacd.eventstore.control;

import javax.jms.*;

public class EventSubscriber {
    private final Connection connection;
    private final Session session;
    private final MessageConsumer consumer;

    public EventSubscriber(String brokerUrl, String topicName, String clientId, String subscriptionName) throws JMSException {
        ConnectionFactory factory = new org.apache.activemq.ActiveMQConnectionFactory(brokerUrl);
        this.connection = factory.createConnection();
        this.connection.setClientID(clientId);
        this.connection.start();

        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);
        this.consumer = session.createDurableSubscriber(topic, subscriptionName);
    }

    public Message receive() throws JMSException {
        return consumer.receive();
    }
}
