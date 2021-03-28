package com.example.demo.unit.walking.domain.entity;

import com.example.demo.util.TestFactory;
import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.domain.entity.WalkingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
                                TestFactory.createWalking(
                                        LocalDateTime.now().plusDays(1L), "-", "-", 30, pets));
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
                                TestFactory.createWalking(
                                        LocalDateTime.now().minusDays(1L), "-", "-", 30, pets));
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
                                TestFactory.createWalking(
                                        LocalDateTime.now().plusDays(1L), "-", "-", 31, pets));
        var ex2 =
                assertThrows(
                        IllegalStateException.class,
                        () ->
                                TestFactory.createWalking(
                                        LocalDateTime.now().plusDays(1L), "-", "-", 61, pets));
        assertEquals("Duration must be 30 or 60 minutes", ex1.getMessage());
        assertEquals("Duration must be 30 or 60 minutes", ex2.getMessage());
    }

    @DisplayName("Test accept walk and change status state")
    @Test
    public void testAcceptWalking() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        assertEquals(walking.getStatus(), WalkingStatus.PENDING);
        var caregiver = mock(Caregiver.class);
        walking.acceptWalk(caregiver);
        assertEquals(walking.getStatus(), WalkingStatus.ACCEPTED);
    }

    @DisplayName("Test to try accept a already accepted walk or canceled should throw an exception")
    @Test
    public void testAcceptErrorInvalidState() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1L), "-", "-", 30, pets);
        var caregiver = mock(Caregiver.class);
        walking.acceptWalk(caregiver);
        var ex1 = assertThrows(IllegalStateException.class, () -> walking.acceptWalk(caregiver));
        assertEquals("This walk was already accepted or canceled", ex1.getMessage());
        assertEquals(WalkingStatus.ACCEPTED, walking.getStatus());
        walking.cancelWalk();
        var ex2 = assertThrows(IllegalStateException.class, () -> walking.acceptWalk(caregiver));
        assertEquals("This walk was already accepted or canceled", ex2.getMessage());
        assertEquals(WalkingStatus.CANCELED, walking.getStatus());
    }
}
