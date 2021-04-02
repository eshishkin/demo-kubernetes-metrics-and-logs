package org.eshishkin.edu.k8sdemo.userservice.service;

import lombok.RequiredArgsConstructor;
import org.eshishkin.edu.k8sdemo.userservice.exception.UserNotFoundException;
import org.eshishkin.edu.k8sdemo.userservice.persistence.UserDocument;
import org.eshishkin.edu.k8sdemo.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EventSender sender;

    public Flux<UserDocument> getUsers() {
        return userRepository.findAll();
    }

    public Mono<UserDocument> create(UserDocument user) {
        return Mono.just(user)
                .doOnNext(doc -> doc.setId(UUID.randomUUID().toString()))
                .flatMap(userRepository::insert)
                .flatMap(doc -> sender.send(doc).thenReturn(doc));
    }

    public Mono<UserDocument> update(String userId, UserDocument user) {
        return get(userId)
                .doOnNext(doc -> {
                    doc.setFirstName(user.getFirstName());
                    doc.setLastName(user.getLastName());
                    doc.setEmail(user.getEmail());
                })
                .flatMap(userRepository::save)
                .flatMap(doc -> sender.send(doc).thenReturn(doc));
    }

    public Mono<UserDocument> delete(String userId) {
        return get(userId)
                .doOnNext(doc -> doc.setDeleted(true))
                .flatMap(userRepository::save)
                .flatMap(doc -> sender.send(doc).thenReturn(doc));
    }

    public Mono<UserDocument> get(String userId) {
        return userRepository
                .findById(userId)
                .filter(Predicate.not(UserDocument::isDeleted))
                .switchIfEmpty(Mono.error(() -> new UserNotFoundException("Unable to find entity: " + userId)));
    }
}
