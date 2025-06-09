package com.framja.itss.pets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.framja.itss.pets.dto.PetDto;

@ExtendWith(MockitoExtension.class)
class PetAuthorizationServiceTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetAuthorizationService petAuthorizationService;

    private PetDto petDto;

    @BeforeEach
    void setUp() {
        petDto = new PetDto();
        petDto.setId(1L);
        petDto.setOwnerId(1L);
    }

    @Test
    @DisplayName("Should return true when user is the owner of the pet")
    void isOwner_WhenUserIsOwner_ReturnsTrue() {
        when(petService.getPetById(1L)).thenReturn(Optional.of(petDto));

        boolean result = petAuthorizationService.isOwner(1L, 1L);

        assertTrue(result);
        verify(petService).getPetById(1L);
    }

    @Test
    @DisplayName("Should return false when user is not the owner of the pet")
    void isOwner_WhenUserIsNotOwner_ReturnsFalse() {
        when(petService.getPetById(1L)).thenReturn(Optional.of(petDto));

        boolean result = petAuthorizationService.isOwner(1L, 2L);

        assertFalse(result);
        verify(petService).getPetById(1L);
    }

    @Test
    @DisplayName("Should return false when pet does not exist")
    void isOwner_WhenPetDoesNotExist_ReturnsFalse() {
        when(petService.getPetById(1L)).thenReturn(Optional.empty());

        boolean result = petAuthorizationService.isOwner(1L, 1L);

        assertFalse(result);
        verify(petService).getPetById(1L);
    }
} 