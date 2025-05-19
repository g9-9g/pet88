package com.framja.itss.medical.service;

import java.util.List;

import com.framja.itss.medical.dto.MedicalAppointmentDto;
import com.framja.itss.medical.dto.MedicalAppointmentDetailDto;
import com.framja.itss.medical.dto.UpdateAppointmentDto;
import com.framja.itss.medical.dto.CreateAppointmentDto;

public interface MedicalAppointmentService {
    MedicalAppointmentDto getAppointmentById(Long appointmentId);
    MedicalAppointmentDetailDto getAppointmentDetailsById(Long appointmentId);
    List<MedicalAppointmentDto> getAppointmentsByOwnerId(Long ownerId);
    List<MedicalAppointmentDto> getAppointmentsByDoctorId(Long doctorId);
    List<MedicalAppointmentDto> getAppointmentsByPetId(Long petId);
    List<MedicalAppointmentDto> getActiveAppointments();
    List<MedicalAppointmentDto> getAllAppointments();
    MedicalAppointmentDto updateAppointment(Long appointmentId, UpdateAppointmentDto updateDto);
    MedicalAppointmentDto createAppointment(CreateAppointmentDto createDto, Long doctorId);
} 