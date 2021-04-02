package org.eshishkin.edu.k8sdemo.userservice.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Document(collection = "users")
public class UserDocument implements Serializable {

    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 64)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 64)
    private String lastName;

    @NotNull
    @Email
    private String email;

    private boolean deleted;

    @Version
    private Long version;
}
