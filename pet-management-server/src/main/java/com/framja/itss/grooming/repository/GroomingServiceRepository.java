package com.framja.itss.grooming.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.framja.itss.grooming.entity.GroomingService;

public interface GroomingServiceRepository extends JpaRepository<GroomingService, Long> {
    
    List<GroomingService> findByIsActiveTrue();
    
    @Query("SELECT gs FROM GroomingService gs WHERE " +
           "(:name IS NULL OR LOWER(gs.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:minPrice IS NULL OR gs.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR gs.price <= :maxPrice) AND " +
           "(:isActive IS NULL OR gs.isActive = :isActive)")
    Page<GroomingService> findWithFilters(
            String name, 
            Double minPrice, 
            Double maxPrice, 
            Boolean isActive,
            Pageable pageable);
} 