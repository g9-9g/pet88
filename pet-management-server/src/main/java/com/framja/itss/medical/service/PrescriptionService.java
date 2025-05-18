package com.framja.itss.medical.service;

import com.framja.itss.medical.entity.Prescription;
import java.util.List;

public interface PrescriptionService {
    List<Prescription> findByAppointmentId(Long appointmentId);
    Prescription save(Prescription prescription);
    void deleteById(Long id);
} 