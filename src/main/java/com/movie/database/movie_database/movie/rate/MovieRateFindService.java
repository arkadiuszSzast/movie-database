package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieRateFindService {

    private final MovieRateRepository movieRateRepository;

    public MovieRateFindService(MovieRateRepository movieRateRepository) {
        this.movieRateRepository = movieRateRepository;
    }

    Optional<MovieRate> findByUserAndMovie(ApplicationUser applicationUser, Movie movie) {
        return movieRateRepository.findByApplicationUserAndMovie(applicationUser, movie);
    }
}
