package com.movie.database.movie_database.user;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserCreateService userCreateService;

    public UserController(UserCreateService userCreateService) {
        this.userCreateService = userCreateService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/users/sign-up")
    public void signUp(@RequestBody ApplicationUser applicationUser) {
        userCreateService.create(applicationUser);
    }
}
