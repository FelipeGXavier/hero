package com.example.demo.unit.walking.domain.usecase;

import com.example.demo.util.TestFactory;
import com.example.demo.walking.application.usecases.AcceptWalking;
import com.example.demo.walking.common.EntityNotFoundException;
import com.example.demo.walking.domain.entity.Caregiver;
import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.WalkingStatus;
import com.example.demo.walking.domain.usecase.AssignCaregiverToWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AcceptWalkTest {

    @DisplayName("Accept an walk should call acceptWalk and change status")
    @Test
    public void testAcceptWalk() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        AssignCaregiverToWalkingUseCase acceptWalk = new AcceptWalking(walkingRepository);
        var caregiver = mock(Caregiver.class);
        acceptWalk.acceptWalking(caregiver, 1L);
        verify(walkingRepository, times(1)).findById(eq(1L));
        assertEquals(WalkingStatus.ACCEPTED, walkingSpy.getStatus());
    }

    @DisplayName("Try to accept nonexistent walking should throw an exception")
    @Test
    public void testRejectWalk() {
        var pets = Arrays.asList(mock(Pet.class), mock(Pet.class));
        var walking =
                TestFactory.createWalking(LocalDateTime.now().plusDays(1), "-", "-", 30, pets);
        var walkingSpy = spy(walking);
        var walkingRepository = mock(WalkingRepository.class);
        when(walkingRepository.findById(eq(1L))).thenReturn(Optional.of(walkingSpy));
        AssignCaregiverToWalkingUseCase acceptWalk = new AcceptWalking(walkingRepository);
        var caregiver = mock(Caregiver.class);
        var ex1 =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> acceptWalk.acceptWalking(caregiver, 2L));
        verify(walkingRepository, times(1)).findById(eq(2L));
        assertEquals("Walking not found, was not possible to accept this walk", ex1.getMessage());
    }
}
