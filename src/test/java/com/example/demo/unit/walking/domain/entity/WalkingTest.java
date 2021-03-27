package com.example.demo.unit.walking.domain.entity;

import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.Walking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class WalkingTest {

    @DisplayName("Test calculated price from walking")
    @Test
    public void testPrice() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var onePetList = Collections.singletonList(mock(Pet.class));
        var builder =
                new Walking.WalkingBuilder()
                        .setScheduledDate(LocalDateTime.now().plusDays(3L))
                        .setLongitude("-")
                        .setLatitude("-");
        var thirtyMinutesWalkingWithoutAdditional =
                builder.setDuration(30).setPets(onePetList).build();
        var thirtyMinutesWalkingWithAdditional = builder.setDuration(30).setPets(pets).build();
        var sixtyMinutesWalkingWithoutAdditional =
                builder.setDuration(60).setPets(onePetList).build();
        var sixtyMinutesWalkingWithAdditional = builder.setDuration(60).setPets(pets).build();
        assertEquals(25.0, thirtyMinutesWalkingWithoutAdditional.getPrice());
        assertEquals(40.0, thirtyMinutesWalkingWithAdditional.getPrice());
        assertEquals(35.0, sixtyMinutesWalkingWithoutAdditional.getPrice());
        assertEquals(55.0, sixtyMinutesWalkingWithAdditional.getPrice());
    }

    @DisplayName("Create walking without pets should throw an exception")
    @Test
    public void testWalkWithoutPets() {
        List<Pet> pets = new ArrayList<>();
        var ex =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                new Walking.WalkingBuilder()
                                        .setScheduledDate(LocalDateTime.now().plusDays(3L))
                                        .setLongitude("-")
                                        .setLatitude("-")
                                        .setDuration(30)
                                        .setPets(pets)
                                        .build());
        assertEquals("Walking must have at least one pet", ex.getMessage());
    }

    @DisplayName("Create walking with scheduled date in paste should throw and execption")
    @Test
    public void testWalkingInvalidScheduleDate() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var ex =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                new Walking.WalkingBuilder()
                                        .setScheduledDate(LocalDateTime.now().minusDays(1L))
                                        .setLongitude("-")
                                        .setLatitude("-")
                                        .setDuration(30)
                                        .setPets(pets)
                                        .build());
        assertEquals("Scheduled date must be greater than now", ex.getMessage());
    }

    @DisplayName("Create walking outside of duration range should throw an exception")
    @Test
    public void testDurationRange() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var ex1 =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                new Walking.WalkingBuilder()
                                        .setScheduledDate(LocalDateTime.now().plusDays(1L))
                                        .setLongitude("-")
                                        .setLatitude("-")
                                        .setDuration(31)
                                        .setPets(pets)
                                        .build());
        var ex2 =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                new Walking.WalkingBuilder()
                                        .setScheduledDate(LocalDateTime.now().plusDays(1L))
                                        .setLongitude("-")
                                        .setLatitude("-")
                                        .setDuration(61)
                                        .setPets(pets)
                                        .build());
        assertEquals("Duration must be 30 or 60 minutes", ex1.getMessage());
        assertEquals("Duration must be 30 or 60 minutes", ex2.getMessage());
    }
}
