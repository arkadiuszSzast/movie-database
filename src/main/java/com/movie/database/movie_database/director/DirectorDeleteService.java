package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DirectorDeleteService {

    private final DirectorRepository directorRepository;

    public DirectorDeleteService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public void delete(UUID directorId) {
        directorRepository.deleteById(directorId);
    }
}
