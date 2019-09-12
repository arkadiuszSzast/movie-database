package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieRateProvider {

    @Autowired
    private MovieRateRepository movieRateRepository;

    @Autowired
    private UserProvider userProvider;


    public MovieRate createMovieRateAndSave(Movie movie, double rate) {
        var movieRate = createMovieRate(movie, rate);
        return movieRateRepository.save(movieRate);
    }

    public MovieRate createMovieRateAndSave(ApplicationUser applicationUser, Movie movie, double rate) {
        var movieRate = new MovieRate(applicationUser, movie, rate);
        return movieRateRepository.save(movieRate);
    }

    private MovieRate createMovieRate(Movie movie, double rate) {
        var user = userProvider.createRandomUserWithUserRole();
        return new MovieRate(user, movie, rate);
    }
}