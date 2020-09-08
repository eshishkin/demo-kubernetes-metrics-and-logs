package org.eshishkin.edu.receiver.service;

import java.util.UUID;
import org.eshishkin.edu.receiver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, UUID> {
}
