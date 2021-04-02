package org.eshishkin.edu.k8sdemo.userservice.service;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.k8sdemo.userservice.config.RabbitConfig;
import org.eshishkin.edu.k8sdemo.userservice.model.UserEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventSender {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfig rabbitConfig;

    @Value("${application.messaging.routing-key}")
    private String routingKey;

    public Mono<Void> send(UserEvent event) {
        return Mono.fromRunnable(
                () -> rabbitTemplate.convertAndSend(rabbitConfig.getRoutingKey(), event)
        ).then();
    }
}
