package com.movie.database.movie_database.user;

import com.movie.database.movie_database.config.security.jwt.RefreshTokenService;
import com.movie.database.movie_database.support.recaptcha.RecaptchaValid;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklist;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@Validated
public class ApplicationUserController {

    private final ApplicationUserCreateService applicationUserCreateService;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationUserGetService applicationUserGetService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public ApplicationUserController(ApplicationUserCreateService applicationUserCreateService,
                                     RefreshTokenService refreshTokenService,
                                     ApplicationUserGetService applicationUserGetService,
                                     TokenBlacklistRepository tokenBlacklistRepository) {
        this.applicationUserCreateService = applicationUserCreateService;
        this.refreshTokenService = refreshTokenService;
        this.applicationUserGetService = applicationUserGetService;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/users/sign-up")
    public void signUp(@Valid @RequestBody ApplicationUser applicationUser,
                       @RequestParam @RecaptchaValid String recaptchaResponse) throws IOException {
        applicationUserCreateService.create(applicationUser);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity refreshToken(@RequestHeader(name = "Refresh-token") String refreshToken) {
        return refreshTokenService.refreshToken(refreshToken);
    }

    @PostMapping("/api/auth/logout")
    public void logout(@RequestParam String token) {
        tokenBlacklistRepository.save(new TokenBlacklist(token));
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ApplicationUserRest> findAll() {
        return applicationUserGetService.findAll();
    }

}
