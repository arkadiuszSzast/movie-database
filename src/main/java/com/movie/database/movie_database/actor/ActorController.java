package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ActorController {

    private final ActorsGetService actorsGetService;
    private final ActorCreateService actorCreateService;

    public ActorController(ActorsGetService actorsGetService, ActorCreateService actorCreateService) {
        this.actorsGetService = actorsGetService;
        this.actorCreateService = actorCreateService;
    }

    @GetMapping("/api/actors")
    public List<Actor> getActors() {
        return actorsGetService.getAll();
    }

    @PostMapping("/api/actors")
    public void addActor(@RequestBody Actor actor) {
        actorCreateService.create(actor);
    }
}
