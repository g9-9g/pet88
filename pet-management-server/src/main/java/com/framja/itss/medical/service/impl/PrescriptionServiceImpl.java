package com.framja.itss.medical.service.impl;

import com.framja.itss.medical.entity.Prescription;
import com.framja.itss.medical.repository.PrescriptionRepository;
import com.framja.itss.medical.service.PrescriptionService;
import com.framja.itss.medical.dto.medicine.PrescriptionDto;
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
    
    @Override
    public PrescriptionDto convertToDto(Prescription prescription) {
        return PrescriptionDto.builder()
                .id(prescription.getId())
                .appointmentId(prescription.getAppointment().getId())
                .medicineId(prescription.getMedicine().getId())
                .medicineName(prescription.getMedicine().getName())
                .quantity(prescription.getQuantity())
                .usageInstructions(prescription.getUsageInstructions())
                .build();
    }
} 