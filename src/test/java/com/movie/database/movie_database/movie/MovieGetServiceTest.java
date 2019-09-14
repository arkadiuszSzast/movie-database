package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.domain.MovieRepository;
import com.movie.database.movie_database.movie.exception.MovieNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieGetServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private MovieGetService movieGetService;

    @Test
    @DisplayName("Should return movie by given id")
    public void shouldReturnMovieByGivenId() {
        //arrange
        var movieId = UUID.randomUUID();
        var categories = List.of(new Category("category1"), new Category("category2"));
        categories.forEach(category -> category.setId(UUID.randomUUID()));
        var directors = List.of(new Director("director_name", "director_surname"));
        directors.forEach(director -> director.setId(UUID.randomUUID()));
        var actors = List.of(new Actor("actor_name", "actor_surname"), new Actor("actor_name2", "actor_surname2"));
        actors.forEach(actor -> actor.setId(UUID.randomUUID()));
        ;
        var movie = new Movie("title", "description", categories, actors, directors);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        //act
        var result = movieGetService.getMovieById(movieId);

        //assert
        assertThat(result).isEqualToComparingFieldByField(movie);
    }

    @Test
    @DisplayName("Should throw exception when movie not found")
    public void shouldThrowExceptionWhenMovieNotFound() {
        //arrange
        var movieId = UUID.randomUUID();
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        //act && assert
        assertThrows(MovieNotFoundException.class, () -> movieGetService.getMovieById(movieId));
    }

    @Test
    @DisplayName("Should return list of movies")
    public void shouldReturnListOfMovies() {
        //arrange
        var categories = List.of(new Category("category1"), new Category("category2"));
        categories.forEach(category -> category.setId(UUID.randomUUID()));
        var directors = List.of(new Director("director_name", "director_surname"));
        directors.forEach(director -> director.setId(UUID.randomUUID()));
        var actors = List.of(new Actor("actor_name", "actor_surname"), new Actor("actor_name2", "actor_surname2"));
        actors.forEach(actor -> actor.setId(UUID.randomUUID()));
        ;
        var movie = new Movie("title", "description", categories, actors, directors);
        var movies = List.of(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        //act
        var result = movieGetService.getMovies();

        //assert
        assertThat(result).usingFieldByFieldElementComparator().containsAll(movies);
    }

    @Test
    @DisplayName("Should return empty list when no movie found")
    public void shouldReturnEmptyListWhenNoMovieFound() {
        //arrange
        when(movieRepository.findAll()).thenReturn(List.of());

        //act
        var result = movieRepository.findAll();

        //assert
        assertThat(result).isEmpty();
    }
}