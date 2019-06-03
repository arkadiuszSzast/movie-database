package com.movie.database.movie_database.movie.domain;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.support.Identifiable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Movie extends Identifiable {

    private String title;
    private String description;
    @ManyToMany
    @JoinTable(name = "movie_category",
            joinColumns = {@JoinColumn(name = "movie", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category", referencedColumnName = "id")}
    )
    private List<Category> categories;

    public Movie() {
    }

    public Movie(String title, String description, List<Category> categories) {
        this.title = title;
        this.description = description;
        this.categories = categories;
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
}

