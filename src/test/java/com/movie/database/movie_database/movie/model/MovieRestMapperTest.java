package com.movie.database.movie_database.movie.model;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.support.Identifiable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRestMapperTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private DirectorRepository directorRepository;
    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private MovieRestMapper movieRestMapper;

    @Test
    @DisplayName("Should map movieRest to domain object")
    public void shouldMapMovieRestToDomainObject() {
        //arrange
        var categoriesIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var directorsIds = List.of(UUID.randomUUID());
        var actorsIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var categories = List.of(new Category("category1"), new Category("category2"));
        var directors = List.of(new Director("director_name", "director_surname"));
        var actors = List.of(new Actor("actor_name", "actor_surname"), new Actor("actor_name2", "actor_surname2"));
        var movieRest = new MovieRest("title", "description", categoriesIds, directorsIds, actorsIds);
        var movie = new Movie(movieRest.getTitle(), movieRest.getDescription(), categories, actors, directors);
        when(categoryRepository.findAllById(categoriesIds)).thenReturn(categories);
        when(directorRepository.findAllById(directorsIds)).thenReturn(directors);
        when(actorRepository.findAllById(actorsIds)).thenReturn(actors);

        //act
        var result = movieRestMapper.mapToDomain(movieRest);

        //assert
        assertThat(result).isEqualToComparingFieldByField(movie);
    }

    @Test
    @DisplayName("Should map movie to movieRest")
    public void shouldMapMovieToMovieRest() {
        //arrange
        var categories = List.of(new Category("category1"), new Category("category2"));
        categories.forEach(category -> category.setId(UUID.randomUUID()));
        var directors = List.of(new Director("director_name", "director_surname"));
        directors.forEach(director -> director.setId(UUID.randomUUID()));
        var actors = List.of(new Actor("actor_name", "actor_surname"), new Actor("actor_name2", "actor_surname2"));
        actors.forEach(actor -> actor.setId(UUID.randomUUID()));
        var categoriesIds = categories
                .stream()
                .map(Identifiable::getId)
                .collect(Collectors.toList());
        var directorsIds = directors
                .stream()
                .map(Identifiable::getId)
                .collect(Collectors.toList());
        var actorsIds = actors
                .stream()
                .map(Identifiable::getId)
                .collect(Collectors.toList());
        var movie = new Movie("title", "description", categories, actors, directors);
        var movieRest = new MovieRest(movie.getTitle(), movie.getDescription(), categoriesIds, directorsIds, actorsIds);

        //act
        var result = movieRestMapper.mapToRest(movie);

        //assert
        assertThat(result).isEqualToComparingFieldByField(movieRest);
    }
}