package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.domain.MovieRepository;
import com.movie.database.movie_database.movie.model.MovieRest;
import com.movie.database.movie_database.movie.model.MovieRestMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieCreateServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieRestMapper movieRestMapper;
    @InjectMocks
    private MovieCreateService movieCreateService;

    @Test
    @DisplayName("Should create and save movie")
    public void shouldCreateSaveMovie() {
        //arrange
        var categoriesIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var directorsIds = List.of(UUID.randomUUID());
        var actorsIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var categories = List.of(new Category("category1"), new Category("category2"));
        var directors = List.of(new Director("director_name", "director_surname"));
        var actors = List.of(new Actor("actor_name", "actor_surname"), new Actor("actor_name2", "actor_surname2"));
        var movieRest = new MovieRest("title", "description", categoriesIds, directorsIds, actorsIds);
        var movie = new Movie(movieRest.getTitle(), movieRest.getDescription(), categories, actors, directors);
        when(movieRestMapper.mapToDomain(movieRest)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);

        //act
        var result = movieCreateService.addMovie(movieRest);

        //assert
        assertThat(result).isEqualToComparingFieldByField(movie);
        verify(movieRepository, times(1)).save(movie);
    }
}