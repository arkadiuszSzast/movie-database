package com.movie.database.movie_database.movie.model;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.domain.Movie;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MovieRestMapper {

    public MovieRest mapToRest(Movie movie) {
        var categories = movie.getCategories()
                .stream()
                .map(Category::getCategory)
                .collect(Collectors.toList());
        return new MovieRest(movie.getId(), movie.getTitle(), movie.getDescription(), categories);
    }
}
