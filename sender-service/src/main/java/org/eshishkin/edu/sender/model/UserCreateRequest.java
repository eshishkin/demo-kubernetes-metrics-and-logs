package org.eshishkin.edu.sender.model;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserCreateRequest implements Serializable {
    private UUID userId;
    private String name;

    public static UserCreateRequest create(String name) {
        UserCreateRequest user = new UserCreateRequest();
        user.setUserId(UUID.randomUUID());
        user.setName(name);
        return user;
    }
}
