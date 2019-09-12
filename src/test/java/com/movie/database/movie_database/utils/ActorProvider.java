package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@Component
public class ActorProvider {

    @Autowired
    private ActorRepository actorRepository;

    public Actor createAndSaveRandomActor() {
        var name = RandomStringUtils.random(10);
        var surname = RandomStringUtils.random(10);
        return actorRepository.save(new Actor(name, surname));
    }
}