package org.eshishkin.edu.k8sdemo.movieservice.persistence.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
public class MovieServiceEvent implements Serializable {
    private final String entityId;
    private final MessageType type;

    public  enum MessageType {
        CREATED, DELETED
    }
}
