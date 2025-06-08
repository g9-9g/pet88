package com.framja.itss.grooming.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.framja.itss.grooming.dto.GroomingServiceDto;

public interface GroomingServiceService {
    
    GroomingServiceDto createService(GroomingServiceDto serviceDto);
    
    Optional<GroomingServiceDto> getServiceById(Long id);
    
    List<GroomingServiceDto> getAllServices();
    
    List<GroomingServiceDto> getActiveServices();
    
    GroomingServiceDto updateService(Long id, GroomingServiceDto serviceDto);
    
    void deleteService(Long id);
    
    Page<GroomingServiceDto> getFilteredServicesWithPagination(
            String name,
            Double minPrice,
            Double maxPrice,
            Boolean isActive,
            String sortBy,
            String sortDir,
            int page,
            int size);
            
    Long getServiceCountsAll();
} 