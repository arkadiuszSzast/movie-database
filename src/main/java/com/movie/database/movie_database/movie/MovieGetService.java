package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.domain.MovieRepository;
import com.movie.database.movie_database.movie.domain.MovieSpecification;
import com.movie.database.movie_database.movie.exception.MovieNotFoundException;
import com.movie.database.movie_database.movie.model.MovieFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovieGetService {

    private final MovieRepository movieRepository;

    public MovieGetService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMovies(MovieFilter movieFilter) {
        var movieSpecification = new MovieSpecification(movieFilter);
        return movieRepository.findAll(movieSpecification);
    }

    public Movie getMovieById(UUID movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException(movieId));
    }
}
