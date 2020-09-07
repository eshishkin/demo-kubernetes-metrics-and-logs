package org.eshishkin.edu.sender.web;

import org.eshishkin.edu.sender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @PostMapping("/createRandomUser")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createRandomUser() {
        userService.createRandomUser();
    }
}
