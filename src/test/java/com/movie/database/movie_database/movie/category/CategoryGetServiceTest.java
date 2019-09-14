package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryGetServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryGetService categoryGetService;

    @Test
    @DisplayName("Should return list of categories")
    public void shouldReturnListOfCategories() {
        //arrange
        var categories = List.of(new Category("category1"), new Category("category2"));
        when(categoryRepository.findAll()).thenReturn(categories);

        //act
        var result = categoryGetService.getAll();

        //assert
        assertThat(result).containsAll(categories);
    }


    @Test
    @DisplayName("Should return empty list when categories not found")
    public void shouldReturnEmptyListWhenCategoriesNotFound() {
        //arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        //act
        var result = categoryGetService.getAll();

        //assert
        assertThat(result).isEmpty();
    }

}