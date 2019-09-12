package com.movie.database.movie_database.director;

import com.movie.database.movie_database.director.domain.DirectorRepository;
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
class DirectorDeleteServiceTest {

    @Mock
    private DirectorRepository directorRepository;
    @InjectMocks
    private DirectorDeleteService directorDeleteService;

    @Test
    @DisplayName("Should delete director")
    public void shouldDeleteDirector() {
        //arrange
        var directorId = UUID.randomUUID();

        //act
        directorDeleteService.delete(directorId);

        //assert
        verify(directorRepository, times(1)).deleteById(directorId);
    }
}