package com.movie.database.movie_database.user;

import com.movie.database.movie_database.config.security.jwt.RefreshTokenService;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    private final UserCreateService userCreateService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserCreateService userCreateService, RefreshTokenService refreshTokenService) {
        this.userCreateService = userCreateService;
        this.refreshTokenService = refreshTokenService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/users/sign-up")
    public void signUp(@RequestBody ApplicationUser applicationUser) {
        userCreateService.create(applicationUser);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity refreshToken(@RequestParam String refreshToken) {
        return refreshTokenService.refreshToken(refreshToken);
    }

    @PostMapping("/test")
    public String refreshToken() {
        return "test";
    }

}
