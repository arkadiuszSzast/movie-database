package com.movie.database.movie_database.movie.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

    private final MovieGetService movieGetService;

    public MovieController(MovieGetService movieGetService) {
        this.movieGetService = movieGetService;
    }

    @GetMapping("/api/movies")
    public List<Movie> getMovies() {
        return movieGetService.getMovies();
    }

}
