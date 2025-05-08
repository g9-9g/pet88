package com.framja.itss.service;

import com.framja.itss.dto.pet.PetDto;
import com.framja.itss.entity.Pet;
import com.framja.itss.entity.User;

import java.util.List;
import java.util.Optional;

public interface PetService {
    PetDto createPet(PetDto petDto);
    Optional<PetDto> getPetById(Long petId);
    List<PetDto> getAllPets();
    List<PetDto> getPetsByOwnerId(Long ownerId);
    PetDto updatePet(Long petId, PetDto petDto);
    void deletePet(Long petId);
    PetDto convertToDto(Pet pet);
    Pet convertToEntity(PetDto petDto, User owner);
    
    // New method for filtered, sorted, and searched pets
    List<PetDto> getFilteredPets(String name, String species, String breed, 
                                String gender, Long ownerId, 
                                String sortField, String sortDir);
} 