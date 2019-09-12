package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectorCreateService {

    private final DirectorRepository directorRepository;

    public DirectorCreateService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director create(Director director) {
        return directorRepository.save(director);
    }
}
