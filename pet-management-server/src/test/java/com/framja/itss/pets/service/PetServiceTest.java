package com.framja.itss.pets.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

    private PetDto petDto;
    private Pet pet;

    @BeforeEach
    void setUp() {
        // Initialize test data
        petDto = new PetDto();
        petDto.setId(1L);
        petDto.setName("Test Pet");
        petDto.setSpecies("Dog");
        petDto.setBreed("Labrador");
        petDto.setGender("Male");
        petDto.setOwnerId(1L);

        pet = new Pet();
        pet.setId(1L);
        pet.setName("Test Pet");
        pet.setSpecies("Dog");
        pet.setBreed("Labrador");
        pet.setGender("Male");
        pet.setOwnerId(1L);
    }

    @Test
    @DisplayName("Should create pet successfully")
    void createPet_Success() {
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        PetDto result = petService.createPet(petDto);

        assertNotNull(result);
        assertEquals(petDto.getName(), result.getName());
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    @DisplayName("Should get pet by id successfully")
    void getPetById_Success() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Optional<PetDto> result = petService.getPetById(1L);

        assertTrue(result.isPresent());
        assertEquals(petDto.getName(), result.get().getName());
    }

    @Test
    @DisplayName("Should get all pets successfully")
    void getAllPets_Success() {
        when(petRepository.findAll()).thenReturn(Arrays.asList(pet));

        List<PetDto> result = petService.getAllPets();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(petDto.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should get pets by owner id successfully")
    void getPetsByOwnerId_Success() {
        when(petRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(pet));

        List<PetDto> result = petService.getPetsByOwnerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(petDto.getName(), result.get(0).getName());
    }

    @Test
    @DisplayName("Should update pet successfully")
    void updatePet_Success() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        PetDto updatedDto = new PetDto();
        updatedDto.setName("Updated Pet");
        PetDto result = petService.updatePet(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Updated Pet", result.getName());
    }

    @Test
    @DisplayName("Should delete pet successfully")
    void deletePet_Success() {
        doNothing().when(petRepository).deleteById(1L);

        petService.deletePet(1L);

        verify(petRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should get filtered pets successfully")
    void getFilteredPets_Success() {
        when(petRepository.findByFilters(any(), any(), any(), any(), any(), any()))
            .thenReturn(Arrays.asList(pet));

        List<PetDto> result = petService.getFilteredPets(
            "Test", "Dog", "Labrador", "Male", 1L, "name", "asc"
        );

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get filtered pets with pagination successfully")
    void getFilteredPetsWithPagination_Success() {
        Page<Pet> petPage = new PageImpl<>(Arrays.asList(pet));
        when(petRepository.findByFiltersWithPagination(
            any(), any(), any(), any(), any(), any(), any(PageRequest.class)
        )).thenReturn(petPage);

        Page<PetDto> result = petService.getFilteredPetsWithPagination(
            "Test", "Dog", "Labrador", "Male", 1L, "name", "asc", 0, 10
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Should get pets with completed appointments successfully")
    void getPetsWithCompletedAppointments_Success() {
        when(petRepository.findPetsWithCompletedAppointments(any())).thenReturn(Arrays.asList(pet));

        List<PetDto> result = petService.getPetsWithCompletedAppointments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should get pets count by owner id successfully")
    void getPetsCountByOwnerId_Success() {
        when(petRepository.countByOwnerId(1L)).thenReturn(5L);

        Long result = petService.getPetsCountByOwnerId(1L);

        assertEquals(5L, result);
    }

    @Test
    @DisplayName("Should get total pets count successfully")
    void getTotalPetsCount_Success() {
        when(petRepository.count()).thenReturn(10L);

        Long result = petService.getTotalPetsCount();

        assertEquals(10L, result);
    }

    @Test
    @DisplayName("Should get filtered pets count successfully")
    void getFilteredPetsCount_Success() {
        when(petRepository.countByFilters(any(), any(), any(), any(), any())).thenReturn(3L);

        Long result = petService.getFilteredPetsCount(
            "Test", "Dog", "Labrador", "Male", 1L
        );

        assertEquals(3L, result);
    }
} 