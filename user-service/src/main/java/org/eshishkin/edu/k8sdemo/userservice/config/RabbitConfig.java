package org.eshishkin.edu.k8sdemo.userservice.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class RabbitConfig {

    @Value("${application.messaging.routing-key}")
    private String routingKey;

    @Bean
    public Queue queue(@Value("${application.messaging.queue}") String name) {
        return new Queue(name, false);
    }

    @Bean
    public TopicExchange exchange(@Value("${application.messaging.exchange}") String name) {
        return new TopicExchange(name);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
