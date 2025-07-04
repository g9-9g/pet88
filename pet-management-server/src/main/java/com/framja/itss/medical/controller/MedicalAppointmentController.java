package com.framja.itss.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.medical.dto.appointment.CreateAppointmentDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDetailDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDto;
import com.framja.itss.medical.dto.appointment.UpdateAppointmentDto;
import com.framja.itss.medical.service.MedicalAppointmentService;
import com.framja.itss.users.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medical/appointments")
public class MedicalAppointmentController {

    @Autowired
    private MedicalAppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<MedicalAppointmentDto>> getAllAppointments(@RequestParam(required = false) AppointmentStatus status) {
        List<MedicalAppointmentDto> appointments = appointmentService.getAllAppointments(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasAnyRole('ROLE_PET_OWNER', 'ROLE_STAFF', 'ROLE_VET', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalAppointmentDetailDto> getAppointmentDetails(@PathVariable Long appointmentId) {
        MedicalAppointmentDetailDto appointmentDto = appointmentService.getAppointmentDetailsById(appointmentId);
        return ResponseEntity.ok(appointmentDto);
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<List<MedicalAppointmentDto>> getOwnerAppointments(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) AppointmentStatus status) {
        List<MedicalAppointmentDto> appointments = appointmentService.getAppointmentsByOwnerId(user.getId(), status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor")
    @PreAuthorize("hasRole('ROLE_VET')")
    public ResponseEntity<List<MedicalAppointmentDto>> getDoctorAppointments(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) AppointmentStatus status) {
        List<MedicalAppointmentDto> appointments = appointmentService.getAppointmentsByDoctorId(user.getId(), status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/pet")
    public ResponseEntity<List<MedicalAppointmentDto>> getAppointmentsByPetId(@RequestParam Long petId) {
        List<MedicalAppointmentDto> appointments = appointmentService.getAppointmentsByPetId(petId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<List<MedicalAppointmentDto>> getActiveAppointments() {
        List<MedicalAppointmentDto> appointments = appointmentService.getActiveAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_VET')")
    public ResponseEntity<MedicalAppointmentDto> createAppointment(@AuthenticationPrincipal User user, @Valid @RequestBody CreateAppointmentDto createDto) {
        MedicalAppointmentDto appointmentDto = appointmentService.createAppointment(createDto, user.getId());
        return ResponseEntity.ok(appointmentDto);
    }

    @PutMapping("/{appointmentId}")
    @PreAuthorize("hasRole('ROLE_VET')")
    public ResponseEntity<MedicalAppointmentDto> updateAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentDto updateDto) {
        MedicalAppointmentDto appointmentDto = appointmentService.updateAppointment(appointmentId, updateDto);
        return ResponseEntity.ok(appointmentDto);
    }


} 