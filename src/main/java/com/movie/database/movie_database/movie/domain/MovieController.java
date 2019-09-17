package com.movie.database.movie_database.movie.domain;

import com.movie.database.movie_database.movie.MovieCreateService;
import com.movie.database.movie_database.movie.MovieDeleteService;
import com.movie.database.movie_database.movie.MovieGetService;
import com.movie.database.movie_database.movie.model.MovieFilter;
import com.movie.database.movie_database.movie.model.MovieRest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<Movie> getMovies(MovieFilter movieFilter) {
        return movieGetService.getMovies(movieFilter);
    }

    @PostMapping("/api/movies")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void addMovie(@RequestBody MovieRest movieRest) {
        movieCreateService.addMovie(movieRest);
    }

    @DeleteMapping("/api/movies/{movieId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMovie(@PathVariable UUID movieId) {
        movieDeleteService.delete(movieId);
    }
}
