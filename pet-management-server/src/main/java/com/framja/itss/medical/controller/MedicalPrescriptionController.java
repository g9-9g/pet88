package com.framja.itss.medical.controller;

import com.framja.itss.medical.dto.medicine.PrescriptionDto;
import com.framja.itss.medical.entity.Medicine;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.entity.Prescription;
import com.framja.itss.medical.service.MedicineService;
import com.framja.itss.medical.service.PrescriptionService;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.dto.medicine.MedicineDto;
import com.framja.itss.medical.dto.medicine.CreateMedicineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medical/prescriptions")
public class MedicalPrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private MedicalAppointmentRepository appointmentRepository;

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByAppointment(@PathVariable Long appointmentId) {
        List<PrescriptionDto> dtos = prescriptionService.findByAppointmentId(appointmentId).stream()
                .map(prescription -> prescriptionService.convertToDto(prescription))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/appointment/{appointmentId}")
    public ResponseEntity<PrescriptionDto> addPrescription(@PathVariable Long appointmentId, @RequestBody PrescriptionDto dto) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        Medicine medicine = medicineService.findById(dto.getMedicineId());
        Prescription prescription = Prescription.builder()
                .appointment(appointment)
                .medicine(medicine)
                .quantity(dto.getQuantity())
                .usageInstructions(dto.getUsageInstructions())
                .build();
        Prescription saved = prescriptionService.save(prescription);
        return ResponseEntity.ok(prescriptionService.convertToDto(saved));
    }

    @PostMapping("/appointment/{appointmentId}/batch")
    public ResponseEntity<List<PrescriptionDto>> addPrescriptions(@PathVariable Long appointmentId, @RequestBody List<PrescriptionDto> dtos) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        List<PrescriptionDto> result = dtos.stream().map(dto -> {
            Medicine medicine = medicineService.findById(dto.getMedicineId());
            Prescription prescription = Prescription.builder()
                    .appointment(appointment)
                    .medicine(medicine)
                    .quantity(dto.getQuantity())
                    .usageInstructions(dto.getUsageInstructions())
                    .build();
            return prescriptionService.convertToDto(prescriptionService.save(prescription));
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/medicine")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MedicineDto> createMedicine(@RequestBody CreateMedicineRequest medicineRequest) {
        MedicineDto saved = medicineService.save(medicineRequest);
        return ResponseEntity.ok(saved);
    }
} 