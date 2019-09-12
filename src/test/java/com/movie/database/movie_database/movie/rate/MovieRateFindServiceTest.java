package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRateFindServiceTest {

    @Mock
    private MovieRateRepository movieRateRepository;
    @InjectMocks
    private MovieRateFindService movieRateFindService;

    @Test
    @DisplayName("Should return optional with MovieRate when existing in database")
    public void shouldReturnMovieRateWhenExist() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var movieRate = Optional.of(new MovieRate(user, movie, 3.0));
        when(movieRateRepository.findByApplicationUserAndMovie(user, movie)).thenReturn(movieRate);

        //act
        var result = movieRateFindService.findByUserAndMovie(user, movie);

        //assert
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(movieRate);
    }

    @Test
    @DisplayName("Should return empty optional with when not found in database")
    public void shouldReturnEmptyOptionalWhenNotFound() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        when(movieRateRepository.findByApplicationUserAndMovie(user, movie)).thenReturn(Optional.empty());

        //act
        var result = movieRateFindService.findByUserAndMovie(user, movie);

        //assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should return all rates of movie")
    public void ShouldReturnAllRatesOfMovie() {
        //arrange
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var rate = 3.4;
        var secondRate = 3.5;
        var thirdRate = 3.7;
        var movieRate = createMovieRate(movie, rate);
        var secondMovieRate = createMovieRate(movie, secondRate);
        var thirdMovieRate = createMovieRate(movie, thirdRate);
        var movieRates = List.of(movieRate, secondMovieRate, thirdMovieRate);
        when(movieRateRepository.findAllByMovie(movie)).thenReturn(movieRates);

        //act
        var result = movieRateFindService.findAllRatesByMovie(movie);

        //assert
        assertThat(result).containsExactly(rate, secondRate, thirdRate);
    }

    @Test
    @DisplayName("Should return empty list when movie has not any rates")
    public void shouldReturnEmptyListWhenMovieHasNotAnyRates() {
        //arrange
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        when(movieRateRepository.findAllByMovie(movie)).thenReturn(Lists.emptyList());

        //act
        var result = movieRateFindService.findAllRatesByMovie(movie);

        //assert
        assertThat(result).isEmpty();
    }

    private MovieRate createMovieRate(Movie movie, double rate) {
        var user = new ApplicationUser("username", "password", "email@email.ca");
        return new MovieRate(user, movie, rate);
    }
}