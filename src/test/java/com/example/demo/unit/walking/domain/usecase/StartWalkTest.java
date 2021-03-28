package com.example.demo.unit.walking.domain.usecase;

import com.example.demo.util.TestFactory;
import com.example.demo.walking.application.usecases.AcceptWalking;
import com.example.demo.walking.application.usecases.StartWalking;
import com.example.demo.walking.common.EntityNotFoundException;
import com.example.demo.walking.common.PermissionDeniedException;
import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.WalkingStatus;
import com.example.demo.walking.domain.usecase.AssignCaregiverToWalkingUseCase;
import com.example.demo.walking.domain.usecase.StartWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class StartWalkTest {

    @DisplayName("Start walk should fill start date")
    @Test
    public void testStartWalk() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        var caregiver = mock(Caregiver.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        walkingSpy.acceptWalk(caregiver);

        StartWalkingUseCase startWalking = new StartWalking(walkingRepository);
        startWalking.startWalk(caregiver, 1L);

        verify(walkingRepository, times(1)).findById(eq(1L));
        assertEquals(WalkingStatus.ACCEPTED, walkingSpy.getStatus());
        assertNotNull(walkingSpy.getStartDate());
        assertNull(walkingSpy.getFinishDate());
    }

    @DisplayName("Try to start nonexistent walking should throw an exception")
    @Test
    public void testNotFoundWalking() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        StartWalkingUseCase startWalking = new StartWalking(walkingRepository);
        var caregiver = mock(Caregiver.class);
        var ex1 =
                assertThrows(
                        EntityNotFoundException.class, () -> startWalking.startWalk(caregiver, 2L));
        verify(walkingRepository, times(1)).findById(eq(2L));
        assertEquals("Walking not found, was not possible to start the walking", ex1.getMessage());
    }

    @DisplayName("Try to start walking with null caregiver should throw an exception")
    @Test
    public void testStartWalkingNullCaregiver() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        StartWalkingUseCase startWalking = new StartWalking(walkingRepository);
        var caregiver = mock(Caregiver.class);
        var ex1 =
                assertThrows(
                        PermissionDeniedException.class,
                        () -> startWalking.startWalk(caregiver, 1L));
        verify(walkingRepository, times(1)).findById(eq(1L));
        assertEquals("You are not the responsible for this walking", ex1.getMessage());
    }

    @DisplayName(
            "Try to start a walking with different caregiver than the one previously accepted should throw an exception")
    @Test
    public void testStartWalkingMismatchCaregiver() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        StartWalkingUseCase startWalking = new StartWalking(walkingRepository);
        var caregiver = mock(Caregiver.class);
        walkingSpy.acceptWalk(caregiver);
        var caregiver2 = mock(Caregiver.class);
        var ex1 =
                assertThrows(
                        PermissionDeniedException.class,
                        () -> startWalking.startWalk(caregiver2, 1L));
        verify(walkingRepository, times(1)).findById(eq(1L));
        assertEquals("You are not the responsible for this walking", ex1.getMessage());
    }
}
