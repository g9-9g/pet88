package com.framja.itss.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.framja.itss.common.enums.Status;
import com.framja.itss.medical.entity.MedicalRequest;

@Repository
public interface MedicalRequestRepository extends JpaRepository<MedicalRequest, Long> {
    List<MedicalRequest> findByOwnerId(Long ownerId);
    List<MedicalRequest> findByStatus(Status status);
    List<MedicalRequest> findByDoctorId(Long doctorId);
} 