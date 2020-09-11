package org.eshishkin.edu.sender.web;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eshishkin.edu.sender.model.Event;
import org.eshishkin.edu.sender.service.LoadGenerationService;
import org.eshishkin.edu.sender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoadGenerationService loadGenerationService;

    @PostMapping("/createRandomUser")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createRandomUser() {
        userService.createRandomUser();
    }

    @GetMapping("/lastEvents")
    public List<Event> getLastEvents() {
        return userService.getLastEvents();
    }

    @PostMapping("/heavyTask")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void heavyTask() {
        CompletableFuture.runAsync(() -> loadGenerationService.heavyTask());
    }

}
