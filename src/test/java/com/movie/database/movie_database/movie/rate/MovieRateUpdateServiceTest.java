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
class MovieRateUpdateServiceTest {

    @Mock
    private MovieRateRepository movieRateRepository;
    @InjectMocks
    private MovieRateUpdateService movieRateUpdateService;

    @Test
    @DisplayName("Should return updated entity")
    public void shouldReturnUpdatedEntity() {
        //arrange
        var user = new ApplicationUser("username", "password", "email@email.ca");
        var movie = new Movie("title", "description", List.of(new Category("category")),
                List.of(new Actor("name", "surname")),
                List.of(new Director("director_name", "director_surname")));
        var updatedRate = 4.0;
        var movieRate = new MovieRate(user, movie, 3.0);
        var updatedMovieRate = new MovieRate(user, movie, updatedRate);
        when(movieRateRepository.save(movieRate)).thenReturn(updatedMovieRate);

        //act
        var result = movieRateUpdateService.update(movieRate, updatedRate);

        //assert
        assertThat(result).isEqualToComparingFieldByField(updatedMovieRate);
    }
}