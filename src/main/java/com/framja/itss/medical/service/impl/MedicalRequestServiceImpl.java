package com.framja.itss.medical.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.common.enums.Status;
import com.framja.itss.exception.ResourceNotFoundException;
import com.framja.itss.medical.dto.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.MedicalRequestDto;
import com.framja.itss.medical.dto.UpdateRequestStatusDto;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.entity.MedicalRequest;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.repository.MedicalRequestRepository;
import com.framja.itss.medical.service.MedicalRequestService;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;

@Service
public class MedicalRequestServiceImpl implements MedicalRequestService {

    @Autowired
    private MedicalRequestRepository requestRepository;
    
    @Autowired
    private MedicalAppointmentRepository appointmentRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public MedicalRequestDto createRequest(CreateMedicalRequestDto createDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id: " + ownerId));
        
        Pet pet = petRepository.findById(createDto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + createDto.getPetId()));
        
        // Validate that the pet belongs to the owner
        if (!pet.getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("The pet does not belong to this owner");
        }
        
        MedicalRequest request = MedicalRequest.builder()
                .pet(pet)
                .owner(owner)
                .symptoms(createDto.getSymptoms())
                .notes(createDto.getNotes())
                .preferredDateTime(createDto.getPreferredDateTime())
                .status(Status.PENDING)
                .build();
        
        MedicalRequest savedRequest = requestRepository.save(request);
        return convertToDto(savedRequest);
    }

    @Override
    public MedicalRequestDto getRequestById(Long requestId) {
        MedicalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        return convertToDto(request);
    }

    @Override
    public List<MedicalRequestDto> getRequestsByOwnerId(Long ownerId) {
        List<MedicalRequest> requests = requestRepository.findByOwnerId(ownerId);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRequestDto> getPendingRequests() {
        List<MedicalRequest> requests = requestRepository.findByStatus(Status.PENDING);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalRequestDto updateRequestStatus(Long requestId, UpdateRequestStatusDto updateDto) {
        MedicalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        
        request.setStatus(updateDto.getStatus());
        
        if (updateDto.getStatus() == Status.ACCEPTED) {
            if (updateDto.getDoctorId() == null) {
                throw new IllegalArgumentException("Doctor ID is required for accepting request");
            }
            
            User doctor = userRepository.findById(updateDto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + updateDto.getDoctorId()));
            
            request.setDoctor(doctor);
            
            // Create appointment
            MedicalAppointment appointment = MedicalAppointment.builder()
                    .request(request)
                    .pet(request.getPet())
                    .owner(request.getOwner())
                    .doctor(doctor)
                    .appointmentDateTime(request.getPreferredDateTime())
                    .symptoms(request.getSymptoms())
                    .notes(request.getNotes())
                    .build();
            
            appointmentRepository.save(appointment);
        } else if (updateDto.getStatus() == Status.REJECTED) {
            request.setRejectionReason(updateDto.getRejectionReason());
        }
        
        MedicalRequest updatedRequest = requestRepository.save(request);
        return convertToDto(updatedRequest);
    }

    @Override
    public List<MedicalRequestDto> getAllRequests(String ownerName, Status status) {
        List<MedicalRequest> requests = requestRepository.findAll();
        
        return requests.stream()
                .filter(request -> {
                    // Apply owner name filter if provided
                    if (ownerName != null && !ownerName.isEmpty()) {
                        String fullName = request.getOwner().getFullName();
                        if (fullName == null || !fullName.toLowerCase().contains(ownerName.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // Apply status filter if provided
                    if (status != null) {
                        if (!request.getStatus().equals(status)) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private MedicalRequestDto convertToDto(MedicalRequest request) {
        return MedicalRequestDto.builder()
                .id(request.getId())
                .petId(request.getPet().getPetId())
                .petName(request.getPet().getName())
                .ownerId(request.getOwner().getId())
                .ownerName(request.getOwner().getFullName())
                .doctorId(request.getDoctor() != null ? request.getDoctor().getId() : null)
                .doctorName(request.getDoctor() != null ? request.getDoctor().getFullName() : null)
                .symptoms(request.getSymptoms())
                .notes(request.getNotes())
                .preferredDateTime(request.getPreferredDateTime())
                .status(request.getStatus())
                .rejectionReason(request.getRejectionReason())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
} 