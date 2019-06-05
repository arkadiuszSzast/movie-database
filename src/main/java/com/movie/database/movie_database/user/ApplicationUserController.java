package com.movie.database.movie_database.user;

import com.movie.database.movie_database.config.security.jwt.RefreshTokenService;
import com.movie.database.movie_database.support.recaptcha.RecaptchaValid;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import com.movie.database.movie_database.user.model.ApplicationUserRest;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklist;
import com.movie.database.movie_database.user.token.blacklist.domain.TokenBlacklistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
public class ApplicationUserController {

    private final ApplicationUserCreateService applicationUserCreateService;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationUserGetService applicationUserGetService;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final ApplicationUserUpdateService applicationUserUpdateService;
    private final ApplicationUserDeleteService applicationUserDeleteService;

    public ApplicationUserController(ApplicationUserCreateService applicationUserCreateService,
                                     RefreshTokenService refreshTokenService,
                                     ApplicationUserGetService applicationUserGetService,
                                     TokenBlacklistRepository tokenBlacklistRepository,
                                     ApplicationUserUpdateService applicationUserUpdateService,
                                     ApplicationUserDeleteService applicationUserDeleteService) {
        this.applicationUserCreateService = applicationUserCreateService;
        this.refreshTokenService = refreshTokenService;
        this.applicationUserGetService = applicationUserGetService;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
        this.applicationUserUpdateService = applicationUserUpdateService;
        this.applicationUserDeleteService = applicationUserDeleteService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/users/sign-up")
    public void signUp(@Valid @RequestBody ApplicationUser applicationUser,
                       @RequestParam @RecaptchaValid String recaptchaResponse) throws IOException, RoleNotFoundException {
        applicationUserCreateService.create(applicationUser);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity refreshToken(@RequestHeader(name = "Refresh-token") String refreshToken) {
        return refreshTokenService.refreshToken(refreshToken);
    }

    @PostMapping("/api/auth/logout")
    public void logout(@RequestHeader(name = "Authorization") String token) {
        tokenBlacklistRepository.save(new TokenBlacklist(token));
    }

    @GetMapping("/api/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ApplicationUserRest> findAll() {
        return applicationUserGetService.findAll();
    }

    @PutMapping("/api/users/{applicationUserId}/roles")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateRoles(@PathVariable UUID applicationUserId, @RequestBody List<Role> roles) {
        applicationUserUpdateService.updateRoles(applicationUserId, roles);
    }

    @DeleteMapping("/api/users/{applicationUserId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRoles(@PathVariable UUID applicationUserId) {
        applicationUserDeleteService.delete(applicationUserId);
    }

}
