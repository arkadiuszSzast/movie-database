package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieRateUpdateService {

    private final MovieRateRepository movieRateRepository;

    public MovieRateUpdateService(MovieRateRepository movieRateRepository) {
        this.movieRateRepository = movieRateRepository;
    }

    MovieRate update(MovieRate movieRate, double rate) {
        movieRate.setRate(rate);
        return movieRateRepository.save(movieRate);
    }
}
