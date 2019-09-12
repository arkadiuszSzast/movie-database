package com.movie.database.movie_database.movie.rate.domain;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRateRepository extends JpaRepository<MovieRate, UUID> {

    Optional<MovieRate> findByApplicationUserAndMovie(ApplicationUser applicationUser, Movie movie);

    List<MovieRate> findAllByMovie(Movie movie);
}
