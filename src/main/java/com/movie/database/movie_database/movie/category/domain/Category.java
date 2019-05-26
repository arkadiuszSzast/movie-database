package com.movie.database.movie_database.movie.category.domain;

import com.movie.database.movie_database.support.Identifiable;

import javax.persistence.Entity;

@Entity
public class Category extends Identifiable {

    private String category;

    public Category() {
    }

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
