package org.eshishkin.edu.k8sdemo.userservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserEvent implements Serializable {
    private String type;
    private String userId;

    public static UserEvent created(String userId) {
        return of(Type.CREATED, userId);
    }

    public static UserEvent deleted(String userId) {
        return of(Type.DELETED, userId);
    }

    public static UserEvent updated(String userId) {
        return of(Type.UPDATED, userId);
    }

    private static UserEvent of(Type type, String userId) {
        UserEvent event = new UserEvent();
        event.setType(type.name());
        event.setUserId(userId);
        return event;
    }

    public enum Type {
        DELETED, CREATED, UPDATED
    }
}
