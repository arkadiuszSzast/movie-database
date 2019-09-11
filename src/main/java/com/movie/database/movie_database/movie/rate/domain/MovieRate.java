package com.movie.database.movie_database.movie.rate.domain;

import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.support.Identifiable;
import com.movie.database.movie_database.user.domain.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MovieRate extends Identifiable {

    private double rate;
    @ManyToOne
    @JoinColumn(name = "applicationUser", referencedColumnName = "id")
    private ApplicationUser applicationUser;
    @ManyToOne
    @JoinColumn(name = "movie", referencedColumnName = "id")
    private Movie movie;

    public MovieRate() {
    }

    public MovieRate(ApplicationUser applicationUser, Movie movie, double rate) {
        this.rate = rate;
        this.applicationUser = applicationUser;
        this.movie = movie;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
