package com.framja.itss.grooming.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.grooming.dto.GroomingServiceDto;
import com.framja.itss.grooming.entity.GroomingService;
import com.framja.itss.grooming.repository.GroomingServiceRepository;
import com.framja.itss.grooming.service.GroomingServiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroomingServiceServiceImpl implements GroomingServiceService {

    private final GroomingServiceRepository groomingServiceRepository;

    @Override
    @Transactional
    public GroomingServiceDto createService(GroomingServiceDto serviceDto) {
        GroomingService service = mapToEntity(serviceDto);
        service = groomingServiceRepository.save(service);
        return mapToDto(service);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroomingServiceDto> getServiceById(Long id) {
        return groomingServiceRepository.findById(id)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingServiceDto> getAllServices() {
        return groomingServiceRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingServiceDto> getActiveServices() {
        return groomingServiceRepository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroomingServiceDto updateService(Long id, GroomingServiceDto serviceDto) {
        GroomingService service = groomingServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grooming service not found with id: " + id));
        
        service.setName(serviceDto.getName());
        service.setDescription(serviceDto.getDescription());
        service.setPrice(serviceDto.getPrice());
        service.setDurationMinutes(serviceDto.getDurationMinutes());
        service.setIsActive(serviceDto.getIsActive());
        service.setImageUrl(serviceDto.getImageUrl());
        
        service = groomingServiceRepository.save(service);
        return mapToDto(service);
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        groomingServiceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GroomingServiceDto> getFilteredServicesWithPagination(
            String name, Double minPrice, Double maxPrice, Boolean isActive,
            String sortBy, String sortDir, int page, int size) {

        if (!("name".equalsIgnoreCase(sortBy) || "price".equalsIgnoreCase(sortBy) || "duration".equalsIgnoreCase(sortBy))) {
            throw new IllegalArgumentException("Invalid sortBy value. Allowed values are: name, price, duration.");
        }
        if (!("asc".equalsIgnoreCase(sortDir) || "desc".equalsIgnoreCase(sortDir))) {
            throw new IllegalArgumentException("Invalid sortDir value. Allowed values are: asc, desc.");
        }

        if ("duration".equalsIgnoreCase(sortBy)) {
                sortBy = "durationMinutes";
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
                Sort.by(sortBy).ascending() : 
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<GroomingService> servicePage = groomingServiceRepository.findWithFilters(
                name, minPrice, maxPrice, isActive, pageable);
        
        return servicePage.map(this::mapToDto);
    }
    
    private GroomingService mapToEntity(GroomingServiceDto dto) {
        return GroomingService.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .durationMinutes(dto.getDurationMinutes())
                .isActive(dto.getIsActive())
                .imageUrl(dto.getImageUrl())
                .build();
    }
    
    private GroomingServiceDto mapToDto(GroomingService entity) {
        return GroomingServiceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .durationMinutes(entity.getDurationMinutes())
                .isActive(entity.getIsActive())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 