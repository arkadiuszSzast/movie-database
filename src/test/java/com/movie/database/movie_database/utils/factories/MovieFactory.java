package com.movie.database.movie_database.utils.factories;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import com.movie.database.movie_database.movie.domain.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MovieFactory {

    @Autowired
    private MovieRepository movieRepository;

    private UUID id = UUID.randomUUID();
    private String title;
    private String description;
    private List<Category> categories;

    public MovieFactory withId(UUID id) {
        this.id = id;
        return this;
    }

    public MovieFactory withName(String title) {
        this.title = title;
        return this;
    }

    public MovieFactory withDescription(String description) {
        this.description = description;
        return this;
    }

    public MovieFactory withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Movie createWithSave() {
        return movieRepository.save(create());
    }

    public Movie create() {
        var movie = new Movie(title, description, categories);
        movie.setId(id);
        return movie;
    }
}
