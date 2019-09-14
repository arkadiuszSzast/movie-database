package com.movie.database.movie_database.utils;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CategoryProvider {

    private final CategoryRepository categoryRepository;

    public CategoryProvider(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String category) {
        return categoryRepository.save(new Category(category));
    }
}
