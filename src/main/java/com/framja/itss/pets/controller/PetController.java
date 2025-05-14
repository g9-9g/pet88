package com.framja.itss.pets.controller;

import com.framja.itss.common.service.UserQueryService;
import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private final UserQueryService userService; // Inject the UserQueryService

    @PostMapping
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getCurrentUserId(authentication);
        
        // PET_ADMIN có thể tạo pet cho bất kỳ ai
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        // PET_OWNER chỉ có thể tạo pet cho chính mình
        if (!isAdmin && !currentUserId.equals(petDto.getOwnerId())) {
            throw new AccessDeniedException("You are not authorized to create a pet for another user");
        }
        
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

    @GetMapping("/owned")
    public ResponseEntity<List<PetDto>> getPetsByOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getCurrentUserId(authentication);

        List<PetDto> pets = petService.getPetsByOwnerId(currentUserId);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        // Kiểm tra pet có tồn tại không
        PetDto existingPet = petService.getPetById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        
        // Kiểm tra quyền
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getCurrentUserId(authentication);
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        // PET_OWNER chỉ có thể cập nhật pet của mình
        if (!isAdmin && !currentUserId.equals(existingPet.getOwnerId())) {
            throw new AccessDeniedException("You are not authorized to update this pet");
        }
        
        PetDto updated = petService.updatePet(id, petDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        // Kiểm tra pet có tồn tại không
        PetDto existingPet = petService.getPetById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        
        // Kiểm tra quyền
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = getCurrentUserId(authentication);
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        // PET_OWNER chỉ có thể xóa pet của mình
        if (!isAdmin && !currentUserId.equals(existingPet.getOwnerId())) {
            throw new AccessDeniedException("You are not authorized to delete this pet");
        }
        
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        
        // Gọi service với tham số phân trang
        Page<PetDto> petPage = petService.getFilteredPetsWithPagination(
                name, species, breed, gender, ownerId, sortBy, sortDir, page, size);
        
        // Trả về kết quả bao gồm cả metadata phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("pets", petPage.getContent());
        response.put("currentPage", petPage.getNumber());
        response.put("totalItems", petPage.getTotalElements());
        response.put("totalPages", petPage.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Helper method to get current user ID from Authentication
    private Long getCurrentUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        
        // Get user ID from principal
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            // Get username from principal
            String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            // Use userService to get the user by username and extract ID
            return userService.getUserByUsername(username)
                    .orElseThrow(() -> new AccessDeniedException("User not found"))
                    .getId();
        }
        
        throw new AccessDeniedException("Invalid authentication principal");
    }
} 