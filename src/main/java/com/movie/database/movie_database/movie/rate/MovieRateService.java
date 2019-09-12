package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.movie.MovieGetService;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.DoubleStream;

@Service
public class MovieRateService {

    private static final double ZERO = 0.0;

    private final ApplicationUserGetService applicationUserGetService;
    private final MovieGetService movieGetService;
    private final MovieRateFindService movieRateFindService;
    private final MovieRateUpdateService movieRateUpdateService;
    private final MovieRateSaveService movieRateSaveService;

    public MovieRateService(ApplicationUserGetService applicationUserGetService, MovieGetService movieGetService,
                            MovieRateFindService movieRateFindService, MovieRateUpdateService movieRateUpdateService,
                            MovieRateSaveService movieRateSaveService) {
        this.applicationUserGetService = applicationUserGetService;
        this.movieGetService = movieGetService;
        this.movieRateFindService = movieRateFindService;
        this.movieRateUpdateService = movieRateUpdateService;
        this.movieRateSaveService = movieRateSaveService;
    }

    void rate(UUID userId, UUID movieId, double rate) {
        var applicationUser = applicationUserGetService.findById(userId);
        var movie = movieGetService.getMovieById(movieId);
        var movieRateOptional = movieRateFindService.findByUserAndMovie(applicationUser, movie);
        movieRateOptional
                .ifPresentOrElse(movieRate -> updateRate(movieRate, rate), () -> createMovieRate(applicationUser, movie, rate));

    }

    double getAverageRateByMovie(UUID movieId) {
        var movie = movieGetService.getMovieById(movieId);
        var rates = movieRateFindService.findAllRatesByMovie(movie);
        return rates
                .stream()
                .flatMapToDouble(DoubleStream::of)
                .average()
                .orElse(ZERO);
    }

    private void updateRate(MovieRate movieRate, double rate) {
        movieRateUpdateService.update(movieRate, rate);
    }

    private void createMovieRate(ApplicationUser applicationUser, Movie movie, double rate) {
        var movieRate = new MovieRate(applicationUser, movie, rate);
        movieRateSaveService.save(movieRate);
    }
}
