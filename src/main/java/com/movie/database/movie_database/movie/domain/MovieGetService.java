package com.movie.database.movie_database.movie.domain;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieGetService {

    private final MovieRepository movieRepository;

    public MovieGetService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }
}
