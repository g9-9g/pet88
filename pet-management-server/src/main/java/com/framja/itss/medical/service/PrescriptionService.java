package com.framja.itss.medical.service;

import com.framja.itss.medical.entity.Prescription;
import com.framja.itss.medical.dto.medicine.PrescriptionDto;
import java.util.List;

public interface PrescriptionService {
    List<Prescription> findByAppointmentId(Long appointmentId);
    Prescription save(Prescription prescription);
    void deleteById(Long id);
    PrescriptionDto convertToDto(Prescription prescription);
} 