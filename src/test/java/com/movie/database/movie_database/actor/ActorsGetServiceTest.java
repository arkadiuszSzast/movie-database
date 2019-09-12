package com.movie.database.movie_database.actor;

import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
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
class ActorsGetServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorsGetService actorsGetService;

    @Test
    @DisplayName("Should return list of actors")
    public void shouldReturnListOfActors() {
        //arrange
        var actor = new Actor("name", "surname");
        var secondActor = new Actor("second_name", "second_surname");
        var thirdActor = new Actor("third_name", "third_surname");
        var actors = List.of(actor, secondActor, thirdActor);
        when(actorRepository.findAll()).thenReturn(actors);

        //act
        var result = actorsGetService.getAll();

        //assert
        assertThat(result).containsAll(actors);
    }

    @Test
    @DisplayName("Should return empty list when actors not found in database")
    public void shouldReturnEmptyListWhenActorsNotFound() {
        //arrange
        when(actorRepository.findAll()).thenReturn(Lists.emptyList());

        //act
        var result = actorsGetService.getAll();

        //assert
        assertThat(result).isEmpty();
    }
}