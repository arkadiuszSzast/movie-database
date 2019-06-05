package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryCreateService {

    private final CategoryRepository categoryRepository;

    public CategoryCreateService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void create(Category category) {
        categoryRepository.save(category);
    }
}
