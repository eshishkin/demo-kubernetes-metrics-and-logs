package org.eshishkin.edu.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eshishkin.edu.receiver.model.User;
import org.eshishkin.edu.receiver.model.UserCreateRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventListener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "${application.jms.queues.events-queue-name}")
    public void listen(String event) {
        try {
            UserCreateRequest request = mapper.readerFor(UserCreateRequest.class).readValue(event);
            log.info("Message received for user: {}", request.getUserId());

            User user = saveUser(request);
            log.info("User {} (name: {}) has been created in Mongo DB at {}",
                    user.getId(), user.getName(), user.getCreated()
            );
        } catch (JsonProcessingException ex) {
            log.warn("Unable to parse incoming message", ex);
        }
    }

    private User saveUser(UserCreateRequest request) {
        User user = new User();
        user.setId(request.getUserId());
        user.setName(request.getName());
        user.setCreated(Instant.now());
        user.setComment(RandomStringUtils.randomAlphabetic(64));

        return userRepository.save(user);
    }
}
