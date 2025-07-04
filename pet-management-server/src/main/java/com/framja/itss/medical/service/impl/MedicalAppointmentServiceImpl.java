package com.framja.itss.medical.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.exception.ResourceNotFoundException;
import com.framja.itss.medical.dto.appointment.CreateAppointmentDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDetailDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDto;
import com.framja.itss.medical.dto.appointment.UpdateAppointmentDto;
import com.framja.itss.medical.dto.medicine.PrescriptionDto;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.service.MedicalAppointmentService;
import com.framja.itss.medical.service.PrescriptionService;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;
import com.framja.itss.common.dto.CountDTO;

import java.util.HashMap;

@Service
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

    @Autowired
    private MedicalAppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @Override
    public MedicalAppointmentDto getAppointmentById(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        return convertToDto(appointment);
    }

    @Override
    public List<MedicalAppointmentDto> getAppointmentsByOwnerId(Long ownerId, AppointmentStatus status) {
        List<MedicalAppointment> appointments;
        if (status != null) {
            appointments = appointmentRepository.findByOwnerIdAndStatus(ownerId, status);
        } else {
            appointments = appointmentRepository.findByOwnerId(ownerId);
        }
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getAppointmentsByDoctorId(Long doctorId, AppointmentStatus status) {
        List<MedicalAppointment> appointments;
        if (status != null) {
            appointments = appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
        } else {
            appointments = appointmentRepository.findByDoctorId(doctorId);
        }
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getActiveAppointments() {
        List<MedicalAppointment> appointments = appointmentRepository.findByStatusIn(
            Arrays.asList(AppointmentStatus.SCHEDULED, AppointmentStatus.FOLLOW_UP)
        );
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getAllAppointments(AppointmentStatus status) {
        List<MedicalAppointment> appointments;
        if (status != null) {
            appointments = appointmentRepository.findByStatus(status);
        } else {
            appointments = appointmentRepository.findAll();
        }
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalAppointmentDto updateAppointment(Long appointmentId, UpdateAppointmentDto updateDto) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        if (updateDto.getDiagnosis() != null) {
            appointment.setDiagnosis(updateDto.getDiagnosis());
        }
        
        if (updateDto.getTreatment() != null) {
            appointment.setTreatment(updateDto.getTreatment());
        }
        
        if (updateDto.getNotes() != null) {
            appointment.setNotes(updateDto.getNotes());
        }
        
        if (updateDto.getStatus() != null) {
            appointment.setStatus(updateDto.getStatus());
        }
        
        MedicalAppointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToDto(updatedAppointment);
    }

    @Override
    @Transactional
    public MedicalAppointmentDto createAppointment(CreateAppointmentDto createDto, Long doctorId) {
        MedicalAppointment appointment = new MedicalAppointment();
        Pet pet = petRepository.findById(createDto.getPetId())
            .orElseThrow(() -> new com.framja.itss.exception.ResourceNotFoundException("Pet not found with id: " + createDto.getPetId()));

        User doctor = userRepository.findById(doctorId)
            .orElseThrow(() -> new com.framja.itss.exception.ResourceNotFoundException("Doctor not found with id: " + doctorId));

        appointment.setDoctor(doctor);
        appointment.setRequest(null);
        appointment.setPet(pet);
        appointment.setOwner(pet.getOwner());
        appointment.setAppointmentDateTime(createDto.getAppointmentDateTime());
        appointment.setDiagnosis(createDto.getDiagnosis());
        appointment.setTreatment(createDto.getTreatment());
        appointment.setNotes(createDto.getNotes());
        appointment.setStatus(AppointmentStatus.FOLLOW_UP);
        MedicalAppointment saved = appointmentRepository.save(appointment);
        return convertToDto(saved);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MedicalAppointmentDetailDto getAppointmentDetailsById(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        
        List<PrescriptionDto> prescriptions = prescriptionService.findByAppointmentId(appointmentId).stream()
                .map(prescription -> PrescriptionDto.builder()
                        .id(prescription.getId())
                        .appointmentId(prescription.getAppointment().getId())
                        .medicineId(prescription.getMedicine().getId())
                        .medicineName(prescription.getMedicine().getName())
                        .quantity(prescription.getQuantity())
                        .usageInstructions(prescription.getUsageInstructions())
                        .build())
                .collect(Collectors.toList());

        return MedicalAppointmentDetailDto.builder()
                .id(appointment.getId())
                .requestId(appointment.getRequest() != null ? appointment.getRequest().getId() : null)
                .petId(appointment.getPet().getPetId())
                .petName(appointment.getPet().getName())
                .ownerId(appointment.getOwner().getId())
                .ownerName(appointment.getOwner().getUsername())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getUsername())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .symptoms(appointment.getSymptoms())
                .diagnosis(appointment.getDiagnosis())
                .treatment(appointment.getTreatment())
                .notes(appointment.getNotes())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .prescriptions(prescriptions)
                .build();
    }
    
    @Override
    public List<MedicalAppointmentDto> getAppointmentsByPetId(Long petId) {
        List<MedicalAppointment> appointments = appointmentRepository.findByPetId(petId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public CountDTO getAppointmentCountsByOwnerId(Long ownerId) {
        CountDTO countDTO = new CountDTO();
        
        // Get total count
        // Long total = appointmentRepository.countByPetOwnerId(ownerId);
        // countDTO.setTotal(total);
        
        // Get status counts
        List<Object[]> statusCounts = appointmentRepository.countByStatusAndPetOwnerId(ownerId);
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;

        for (Object[] statusCount : statusCounts) {
            String status = ((AppointmentStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }
    
    @Override
    public CountDTO getAppointmentCountsByDoctorId(Long doctorId) {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = appointmentRepository.countByStatusAndDoctorId(doctorId);
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((AppointmentStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }
    
    @Override
    public CountDTO getAppointmentCountsAll() {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = appointmentRepository.countByStatusAll();
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((AppointmentStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }
    
    private MedicalAppointmentDto convertToDto(MedicalAppointment appointment) {
        return MedicalAppointmentDto.builder()
                .id(appointment.getId())
                .requestId(appointment.getRequest() != null ? appointment.getRequest().getId() : null)
                .petId(appointment.getPet().getPetId())
                .petName(appointment.getPet().getName())
                .ownerId(appointment.getOwner().getId())
                .ownerName(appointment.getOwner().getUsername())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getUsername())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .symptoms(appointment.getSymptoms())
                .diagnosis(appointment.getDiagnosis())
                .treatment(appointment.getTreatment())
                .notes(appointment.getNotes())
                .status(appointment.getStatus())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
} 