package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryDeleteServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryDeleteService categoryDeleteService;

    @Test
    @DisplayName("Should delete category")
    public void shouldDeleteCategory() {
        //arrange
        var categoryId = UUID.randomUUID();

        //act
        categoryDeleteService.delete(categoryId);

        //assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

}