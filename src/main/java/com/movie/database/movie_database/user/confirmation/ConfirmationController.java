package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.support.properties.MovieDbProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ConfirmationController {

    private final ApplicationUserConfirmationService applicationUserConfirmationService;
    private final MovieDbProperties movieDbProperties;

    public ConfirmationController(ApplicationUserConfirmationService applicationUserConfirmationService,
                                  MovieDbProperties movieDbProperties) {
        this.applicationUserConfirmationService = applicationUserConfirmationService;
        this.movieDbProperties = movieDbProperties;
    }

    @GetMapping("/api/confirm")
    public ResponseEntity<Object> confirmApplicationUser(@RequestParam String token) {
        applicationUserConfirmationService.confirm(token);
        return ResponseEntity.ok()
                .location(URI.create(movieDbProperties.getFrontendUrl() + "/confirmed"))
                .build();
    }
}
