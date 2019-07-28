package com.movie.database.movie_database.movie.domain;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.support.Identifiable;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Movie extends Identifiable {

    private String title;
    private String description;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_category",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category", referencedColumnName = "id")}
    )
    private List<Category> categories;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_actor",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "actor", referencedColumnName = "id")}
    )
    private List<Actor> actors;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_director",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "director", referencedColumnName = "id")}
    )
    private List<Director> directors;

    public Movie() {
    }

    public Movie(String title, String description, List<Category> categories, List<Actor> actors, List<Director> directors) {
        this.title = title;
        this.description = description;
        this.categories = categories;
        this.actors = actors;
        this.directors = directors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }
}

