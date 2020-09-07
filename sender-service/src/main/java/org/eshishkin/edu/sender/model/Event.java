package org.eshishkin.edu.sender.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter @Setter
@RedisHash(value = "events", timeToLive = 3600)
public class Event implements Serializable {

    @Id
    private UUID id;

    private String event;

    public static Event of(String payload) {
        Event event = new Event();
        event.setId(UUID.randomUUID());
        event.setEvent(payload);
        return event;
    }
}
