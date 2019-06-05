package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorGetService {

    private final DirectorRepository directorRepository;

    public DirectorGetService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<Director> getAll() {
        return directorRepository.findAll();
    }
}
