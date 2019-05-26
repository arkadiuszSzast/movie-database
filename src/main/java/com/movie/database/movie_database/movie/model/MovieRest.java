package com.movie.database.movie_database.movie.model;

import java.util.List;
import java.util.UUID;

public class MovieRest {

    private final UUID id;
    private final String title;
    private final String description;
    private final List<String> categories;

    public MovieRest(UUID id, String title, String description, List<String> categories) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categories = categories;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategories() {
        return categories;
    }
}
