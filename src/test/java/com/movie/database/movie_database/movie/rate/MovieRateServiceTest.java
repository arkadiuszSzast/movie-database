package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.MovieGetService;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.user.ApplicationUserGetService;
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
import java.util.UUID;
import java.util.stream.DoubleStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieRateServiceTest {

    private static final double ZERO = 0.0;

    @Mock
    private ApplicationUserGetService applicationUserGetService;
    @Mock
    private MovieGetService movieGetService;
    @Mock
    private MovieRateFindService movieRateFindService;
    @Mock
    private MovieRateUpdateService movieRateUpdateService;
    @Mock
    private MovieRateSaveService movieRateSaveService;
    @InjectMocks
    private MovieRateService movieRateService;

    @Test
    @DisplayName("Should return average rate of movie")
    public void shouldReturnAverageRateOfMovie() {
        //arrange
        var movieId = UUID.randomUUID();
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var rates = List.of(4.2, 4.5, 4.7);
        var average = rates
                .stream()
                .flatMapToDouble(DoubleStream::of)
                .average()
                .orElse(ZERO);
        when(movieGetService.getMovieById(movieId)).thenReturn(movie);
        when(movieRateFindService.findAllRatesByMovie(movie)).thenReturn(rates);

        //act
        var result = movieRateService.getAverageRateByMovie(movieId);

        //assert
        assertThat(result).isEqualTo(average);
    }

    @Test
    @DisplayName("Should return zero when movie has no rates")
    public void shouldReturnZeroWhenMovieHasNoRates() {
        //arrange
        var movieId = UUID.randomUUID();
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        when(movieGetService.getMovieById(movieId)).thenReturn(movie);
        when(movieRateFindService.findAllRatesByMovie(movie)).thenReturn(Lists.emptyList());

        //act
        var result = movieRateService.getAverageRateByMovie(movieId);

        //assert
        assertThat(result).isEqualTo(ZERO);
    }

    @Test
    @DisplayName("Should update rate when already exists")
    public void shouldUpdateRateWhenAlreadyExists() {
        //arrange
        var userId = UUID.randomUUID();
        var movieId = UUID.randomUUID();
        var rate = 4.4;
        var updatedRate = 4.6;
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var movieRate = new MovieRate(user, movie, rate);
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(movieGetService.getMovieById(movieId)).thenReturn(movie);
        when(movieRateFindService.findByUserAndMovie(user, movie)).thenReturn(Optional.of(movieRate));

        //act
        movieRateService.rate(userId, movieId, updatedRate);

        //assert
        verify(movieRateUpdateService, times(1)).update(movieRate, updatedRate);
        verify(movieRateSaveService, never()).save(movieRate);
    }

    @Test
    @DisplayName("Should save new rate when not exists in database")
    public void shouldSaveNewRateWhenNotExists() {
        //arrange
        var userId = UUID.randomUUID();
        var movieId = UUID.randomUUID();
        var rate = 4.4;
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var movieRate = new MovieRate(user, movie, rate);
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(movieGetService.getMovieById(movieId)).thenReturn(movie);
        when(movieRateFindService.findByUserAndMovie(user, movie)).thenReturn(Optional.empty());

        //act
        movieRateService.rate(userId, movieId, rate);

        //assert
        verify(movieRateSaveService, times(1)).save(any());
        verify(movieRateUpdateService, never()).update(movieRate, rate);
    }
}