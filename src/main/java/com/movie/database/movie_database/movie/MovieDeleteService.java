package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.movie.domain.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MovieDeleteService {

    private final MovieRepository movieRepository;

    public MovieDeleteService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void delete(UUID movieId) {
        movieRepository.deleteById(movieId);
    }
}
