package com.framja.itss.pets.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.service.PetService;
import com.framja.itss.users.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_PET_OWNER') and #petDto.ownerId == authentication.principal.id)")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto, @AuthenticationPrincipal User user) {
        PetDto created = petService.createPet(petDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PET_OWNER', 'STAFF', 'VET')")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        return petService.getPetById(id)
                .map(petDto -> new ResponseEntity<>(petDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'VET')")
    public ResponseEntity<List<PetDto>> getAllPets() {
        List<PetDto> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/owned")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<List<PetDto>> getPetsByOwner(@AuthenticationPrincipal User user) {
        List<PetDto> pets = petService.getPetsByOwnerId(user.getId());
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PET_OWNER') and @petAuthorizationService.isOwner(#id, authentication.principal.id))")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        PetDto updated = petService.updatePet(id, petDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_PET_OWNER') and @petAuthorizationService.isOwner(#id, authentication.principal.id))")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        Page<PetDto> petPage = petService.getFilteredPetsWithPagination(
                name, species, breed, gender, ownerId, sortBy, sortDir, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("pets", petPage.getContent());
        response.put("currentPage", petPage.getNumber());
        response.put("totalItems", petPage.getTotalElements());
        response.put("totalPages", petPage.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/completed-appointments")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_VET')")
    public ResponseEntity<List<PetDto>> getPetsWithCompletedAppointments(@RequestParam(value = "ownerId", required = false) Long ownerId) {
        List<PetDto> pets = petService.getPetsWithCompletedAppointments(ownerId);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/owned/count")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<Long> getPetsCountByOwner(@AuthenticationPrincipal User user) {
        Long count = petService.getPetsCountByOwnerId(user.getId());
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_VET')")
    public ResponseEntity<Long> getTotalPetsCount() {
        Long count = petService.getTotalPetsCount();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/search/count")
    public ResponseEntity<Long> getFilteredPetsCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long ownerId) {
        
        Long count = petService.getFilteredPetsCount(name, species, breed, gender, ownerId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
} 