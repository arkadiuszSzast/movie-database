package com.movie.database.movie_database.movie.rate;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.rate.domain.MovieRate;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRateSaveServiceTest {

    @Mock
    private MovieRateRepository movieRateRepository;
    @InjectMocks
    private MovieRateSaveService movieRateSaveService;

    @Test
    @DisplayName("Should return saved entity")
    public void shouldReturnSavedEntity() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var movieRate = new MovieRate(user, movie, 3.0);
        when(movieRateRepository.save(movieRate)).thenReturn(movieRate);

        //act
        var result = movieRateSaveService.save(movieRate);

        //assert
        assertThat(result).isEqualToComparingFieldByField(movieRate);
    }
}