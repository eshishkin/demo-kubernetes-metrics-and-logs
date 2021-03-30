package org.eshishkin.edu.k8sdemo.userservice.web;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.eshishkin.edu.k8sdemo.userservice.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    @GetMapping
    public Flux<UserDocument> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public Mono<UserDocument> get(@PathVariable String userId) {
        return userService.get(userId);
    }

    @PostMapping
    public Mono<UserDocument> create(@Valid @RequestBody UserDocument user) {
        return userService.create(user);
    }

    @PutMapping("/{userId}")
    public Mono<UserDocument> update(@PathVariable String userId,
                                     @Valid @RequestBody UserDocument user) {
        return userService.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public Mono<UserDocument> delete(@PathVariable String userId) {
        return userService.delete(userId);
    }
}
