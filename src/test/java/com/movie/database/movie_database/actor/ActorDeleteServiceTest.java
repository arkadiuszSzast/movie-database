package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.ActorRepository;
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
class ActorDeleteServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorDeleteService actorDeleteService;

    @Test
    @DisplayName("Should delete actor")
    public void shouldDeleteActor() {
        //arrange
        var actorId = UUID.randomUUID();

        //act
        actorDeleteService.delete(actorId);

        //assert
        verify(actorRepository, times(1)).deleteById(actorId);
    }
}