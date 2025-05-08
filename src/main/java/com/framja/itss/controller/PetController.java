package com.framja.itss.controller;

import com.framja.itss.dto.pet.PetDto;
import com.framja.itss.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        PetDto created = petService.createPet(petDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        return petService.getPetById(id)
                .map(petDto -> new ResponseEntity<>(petDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<PetDto>> getAllPets() {
        List<PetDto> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<PetDto>> getPetsByOwner(@PathVariable Long ownerId) {
        List<PetDto> pets = petService.getPetsByOwnerId(ownerId);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        PetDto updated = petService.updatePet(id, petDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PetDto>> searchPets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        
        List<PetDto> filteredPets = petService.getFilteredPets(
                name, species, breed, gender, ownerId, sortBy, sortDir);
        
        return new ResponseEntity<>(filteredPets, HttpStatus.OK);
    }
} 