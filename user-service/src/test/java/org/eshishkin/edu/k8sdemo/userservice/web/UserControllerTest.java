package org.eshishkin.edu.k8sdemo.userservice.web;

import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.eshishkin.edu.k8sdemo.userservice.service.UserService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest(controllers = {UserController.class})
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @CsvSource(value = {
            "John, Doe, john.doe_example.com",
            "John, N/A, john.doe@example.com",
            "N/A, Doe, john.doe@example.com"
    }, nullValues = "N/A")
    public void testValidation(String firstName, String lastName, String email) {
        UserDocument request = new UserDocument();

        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);

        System.out.println(request);

        webTestClient.post().uri("/users")
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
