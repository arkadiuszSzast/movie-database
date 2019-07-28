package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.movie.domain.MovieRepository;
import com.movie.database.movie_database.movie.model.MovieRest;
import com.movie.database.movie_database.movie.model.MovieRestMapper;
import org.springframework.stereotype.Service;

@Service
public class MovieCreateService {

    private final MovieRepository movieRepository;
    private final MovieRestMapper movieRestMapper;

    public MovieCreateService(MovieRepository movieRepository, MovieRestMapper movieRestMapper) {
        this.movieRepository = movieRepository;
        this.movieRestMapper = movieRestMapper;
    }

    public void addMovie(MovieRest movieRest) {
        var movie = movieRestMapper.mapToDomain(movieRest);
        movieRepository.save(movie);
    }
}
