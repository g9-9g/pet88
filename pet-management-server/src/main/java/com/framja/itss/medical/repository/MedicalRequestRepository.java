package com.framja.itss.medical.repository;

import java.util.List;

import com.framja.itss.common.enums.MedicalRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.framja.itss.medical.entity.MedicalRequest;

@Repository
public interface MedicalRequestRepository extends JpaRepository<MedicalRequest, Long> {
    List<MedicalRequest> findByOwnerId(Long ownerId);
    List<MedicalRequest> findByStatus(MedicalRequestStatus status);
    
    @Query("SELECT COUNT(r) FROM MedicalRequest r WHERE r.owner.id = :ownerId")
    Long countByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT r.status as status, COUNT(r) as count FROM MedicalRequest r " +
           "WHERE r.owner.id = :ownerId GROUP BY r.status")
    List<Object[]> countByStatusAndOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT r.status as status, COUNT(r) as count FROM MedicalRequest r " +
           "GROUP BY r.status")
    List<Object[]> countByStatusAll();
} 