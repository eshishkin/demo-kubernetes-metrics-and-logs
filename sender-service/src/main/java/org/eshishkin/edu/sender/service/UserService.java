package org.eshishkin.edu.sender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eshishkin.edu.sender.model.Event;
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

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${application.jms.queues.events-queue-name}")
    private String queue;

    public void createRandomUser() {
        UserCreateRequest request = UserCreateRequest.create(RandomStringUtils.randomAlphabetic(10));

        rabbitTemplate.convertAndSend(queue, request, m -> {
            String payload = toJson(request);
            log.info("Sending request {} to \"{}\" queue", payload, queue);
            eventRepository.save(Event.of(payload));
            return m;
        });
    }

    public List<Event> getLastEvents() {
        List<Event> events = new ArrayList<>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
