package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import org.springframework.stereotype.Service;

@Service
public class ActorCreateService {

    private final ActorRepository actorRepository;

    public ActorCreateService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void create(Actor actor) {
        actorRepository.save(actor);
    }
}
