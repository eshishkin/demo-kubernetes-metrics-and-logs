package org.eshishkin.edu.k8sdemo.userservice.repository;

import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {


}
