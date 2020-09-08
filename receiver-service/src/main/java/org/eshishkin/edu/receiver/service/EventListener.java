package org.eshishkin.edu.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eshishkin.edu.receiver.model.UserCreateRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventListener {

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "${application.jms.queues.events-queue-name}")
    public void listen(String event) {
        try {
            UserCreateRequest payload = mapper.readerFor(UserCreateRequest.class).readValue(event);
            log.info("Message received for user: {}", payload.getUserId()) ;
        } catch (JsonProcessingException ex) {
            log.warn("Unable to parse incoming message", ex);
        }
    }

}
