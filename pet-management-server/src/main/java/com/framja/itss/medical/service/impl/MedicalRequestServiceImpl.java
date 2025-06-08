package com.framja.itss.medical.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.common.enums.MedicalRequestStatus;
import com.framja.itss.exception.ResourceNotFoundException;
import com.framja.itss.medical.dto.request.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.request.MedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateRequestStatusDto;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.entity.MedicalRequest;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.repository.MedicalRequestRepository;
import com.framja.itss.medical.service.MedicalRequestService;
import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.service.NotificationService;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;
import com.framja.itss.common.dto.CountDTO;

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

    @Autowired
    private NotificationService notificationService;

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
                .status(MedicalRequestStatus.PENDING)
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
        List<MedicalRequest> requests = requestRepository.findByStatus(MedicalRequestStatus.PENDING);
        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalRequestDto updateRequestStatus(Long requestId, UpdateRequestStatusDto updateDto) {
        MedicalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        
        if (request.getStatus() != MedicalRequestStatus.PENDING) {
            throw new IllegalArgumentException("MedicalRequestStatus is not pending");
        }
        
        request.setStatus(updateDto.getStatus());
        
        // Set the staff who updates the request
        if (updateDto.getStaffId() != null) {
            User staff = userRepository.findById(updateDto.getStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + updateDto.getStaffId()));
            request.setUpdatedBy(staff);
        }
        
        if (updateDto.getStatus() == MedicalRequestStatus.ACCEPTED) {
            if (updateDto.getDoctorId() == null) {
                throw new IllegalArgumentException("Doctor ID is required for accepting request");
            }
            
            User doctor = userRepository.findById(updateDto.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + updateDto.getDoctorId()));
            
            // Create appointment
            MedicalAppointment appointment = MedicalAppointment.builder()
                    .request(request)
                    .pet(request.getPet())
                    .owner(request.getOwner())
                    .doctor(doctor)
                    .appointmentDateTime(request.getPreferredDateTime())
                    .symptoms(request.getSymptoms())
                    .notes(request.getNotes())
                    .status(AppointmentStatus.SCHEDULED)
                    .build();
            
            appointmentRepository.save(appointment);

            // Send notification to owner
            NotificationDTO ownerNotification = new NotificationDTO();
            ownerNotification.setUserId(request.getOwner().getId());
            ownerNotification.setMessage("Yêu cầu khám bệnh của bạn đã được chấp nhận. Lịch hẹn đã được tạo.");
            notificationService.createNotification(ownerNotification);

            // Send notification to doctor
            NotificationDTO doctorNotification = new NotificationDTO();
            doctorNotification.setUserId(doctor.getId());
            doctorNotification.setMessage("Bạn có một lịch hẹn khám bệnh mới cho thú cưng " + request.getPet().getName() + 
                    " vào lúc " + request.getPreferredDateTime());
            notificationService.createNotification(doctorNotification);
            
        } else if (updateDto.getStatus() == MedicalRequestStatus.REJECTED) {
            request.setRejectionReason(updateDto.getRejectionReason());
            
            // Send notification to owner
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setUserId(request.getOwner().getId());
            notificationDTO.setMessage("Yêu cầu khám bệnh của bạn đã bị từ chối. Lý do: " + updateDto.getRejectionReason());
            notificationService.createNotification(notificationDTO);
        }
        
        MedicalRequest updatedRequest = requestRepository.save(request);
        return convertToDto(updatedRequest);
    }

    @Override
    public List<MedicalRequestDto> getAllRequests(Long ownerId, MedicalRequestStatus medicalRequestStatus) {
        List<MedicalRequest> requests = requestRepository.findAll();
        
        return requests.stream()
                .filter(request -> {
                    // Apply owner ID filter if provided
                    if (ownerId != null) {
                        if (!request.getOwner().getId().equals(ownerId)) {
                            return false;
                        }
                    }
                    
                    // Apply medicalRequestStatus filter if provided
                    if (medicalRequestStatus != null) {
                        if (!request.getStatus().equals(medicalRequestStatus)) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countRequests(Long ownerId, MedicalRequestStatus medicalRequestStatus) {
        List<MedicalRequest> requests = requestRepository.findAll();
        
        return requests.stream()
                .filter(request -> {
                    // Apply owner ID filter if provided
                    if (ownerId != null) {
                        if (!request.getOwner().getId().equals(ownerId)) {
                            return false;
                        }
                    }
                    
                    // Apply medicalRequestStatus filter if provided
                    if (medicalRequestStatus != null) {
                        if (!request.getStatus().equals(medicalRequestStatus)) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .count();
    }
    
    @Override
    @Transactional
    public void deleteRequest(Long requestId, Long userId) {
        MedicalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        
        // Only the owner of the request can delete it
        if (!request.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the owner can delete this request");
        }
        
        // Only PENDING requests can be deleted
        if (request.getStatus() != MedicalRequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be deleted");
        }
        
        requestRepository.delete(request);
    }
    
    @Override
    @Transactional
    public MedicalRequestDto updateRequest(Long requestId, UpdateMedicalRequestDto updateDto, Long userId) {
        MedicalRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + requestId));
        
        // Only the owner of the request can update it
        if (!request.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the owner can update this request");
        }
        
        // Only PENDING requests can be updated
        if (request.getStatus() != MedicalRequestStatus.PENDING) {
            throw new IllegalArgumentException("Only pending requests can be updated");
        }
        
        // Validate that the pet exists and belongs to the owner
        Pet pet = petRepository.findById(updateDto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + updateDto.getPetId()));
        
        if (!pet.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("The pet does not belong to this owner");
        }
        
        // Update the request properties
        request.setPet(pet);
        
        if (updateDto.getSymptoms() != null) {
            request.setSymptoms(updateDto.getSymptoms());
        }
        
        if (updateDto.getNotes() != null) {
            request.setNotes(updateDto.getNotes());
        }
        
        if (updateDto.getPreferredDateTime() != null) {
            request.setPreferredDateTime(updateDto.getPreferredDateTime());
        }
        
        MedicalRequest updatedRequest = requestRepository.save(request);
        return convertToDto(updatedRequest);
    }
    
    @Override
    public CountDTO getRequestCountsByOwnerId(Long ownerId) {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = requestRepository.countByStatusAndOwnerId(ownerId);
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((MedicalRequestStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }

    @Override
    public CountDTO getRequestCountsAll() {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = requestRepository.countByStatusAll();
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((MedicalRequestStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }
    
    private MedicalRequestDto convertToDto(MedicalRequest request) {
        return MedicalRequestDto.builder()
                .id(request.getId())
                .petId(request.getPet().getPetId())
                .petName(request.getPet().getName())
                .ownerId(request.getOwner().getId())
                .ownerName(request.getOwner().getUsername())
                .updatedById(request.getUpdatedBy() != null ? request.getUpdatedBy().getId() : null)
                .updatedByName(request.getUpdatedBy() != null ? request.getUpdatedBy().getUsername() : null)
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