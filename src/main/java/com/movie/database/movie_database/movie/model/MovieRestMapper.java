package com.movie.database.movie_database.movie.model;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import com.movie.database.movie_database.movie.domain.Movie;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MovieRestMapper {

    private final CategoryRepository categoryRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    public MovieRestMapper(CategoryRepository categoryRepository, DirectorRepository directorRepository, ActorRepository actorRepository) {
        this.categoryRepository = categoryRepository;
        this.directorRepository = directorRepository;
        this.actorRepository = actorRepository;
    }

    public MovieRest mapToRest(Movie movie) {
        var categories = movie.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        var directors = movie.getDirectors()
                .stream()
                .map(Director::getId)
                .collect(Collectors.toList());
        var actors = movie.getActors()
                .stream()
                .map(Actor::getId)
                .collect(Collectors.toList());
        return new MovieRest(movie.getTitle(), movie.getDescription(), categories, directors, actors);
    }

    public Movie mapToDomain(MovieRest rest) {
        var categories = categoryRepository.findAllById(rest.getCategories());
        var directors = directorRepository.findAllById(rest.getDirectors());
        var actors = actorRepository.findAllById(rest.getActors());

        return new Movie(rest.getTitle(), rest.getDescription(), categories, actors, directors);
    }
}
