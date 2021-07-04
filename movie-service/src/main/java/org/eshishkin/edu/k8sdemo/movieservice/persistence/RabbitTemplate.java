package org.eshishkin.edu.k8sdemo.movieservice.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.eshishkin.edu.k8sdemo.movieservice.config.RabbitConfig;
import org.eshishkin.edu.k8sdemo.movieservice.persistence.event.MovieServiceEvent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class RabbitTemplate {

    @Inject
    RabbitConfig config;

    @Inject
    ObjectMapper objectMapper;

    private GenericObjectPool<Connection> pool;

    @PostConstruct
    void init() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = createConnectionFactory();
        pool = new GenericObjectPool<>(new BasePooledObjectFactory<Connection>() {
            @Override
            public Connection create() throws Exception {
                return connectionFactory.newConnection();
            }

            @Override
            public PooledObject<Connection> wrap(Connection obj) {
                return new DefaultPooledObject<>(obj);
            }

            @Override
            public void destroyObject(PooledObject<Connection> p) throws Exception {
                p.getObject().close();
            }
        });
    }

    public void send(String routingKey, MovieServiceEvent event) {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().build();
        publish(routingKey, properties, toBytes(event));
    }

    private void publish(String routingKey, AMQP.BasicProperties properties, byte[] body) {
        Connection connection = null;

        try {
            connection = pool.borrowObject();
            try (Channel channel = createChannel(connection)) {
                channel.basicPublish(
                        config.getExchange(),
                        routingKey,
                        properties,
                        body
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        } finally {
            if (connection != null) {
                pool.returnObject(connection);
            }
        }
    }

    @SneakyThrows
    private Channel createChannel(Connection connection) {
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(config.getExchange(), BuiltinExchangeType.DIRECT, true);
        channel.queueDeclare(config.getReviewQueue(), true, false, false, new HashMap<>());
        channel.queueDeclare(config.getMovieQueue(), true, false, false, new HashMap<>());

        channel.queueBind(config.getReviewQueue(), config.getExchange(), config.getReviewRoutingKey());
        channel.queueBind(config.getMovieQueue(), config.getExchange(), config.getMovieRoutingKey());

        return channel;
    }

    @SneakyThrows
    private ConnectionFactory createConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(config.getUrl());
        factory.setUsername(config.getUser());
        factory.setPassword(config.getPassword());
        return factory;
    }

    @SneakyThrows
    private byte[] toBytes(Object data) {
        return objectMapper.writeValueAsBytes(data);
    }

    @PreDestroy
    void destroy() {
        pool.close();
    }

}
