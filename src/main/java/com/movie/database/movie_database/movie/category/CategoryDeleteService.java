package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryDeleteService {

    private final CategoryRepository categoryRepository;

    public CategoryDeleteService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void delete(UUID categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
