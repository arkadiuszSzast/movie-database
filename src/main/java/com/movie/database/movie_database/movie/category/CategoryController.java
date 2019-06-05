package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.Category;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CategoryController {

    private final CategoryGetService categoryGetService;
    private final CategoryCreateService categoryCreateService;
    private final CategoryDeleteService categoryDeleteService;

    public CategoryController(CategoryGetService categoryGetService,
                              CategoryCreateService categoryCreateService,
                              CategoryDeleteService categoryDeleteService) {
        this.categoryGetService = categoryGetService;
        this.categoryCreateService = categoryCreateService;
        this.categoryDeleteService = categoryDeleteService;
    }

    @GetMapping("/api/categories")
    public List<Category> getCategories() {
        return categoryGetService.getAll();
    }

    @PostMapping("/api/categories")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void addCategory(@RequestBody Category category) {
        categoryCreateService.create(category);
    }

    @DeleteMapping("/api/categories/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCategory(@PathVariable UUID categoryId) {
        categoryDeleteService.delete(categoryId);
    }
}
