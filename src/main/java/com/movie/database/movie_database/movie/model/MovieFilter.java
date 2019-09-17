package com.movie.database.movie_database.movie.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieFilter {

    private final String title;
    private final String description;
    private final List<UUID> actors;
    private final List<UUID> directors;
    private final List<UUID> categories;

    public MovieFilter(String title, String description, List<UUID> actors, List<UUID> directors, List<UUID> categories) {
        this.title = title;
        this.description = description;
        this.actors = actors;
        this.directors = directors;
        this.categories = categories;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<List<UUID>> getActors() {
        return Optional.ofNullable(actors);
    }

    public Optional<List<UUID>> getDirectors() {
        return Optional.ofNullable(directors);
    }

    public Optional<List<UUID>> getCategories() {
        return Optional.ofNullable(categories);
    }
}
