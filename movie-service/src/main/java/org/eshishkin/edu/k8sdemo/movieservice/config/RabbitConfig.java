package org.eshishkin.edu.k8sdemo.movieservice.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class RabbitConfig {

    @ConfigProperty(name = "quarkus.rabbitmq.address")
    String url;

    @ConfigProperty(name = "quarkus.rabbitmq.username")
    String user;

    @ConfigProperty(name = "quarkus.rabbitmq.password")
    String password;

    @ConfigProperty(name = "application.messaging.exchange")
    String exchange;

    @ConfigProperty(name = "application.messaging.queue")
    String queue;

    @ConfigProperty(name = "application.messaging.routing-key")
    String routingKey;

    private Connection connection;
    private Channel channel;

    @PostConstruct
    void init() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = createConnectionFactory();
        connection = connectionFactory.newConnection();
        channel = createChannel(connection);
    }


    @SneakyThrows
    private Channel createChannel(Connection connection) {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true);
        channel.queueBind(queue, exchange, routingKey);

        return channel;
    }

    @SneakyThrows
    private ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(url);
        factory.setUsername(user);
        factory.setPassword(password);
        return factory;
    }

    @PreDestroy
    void destroy() {
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
