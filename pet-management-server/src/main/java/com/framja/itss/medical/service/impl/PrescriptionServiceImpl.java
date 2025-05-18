package com.framja.itss.medical.service.impl;

import com.framja.itss.medical.entity.Prescription;
import com.framja.itss.medical.repository.PrescriptionRepository;
import com.framja.itss.medical.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Override
    public List<Prescription> findByAppointmentId(Long appointmentId) {
        return prescriptionRepository.findAll().stream()
            .filter(p -> p.getAppointment().getId().equals(appointmentId))
            .toList();
    }

    @Override
    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public void deleteById(Long id) {
        prescriptionRepository.deleteById(id);
    }
} 