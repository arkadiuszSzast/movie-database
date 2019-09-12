package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActorCreateServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorCreateService actorCreateService;

    @Test
    @DisplayName("Should return saved actor")
    public void shouldReturnSavedActor() {
        //arrange
        var actor = new Actor("name", "surname");
        when(actorRepository.save(actor)).thenReturn(actor);

        //act
        var result = actorCreateService.create(actor);

        //assert
        assertThat(result).isEqualToComparingFieldByField(actor);
    }
}