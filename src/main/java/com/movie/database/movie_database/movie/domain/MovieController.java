package com.movie.database.movie_database.movie.domain;

import com.movie.database.movie_database.movie.MovieCreateService;
import com.movie.database.movie_database.movie.MovieDeleteService;
import com.movie.database.movie_database.movie.MovieGetService;
import com.movie.database.movie_database.movie.model.MovieRest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class MovieController {

    private final MovieGetService movieGetService;
    private final MovieCreateService movieCreateService;
    private final MovieDeleteService movieDeleteService;

    public MovieController(MovieGetService movieGetService, MovieCreateService movieCreateService, MovieDeleteService movieDeleteService) {
        this.movieGetService = movieGetService;
        this.movieCreateService = movieCreateService;
        this.movieDeleteService = movieDeleteService;
    }

    @GetMapping("/api/movies")
    public List<Movie> getMovies() {
        return movieGetService.getMovies();
    }

    @PostMapping("/api/movies")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@RequestBody MovieRest movieRest) {
        movieCreateService.addMovie(movieRest);
    }

    @DeleteMapping("/api/movies/{movieId}")
    public void deleteMovie(@PathVariable UUID movieId) {
        movieDeleteService.delete(movieId);
    }
}
