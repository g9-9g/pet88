package com.framja.itss.pets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.entity.Pet;

public interface PetService {
    PetDto createPet(PetDto petDto);
    Optional<PetDto> getPetById(Long petId);
    List<PetDto> getAllPets();
    List<PetDto> getPetsByOwnerId(Long ownerId);
    PetDto updatePet(Long petId, PetDto petDto);
    void deletePet(Long petId);
    PetDto convertToDto(Pet pet);
    Pet convertToEntity(PetDto petDto, Long ownerId);
    
    // Method for filtered, sorted, and searched pets (without pagination)
    List<PetDto> getFilteredPets(String name, String species, String breed, 
                                String gender, Long ownerId, 
                                String sortField, String sortDir);
    
    // New method with pagination
    Page<PetDto> getFilteredPetsWithPagination(String name, String species, String breed,
                                      String gender, Long ownerId,
                                      String sortField, String sortDir,
                                      int page, int size);

    // Lấy danh sách Pet đã có appointment completed=true, có thể lọc theo ownerId
    List<PetDto> getPetsWithCompletedAppointments(Long ownerId);

    // Methods for counting pets
    Long getPetsCountByOwnerId(Long ownerId);
    Long getTotalPetsCount();
    Long getFilteredPetsCount(String name, String species, String breed, String gender, Long ownerId);
} 