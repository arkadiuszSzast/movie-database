package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ActorController {

    private final ActorsGetService actorsGetService;
    private final ActorCreateService actorCreateService;
    private final ActorDeleteService actorDeleteService;

    public ActorController(ActorsGetService actorsGetService,
                           ActorCreateService actorCreateService,
                           ActorDeleteService actorDeleteService) {
        this.actorsGetService = actorsGetService;
        this.actorCreateService = actorCreateService;
        this.actorDeleteService = actorDeleteService;
    }

    @GetMapping("/api/actors")
    public List<Actor> getActors() {
        return actorsGetService.getAll();
    }

    @PostMapping("/api/actors")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addActor(@RequestBody Actor actor) {
        actorCreateService.create(actor);
    }

    @DeleteMapping("/api/actors/{actorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteActor(@PathVariable UUID actorId) {
        actorDeleteService.delete(actorId);
    }
}
