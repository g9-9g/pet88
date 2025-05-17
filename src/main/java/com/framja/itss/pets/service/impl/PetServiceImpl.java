package com.framja.itss.pets.service.impl;

import com.framja.itss.common.service.UserQueryService;
import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.users.entity.User;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.pets.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserQueryService userService;

    @Override
    @Transactional
    public PetDto createPet(PetDto petDto) {
        System.out.println("Creating pet: " + petDto);
        User owner = userService.getUserById(petDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Pet pet = convertToEntity(petDto, petDto.getOwnerId());
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
    public Pet convertToEntity(PetDto petDto, Long ownerId) {
        User owner = userService.getUserById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
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
    
    @Override
    public Page<PetDto> getFilteredPetsWithPagination(String name, String species, String breed,
                                             String gender, Long ownerId,
                                             String sortField, String sortDir,
                                             int page, int size) {
        // Default sort by name ascending if not specified
        if (sortField == null || sortField.isEmpty()) {
            sortField = "name";
        }
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        
        // Tạo Pageable để hỗ trợ phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Gọi repository với phân trang
        Page<Pet> petPage = petRepository.findPetsWithFiltersAndPagination(
                name, species, breed, gender, ownerId, pageable);
        
        // Convert entity page sang DTO page
        return petPage.map(this::convertToDto);
    }

    @Override
    public List<PetDto> getPetsWithCompletedAppointments(Long ownerId) {
        return petRepository.findPetsWithCompletedAppointments(ownerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
} 