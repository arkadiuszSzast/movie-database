package com.movie.database.movie_database.actor.domain;

import com.movie.database.movie_database.support.Identifiable;

import javax.persistence.Entity;

@Entity
public class Actor extends Identifiable {

    private String name;
    private String surname;

    public Actor() {
    }

    public Actor(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
