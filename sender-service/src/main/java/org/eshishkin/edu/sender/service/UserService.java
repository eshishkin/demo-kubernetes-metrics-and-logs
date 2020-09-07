package org.eshishkin.edu.sender.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eshishkin.edu.sender.model.UserCreateRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${application.jms.queues.events-queue-name}")
    private String queue;

    public void createRandomUser() {
        UserCreateRequest request = UserCreateRequest.create(RandomStringUtils.randomAlphabetic(10));

        rabbitTemplate.convertAndSend(queue, request, m -> {
            log.info("Sending request {} to {}", request, queue);
            return m;
        });
    }
}
