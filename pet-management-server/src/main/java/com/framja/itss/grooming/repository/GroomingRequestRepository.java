package com.framja.itss.grooming.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.framja.itss.grooming.entity.GroomingRequest;
import com.framja.itss.common.enums.GroomingRequestStatus;

public interface GroomingRequestRepository extends JpaRepository<GroomingRequest, Long> {
    
    List<GroomingRequest> findByOwnerId(Long ownerId);
    
    @Query("SELECT gr FROM GroomingRequest gr WHERE gr.pet.petId = :petId")
    List<GroomingRequest> findByPetId(Long petId);
    
    List<GroomingRequest> findByStaffId(Long staffId);
    
    List<GroomingRequest> findByStatus(GroomingRequestStatus status);
    
    Page<GroomingRequest> findByOwnerId(Long ownerId, Pageable pageable);
    
    @Query("SELECT gr FROM GroomingRequest gr WHERE " +
           "(:ownerId IS NULL OR gr.owner.id = :ownerId) AND " +
           "(:petId IS NULL OR gr.pet.petId = :petId) AND " +
           "(:serviceId IS NULL OR gr.service.id = :serviceId) AND " +
           "(:staffId IS NULL OR gr.staff.id = :staffId) AND " +
           "(:status IS NULL OR gr.status = :status) AND " +
           "(:startDate IS NULL OR gr.requestedDateTime >= :startDate) AND " +
           "(:endDate IS NULL OR gr.requestedDateTime <= :endDate)")
    Page<GroomingRequest> findWithFilters(
            Long ownerId,
            Long petId,
            Long serviceId,
            Long staffId,
            GroomingRequestStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);
} 