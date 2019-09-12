package com.movie.database.movie_database.movie.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class MovieRest {

    private final String title;
    private final String description;
    private final List<UUID> categories;
    private final List<UUID> directors;
    private final List<UUID> actors;

    public MovieRest(@JsonProperty("title") String title, @JsonProperty("description") String description,
                     @JsonProperty("categories") List<UUID> categories, @JsonProperty("directors") List<UUID> directors,
                     @JsonProperty("actors") List<UUID> actors) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.directors = directors;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<UUID> getCategories() {
        return categories;
    }

    public List<UUID> getDirectors() {
        return directors;
    }

    public List<UUID> getActors() {
        return actors;
    }
}
