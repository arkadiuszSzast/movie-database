package com.movie.database.movie_database.user.confirmation;

import com.movie.database.movie_database.support.properties.MovieDbProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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
    public void confirmApplicationUser(@RequestParam String token, HttpServletResponse httpServletResponse) {
        applicationUserConfirmationService.confirm(token);
        httpServletResponse.setHeader("Location", movieDbProperties.getFrontendUrl() + "/confirmed");
        httpServletResponse.setStatus(302);
    }
}
