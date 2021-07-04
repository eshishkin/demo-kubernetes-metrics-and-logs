package org.eshishkin.edu.k8sdemo.userservice.web;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.eshishkin.edu.k8sdemo.userservice.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ErrorPayload> onNotFound(UserNotFoundException ex) {
        return toPayload("USER_NOT_FOUND", ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Mono<ErrorPayload> onError(RuntimeException ex) {
        return Mono.just(ex)
                .doOnNext(e -> log.error("Unexpected error", e))
                .flatMap(e -> toPayload("UNEXPECTED_ERROR", e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ErrorPayload> onInvalidData(ServerWebInputException ex) {
        return toPayload("BAD_REQUEST", ex);
    }


    private Mono<ErrorPayload> toPayload(String code, Exception ex) {
        return Mono.just(ex)
                .map(e -> new ErrorPayload(code, getMessage(e)));
    }

    private String getMessage(Exception ex) {
        String message = ex.getMessage();
        if (!StringUtils.hasText(message)) {
            return "[No message]";
        }
        return message;
    }

    @Value
    public static class ErrorPayload {
        String code, message;
    }
}
