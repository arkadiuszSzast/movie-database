package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieRateSaveService {

    private final MovieRateRepository movieRateRepository;

    public MovieRateSaveService(MovieRateRepository movieRateRepository) {
        this.movieRateRepository = movieRateRepository;
    }

    public MovieRate save(MovieRate movieRate) {
        return movieRateRepository.save(movieRate);
    }
}
