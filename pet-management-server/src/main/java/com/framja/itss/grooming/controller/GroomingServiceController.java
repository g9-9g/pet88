package com.framja.itss.grooming.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.grooming.dto.GroomingServiceDto;
import com.framja.itss.grooming.service.GroomingServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grooming/services")
@RequiredArgsConstructor
public class GroomingServiceController {

    private final GroomingServiceService groomingServiceService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GroomingServiceDto> createGroomingService(@RequestBody GroomingServiceDto serviceDto) {
        GroomingServiceDto created = groomingServiceService.createService(serviceDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<GroomingServiceDto> getGroomingServiceById(@PathVariable Long id) {
        return groomingServiceService.getServiceById(id)
                .map(serviceDto -> new ResponseEntity<>(serviceDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<List<GroomingServiceDto>> getAllGroomingServices() {
        List<GroomingServiceDto> services = groomingServiceService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<List<GroomingServiceDto>> getActiveGroomingServices() {
        List<GroomingServiceDto> services = groomingServiceService.getActiveServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<GroomingServiceDto> updateGroomingService(
            @PathVariable Long id, @RequestBody GroomingServiceDto serviceDto) {
        GroomingServiceDto updated = groomingServiceService.updateService(id, serviceDto);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGroomingService(@PathVariable Long id) {
        groomingServiceService.deleteService(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<?> searchGroomingServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        Page<GroomingServiceDto> servicePage = groomingServiceService.getFilteredServicesWithPagination(
                name, minPrice, maxPrice, isActive, sortBy, sortDir, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("services", servicePage.getContent());
        response.put("currentPage", servicePage.getNumber());
        response.put("totalItems", servicePage.getTotalElements());
        response.put("totalPages", servicePage.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 