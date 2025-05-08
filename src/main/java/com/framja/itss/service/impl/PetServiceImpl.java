package com.framja.itss.service.impl;

import com.framja.itss.dto.pet.PetDto;
import com.framja.itss.entity.Pet;
import com.framja.itss.entity.User;
import com.framja.itss.repository.PetRepository;
import com.framja.itss.repository.UserRepository;
import com.framja.itss.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PetDto createPet(PetDto petDto) {
        User owner = userRepository.findById(petDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Pet pet = convertToEntity(petDto, owner);
        Pet saved = petRepository.save(pet);
        return convertToDto(saved);
    }

    @Override
    public Optional<PetDto> getPetById(Long petId) {
        return petRepository.findById(petId).map(this::convertToDto);
    }

    @Override
    public List<PetDto> getAllPets() {
        return petRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PetDto> getPetsByOwnerId(Long ownerId) {
        return petRepository.findByOwner_Id(ownerId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PetDto updatePet(Long petId, PetDto petDto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        pet.setName(petDto.getName());
        pet.setSpecies(petDto.getSpecies());
        pet.setBreed(petDto.getBreed());
        pet.setGender(petDto.getGender());
        pet.setBirthdate(petDto.getBirthdate());
        pet.setColor(petDto.getColor());
        pet.setAvatarUrl(petDto.getAvatarUrl());
        pet.setHealthNotes(petDto.getHealthNotes());
        pet.setVaccinationHistory(petDto.getVaccinationHistory());
        pet.setNutritionNotes(petDto.getNutritionNotes());
        Pet updated = petRepository.save(pet);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    @Override
    public PetDto convertToDto(Pet pet) {
        return PetDto.builder()
                .petId(pet.getPetId())
                .ownerId(pet.getOwner() != null ? pet.getOwner().getId() : null)
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .birthdate(pet.getBirthdate())
                .color(pet.getColor())
                .avatarUrl(pet.getAvatarUrl())
                .healthNotes(pet.getHealthNotes())
                .vaccinationHistory(pet.getVaccinationHistory())
                .nutritionNotes(pet.getNutritionNotes())
                .createdAt(pet.getCreatedAt())
                .updatedAt(pet.getUpdatedAt())
                .build();
    }

    @Override
    public Pet convertToEntity(PetDto petDto, User owner) {
        return Pet.builder()
                .petId(petDto.getPetId())
                .owner(owner)
                .name(petDto.getName())
                .species(petDto.getSpecies())
                .breed(petDto.getBreed())
                .gender(petDto.getGender())
                .birthdate(petDto.getBirthdate())
                .color(petDto.getColor())
                .avatarUrl(petDto.getAvatarUrl())
                .healthNotes(petDto.getHealthNotes())
                .vaccinationHistory(petDto.getVaccinationHistory())
                .nutritionNotes(petDto.getNutritionNotes())
                .createdAt(petDto.getCreatedAt())
                .updatedAt(petDto.getUpdatedAt())
                .build();
    }

    @Override
    public List<PetDto> getFilteredPets(String name, String species, String breed, 
                                        String gender, Long ownerId, 
                                        String sortField, String sortDir) {
        // Default sort by name ascending if not specified
        if (sortField == null || sortField.isEmpty()) {
            sortField = "name";
        }
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        
        // Use JPQL query for filtering and sorting
        List<Pet> pets = petRepository.findPetsWithFiltersAndSort(
                name, species, breed, gender, ownerId, sortField, sortDir);
        
        // Convert to DTOs
        return pets.stream().map(this::convertToDto).collect(Collectors.toList());
    }
} 