package com.framja.itss.medical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.medical.entity.MedicalAppointment;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, Long> {
    List<MedicalAppointment> findByOwnerId(Long ownerId);
    List<MedicalAppointment> findByDoctorId(Long doctorId);
    List<MedicalAppointment> findByOwnerIdAndStatus(Long ownerId, AppointmentStatus status);
    List<MedicalAppointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    @Query("SELECT a FROM MedicalAppointment a WHERE a.pet.petId = :petId")
    List<MedicalAppointment> findByPetId(@Param("petId") Long petId);

    List<MedicalAppointment> findByStatus(AppointmentStatus status);
    
    List<MedicalAppointment> findByStatusIn(List<AppointmentStatus> statuses);
} 