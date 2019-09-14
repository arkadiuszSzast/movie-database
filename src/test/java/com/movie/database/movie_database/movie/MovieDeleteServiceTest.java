package com.movie.database.movie_database.movie;

import com.movie.database.movie_database.movie.domain.MovieRepository;
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
class MovieDeleteServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private MovieDeleteService movieDeleteService;


    @Test
    @DisplayName("Should delete movie by given id")
    public void shouldDeleteMovieByGivenId() {
        //arrange
        var movieId = UUID.randomUUID();

        //act
        movieDeleteService.delete(movieId);

        //assert
        verify(movieRepository, times(1)).deleteById(movieId);
    }
}