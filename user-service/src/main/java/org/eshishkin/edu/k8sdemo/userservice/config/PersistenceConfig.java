package org.eshishkin.edu.k8sdemo.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "org.eshishkin.edu.k8sdemo.userservice.repository")
public class PersistenceConfig {
}
