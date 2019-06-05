package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.Director;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class DirectorController {

    private final DirectorGetService directorGetService;
    private final DirectorCreateService directorCreateService;
    private final DirectorDeleteService directorDeleteService;

    public DirectorController(DirectorGetService directorGetService,
                              DirectorCreateService directorCreateService,
                              DirectorDeleteService directorDeleteService) {
        this.directorGetService = directorGetService;
        this.directorCreateService = directorCreateService;
        this.directorDeleteService = directorDeleteService;
    }

    @GetMapping("/api/directors")
    public List<Director> getDirectors() {
        return directorGetService.getAll();
    }

    @PostMapping("/api/directors")
    public void addDirector(@RequestBody Director director) {
        directorCreateService.create(director);
    }

    @DeleteMapping("/api/directors/{directorId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void addDirector(@PathVariable UUID directorId) {
        directorDeleteService.delete(directorId);
    }
}
