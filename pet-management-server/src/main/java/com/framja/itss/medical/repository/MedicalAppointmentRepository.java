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

    List<MedicalAppointment> findByPetOwnerId(Long ownerId);
    
    @Query("SELECT COUNT(a) FROM MedicalAppointment a WHERE a.pet.owner.id = :ownerId")
    Long countByPetOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT a.status as status, COUNT(a) as count FROM MedicalAppointment a " +
           "WHERE a.pet.owner.id = :ownerId GROUP BY a.status")
    List<Object[]> countByStatusAndPetOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(a) FROM MedicalAppointment a WHERE a.doctor.id = :doctorId")
    Long countByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT a.status as status, COUNT(a) as count FROM MedicalAppointment a " +
           "WHERE a.doctor.id = :doctorId GROUP BY a.status")
    List<Object[]> countByStatusAndDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT a.status as status, COUNT(a) as count FROM MedicalAppointment a " +
           "GROUP BY a.status")
    List<Object[]> countByStatusAll();
} 