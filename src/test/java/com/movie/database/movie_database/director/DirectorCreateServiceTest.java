package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectorCreateServiceTest {

    @Mock
    private DirectorRepository directorRepository;
    @InjectMocks
    private DirectorCreateService directorCreateService;

    @Test
    @DisplayName("Should return saved director")
    public void shouldReturnSavedDirector() {
        //arrange
        var director = new Director("name", "surname");
        when(directorRepository.save(director)).thenReturn(director);

        //act
        var result = directorCreateService.create(director);

        //assert
        assertThat(result).isEqualToComparingFieldByField(director);
    }
}