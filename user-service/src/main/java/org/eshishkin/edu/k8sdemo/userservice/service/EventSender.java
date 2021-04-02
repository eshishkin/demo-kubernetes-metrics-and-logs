package org.eshishkin.edu.k8sdemo.userservice.service;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${application.messaging.routing-key}")
    private String routingKey;

    public Mono<Void> send(UserDocument document) {
        return Mono
                .fromRunnable(() -> rabbitTemplate.convertAndSend(routingKey, document))
                .doOnEach(System.out::println)
                .then();
    }
}
