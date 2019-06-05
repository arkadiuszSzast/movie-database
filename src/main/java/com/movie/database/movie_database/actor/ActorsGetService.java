package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorsGetService {

    private final ActorRepository actorRepository;

    public ActorsGetService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> getAll() {
        return actorRepository.findAll();
    }
}
