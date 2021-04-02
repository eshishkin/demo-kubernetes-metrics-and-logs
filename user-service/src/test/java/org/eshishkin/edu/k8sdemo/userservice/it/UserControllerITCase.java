package org.eshishkin.edu.k8sdemo.userservice.it;

import org.eshishkin.edu.k8sdemo.userservice.model.UserEvent;
import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.eshishkin.edu.k8sdemo.userservice.web.ExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerITCase {

    @Container
    static MongoDBContainer MONGO = new MongoDBContainer("mongo:4.4.2");

    @Container
    static RabbitMQContainer RABBIT = new RabbitMQContainer("rabbitmq:3.8.8-management");

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
        registry.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
        registry.add("spring.rabbitmq.host", RABBIT::getHost);
        registry.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT::getAdminPassword);
    }

    @BeforeEach
    void cleanUp() {
        reactiveMongoTemplate.getMongoDatabase()
                .flatMap(x -> Mono.from(x.drop()))
                .block();
    }

    @Test
    public void testCreateUser() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromValue(prepareRequest()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDocument.class)
                .value(result -> {
                    assertNotNull(result.getId());
                    assertEquals("John", result.getFirstName());
                    assertEquals("Doe", result.getLastName());
                    assertEquals("john.doe@example.com", result.getEmail());

                    assertMessageForMessage(result.getId(), "CREATED");
                });

    }

    @Test
    public void testGetNonExistingUser() {
        webTestClient.get().uri("/users/" + UUID.randomUUID())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionAdvice.ErrorPayload.class)
                .value(ex -> assertEquals("USER_NOT_FOUND", ex.getCode()));
    }

    @Test
    public void testGetCreatedUser() {
        String id = webTestClient.post().uri("/users")
                .body(BodyInserters.fromValue(prepareRequest()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDocument.class)
                .returnResult()
                .getResponseBody()
                .getId();

        assertMessageForMessage(id, "CREATED");

        webTestClient.get().uri("/users/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDocument.class)
                .value(result -> {
                    assertNotNull(result.getId());
                    assertEquals("John", result.getFirstName());
                    assertEquals("Doe", result.getLastName());
                    assertEquals("john.doe@example.com", result.getEmail());
                });
    }

    @Test
    public void testGetAllUsers() {
        Stream.of(prepareRequest(), prepareRequest(), prepareRequest()).forEach(request -> {
            webTestClient.post().uri("/users")
                    .body(BodyInserters.fromValue(request))
                    .exchange()
                    .expectStatus()
                    .isOk();

            assertMessageForMessage("CREATED");
        });

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserDocument.class)
                .hasSize(3);
    }

    @Test
    public void testUpdateUser() {
        //given
        String id = webTestClient.post().uri("/users")
                .body(BodyInserters.fromValue(prepareRequest()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UserDocument.class)
                .returnResult()
                .getResponseBody()
                .getId();

        assertMessageForMessage(id, "CREATED");

        //when
        UserDocument payload = prepareRequest();
        payload.setFirstName("Updated");

        //then
        webTestClient.put().uri("/users/" + id)
                .body(BodyInserters.fromValue(payload))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDocument.class)
                .value(result -> {
                    assertEquals(id, result.getId());
                    assertEquals("Updated", result.getFirstName());
                    assertEquals("Doe", result.getLastName());
                    assertEquals("john.doe@example.com", result.getEmail());
                });

        assertMessageForMessage(id, "UPDATED");
    }

    private UserDocument prepareRequest() {
        UserDocument request = new UserDocument();
        request.setId("dummy");
        request.setEmail("john.doe@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");

        return request;
    }

    private void assertMessageForMessage(String id, String type) {
        UserEvent event = rabbitTemplate.receiveAndConvert(
                "user.updates",
                1000,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(event);
        assertEquals(type, event.getType());
        assertEquals(id, event.getUserId());
    }

    private void assertMessageForMessage(String type) {
        UserEvent event = rabbitTemplate.receiveAndConvert(
                "user.updates",
                1000,
                new ParameterizedTypeReference<>() {}
        );

        assertNotNull(event);
        assertEquals(type, event.getType());
    }
}
