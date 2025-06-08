package com.framja.itss.medical.service;

import java.util.List;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.common.dto.CountDTO;
import com.framja.itss.medical.dto.appointment.CreateAppointmentDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDetailDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDto;
import com.framja.itss.medical.dto.appointment.UpdateAppointmentDto;

public interface MedicalAppointmentService {
    MedicalAppointmentDto getAppointmentById(Long appointmentId);
    MedicalAppointmentDetailDto getAppointmentDetailsById(Long appointmentId);
    List<MedicalAppointmentDto> getAppointmentsByOwnerId(Long ownerId, AppointmentStatus status);
    List<MedicalAppointmentDto> getAppointmentsByDoctorId(Long doctorId, AppointmentStatus status);
    List<MedicalAppointmentDto> getAppointmentsByPetId(Long petId);
    List<MedicalAppointmentDto> getActiveAppointments();
    List<MedicalAppointmentDto> getAllAppointments(AppointmentStatus status);
    MedicalAppointmentDto updateAppointment(Long appointmentId, UpdateAppointmentDto updateDto);
    MedicalAppointmentDto createAppointment(CreateAppointmentDto createDto, Long doctorId);
    CountDTO getAppointmentCountsByOwnerId(Long ownerId);
    CountDTO getAppointmentCountsByDoctorId(Long doctorId);
    CountDTO getAppointmentCountsAll();
} 