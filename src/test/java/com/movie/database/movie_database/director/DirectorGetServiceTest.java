package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import org.assertj.core.util.Lists;
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
class DirectorGetServiceTest {

    @Mock
    private DirectorRepository directorRepository;
    @InjectMocks
    private DirectorGetService directorGetService;

    @Test
    @DisplayName("Should return list of directors")
    public void shouldReturnListOfDirecors() {
        //arrange
        var director = new Director("name", "surname");
        var secondDirector = new Director("second_name", "second_surname");
        var directors = List.of(director, secondDirector);
        when(directorRepository.findAll()).thenReturn(directors);

        //act
        var result = directorGetService.getAll();

        //assert
        assertThat(result).containsAll(directors);
    }

    @Test
    @DisplayName("Should return empty list when directors not found in database")
    public void shouldReturnEmptyListWhenDirectorsNotFoundInDatabase() {
        //arrange
        when(directorRepository.findAll()).thenReturn(Lists.emptyList());

        //act
        var result = directorGetService.getAll();

        //assert
        assertThat(result).isEmpty();
    }
}