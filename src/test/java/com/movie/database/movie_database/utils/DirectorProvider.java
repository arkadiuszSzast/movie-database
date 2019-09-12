package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

@Component
public class DirectorProvider {

    @Autowired
    private DirectorRepository directorRepository;

    public Director createAndSaveRandomDirector() {
        var name = RandomStringUtils.random(10);
        var surname = RandomStringUtils.random(10);
        return directorRepository.save(new Director(name, surname));
    }
}