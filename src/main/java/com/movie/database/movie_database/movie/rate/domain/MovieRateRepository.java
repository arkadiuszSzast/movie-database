package com.movie.database.movie_database.movie.rate.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRateRepository extends JpaRepository<MovieRate, UUID> {
}
