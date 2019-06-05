package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.ActorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActorDeleteService {

    private final ActorRepository actorRepository;

    public ActorDeleteService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public void delete(UUID actorId) {
        actorRepository.deleteById(actorId);
    }
}
