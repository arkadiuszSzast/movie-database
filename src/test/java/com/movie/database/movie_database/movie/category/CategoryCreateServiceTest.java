package com.movie.database.movie_database.movie.category;

import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryCreateServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryCreateService categoryCreateService;

    @Test
    @DisplayName("Should save category")
    public void shouldSaveCategory() {
        //arrange
        var category = new Category("category");
        when(categoryRepository.save(category)).thenReturn(category);

        //assert
        var result = categoryCreateService.create(category);

        //act
        verify(categoryRepository, times(1)).save(category);
        assertThat(result).isEqualToComparingFieldByField(category);

    }
}