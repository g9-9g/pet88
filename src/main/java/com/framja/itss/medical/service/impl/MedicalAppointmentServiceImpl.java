package com.framja.itss.medical.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.exception.ResourceNotFoundException;
import com.framja.itss.medical.dto.MedicalAppointmentDto;
import com.framja.itss.medical.dto.UpdateAppointmentDto;
import com.framja.itss.medical.dto.CreateAppointmentDto;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.service.MedicalAppointmentService;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;
import com.framja.itss.common.enums.RoleName;

@Service
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

    @Autowired
    private MedicalAppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MedicalAppointmentDto getAppointmentById(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        return convertToDto(appointment);
    }

    @Override
    public List<MedicalAppointmentDto> getAppointmentsByOwnerId(Long ownerId) {
        List<MedicalAppointment> appointments = appointmentRepository.findByOwnerId(ownerId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        List<MedicalAppointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getActiveAppointments() {
        List<MedicalAppointment> appointments = appointmentRepository.findByCompletedFalseAndCancelledFalse();
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalAppointmentDto> getAllAppointments() {
        List<MedicalAppointment> appointments = appointmentRepository.findAll();
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
        
        if (updateDto.getCompleted() != null) {
            appointment.setCompleted(updateDto.getCompleted());
        }
        
        if (updateDto.getCancelled() != null) {
            appointment.setCancelled(updateDto.getCancelled());
        }
        
        MedicalAppointment updatedAppointment = appointmentRepository.save(appointment);
        return convertToDto(updatedAppointment);
    }

    @Override
    @Transactional
    public MedicalAppointmentDto createAppointment(CreateAppointmentDto createDto) {
        MedicalAppointment appointment = new MedicalAppointment();
        User doctor = userRepository.findById(createDto.getDoctorId())
            .filter(user -> user.getRole() == RoleName.ROLE_VET)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with id: " + createDto.getDoctorId()));
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(createDto.getAppointmentDateTime());
        appointment.setDiagnosis(createDto.getDiagnosis());
        appointment.setTreatment(createDto.getTreatment());
        appointment.setNotes(createDto.getNotes());
        appointment.setCompleted(Boolean.TRUE.equals(createDto.getCompleted()));
        appointment.setCancelled(Boolean.TRUE.equals(createDto.getCancelled()));
        MedicalAppointment saved = appointmentRepository.save(appointment);
        return convertToDto(saved);
    }
    
    private MedicalAppointmentDto convertToDto(MedicalAppointment appointment) {
        return MedicalAppointmentDto.builder()
                .id(appointment.getId())
                .requestId(appointment.getRequest().getId())
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
                .completed(appointment.isCompleted())
                .cancelled(appointment.isCancelled())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
} 