package org.eshishkin.edu.k8sdemo.movieservice.config;

import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;

@Getter
@Singleton
public class RabbitConfig {

    @ConfigProperty(name = "application.messaging.rabbitmq.address")
    String url;

    @ConfigProperty(name = "application.messaging.rabbitmq.username")
    String user;

    @ConfigProperty(name = "application.messaging.rabbitmq.password")
    String password;

    @ConfigProperty(name = "application.messaging.exchange")
    String exchange;

    @ConfigProperty(name = "application.messaging.movie-queue")
    String movieQueue;

    @ConfigProperty(name = "application.messaging.review-queue")
    String reviewQueue;

    @ConfigProperty(name = "application.messaging.movie-routing-key")
    String movieRoutingKey;

    @ConfigProperty(name = "application.messaging.review-routing-key")
    String reviewRoutingKey;
}
