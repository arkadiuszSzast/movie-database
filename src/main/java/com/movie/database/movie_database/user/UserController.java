package com.movie.database.movie_database.user;

import com.movie.database.movie_database.config.security.jwt.RefreshTokenService;
import com.movie.database.movie_database.support.recaptcha.RecaptchaValid;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class UserController {

    private final ApplicationUserCreateService applicationUserCreateService;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationUserGetService applicationUserGetService;

    public UserController(ApplicationUserCreateService applicationUserCreateService,
                          RefreshTokenService refreshTokenService,
                          ApplicationUserGetService applicationUserGetService) {
        this.applicationUserCreateService = applicationUserCreateService;
        this.refreshTokenService = refreshTokenService;
        this.applicationUserGetService = applicationUserGetService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/users/sign-up")
    public void signUp(@RequestBody ApplicationUser applicationUser,
                       @RequestParam @RecaptchaValid String recaptchaResponse) {
        applicationUserCreateService.create(applicationUser);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity refreshToken(@RequestParam String refreshToken) {
        return refreshTokenService.refreshToken(refreshToken);
    }

    @GetMapping("/api/users")
    public List<ApplicationUserRest> findAll() {
        return applicationUserGetService.findAll();
    }

    @PostMapping("/test")
    public String refreshToken() {
        return "test";
    }

}
