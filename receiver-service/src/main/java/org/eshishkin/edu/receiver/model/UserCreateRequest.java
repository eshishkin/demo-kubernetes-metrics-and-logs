package org.eshishkin.edu.receiver.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreateRequest implements Serializable {
    private UUID userId;
    private String name;
}
