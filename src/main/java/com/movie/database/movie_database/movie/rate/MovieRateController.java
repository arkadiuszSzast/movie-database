package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.support.user.CurrentUserId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class MovieRateController {

    private final MovieRateService movieRateService;

    public MovieRateController(MovieRateService movieRateService) {
        this.movieRateService = movieRateService;
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping("/api/movie-rate")
    public void rateMovie(@CurrentUserId UUID userId, @RequestParam UUID movieId, @RequestParam double rate) {
        movieRateService.rate(userId, movieId, rate);
    }

    @GetMapping("/api/movie-rate")
    public double getMovieAverageRate(@RequestParam UUID movieId) {
        return movieRateService.getAverageRateByMovie(movieId);
    }
}
