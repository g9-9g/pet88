package com.framja.itss.grooming.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.common.enums.GroomingRequestStatus;
import com.framja.itss.grooming.dto.GroomingRequestDto;
import com.framja.itss.grooming.dto.GroomingServiceDto;
import com.framja.itss.grooming.entity.GroomingRequest;
import com.framja.itss.grooming.entity.GroomingService;
import com.framja.itss.grooming.repository.GroomingRequestRepository;
import com.framja.itss.grooming.repository.GroomingServiceRepository;
import com.framja.itss.grooming.service.GroomingRequestService;
import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.service.NotificationService;
import com.framja.itss.pets.dto.PetDto;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;
import com.framja.itss.common.dto.CountDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroomingRequestServiceImpl implements GroomingRequestService {

    private final GroomingRequestRepository groomingRequestRepository;
    private final GroomingServiceRepository groomingServiceRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public GroomingRequestDto createRequest(GroomingRequestDto.CreateRequest createRequest, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));
        
        Pet pet = petRepository.findById(createRequest.getPetId())
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + createRequest.getPetId()));
        
        // Verify the pet belongs to the owner
        if (!pet.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("Pet does not belong to the requesting user");
        }
        
        GroomingService service = groomingServiceRepository.findById(createRequest.getServiceId())
                .orElseThrow(() -> new RuntimeException("Grooming service not found with id: " + createRequest.getServiceId()));
        
        GroomingRequest request = GroomingRequest.builder()
                .service(service)
                .pet(pet)
                .owner(owner)
                .requestedDateTime(createRequest.getRequestedDateTime())
                .notes(createRequest.getNotes())
                .status(GroomingRequestStatus.PENDING)
                .build();
        
        request = groomingRequestRepository.save(request);
        return mapToDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroomingRequestDto> getRequestById(Long id) {
        return groomingRequestRepository.findById(id)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingRequestDto> getAllRequests() {
        return groomingRequestRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingRequestDto> getRequestsByOwnerId(Long ownerId) {
        return groomingRequestRepository.findByOwnerId(ownerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingRequestDto> getRequestsByPetId(Long petId) {
        return groomingRequestRepository.findByPetId(petId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingRequestDto> getRequestsByStaffId(Long staffId) {
        return groomingRequestRepository.findByStaffId(staffId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroomingRequestDto> getRequestsByStatus(GroomingRequestStatus status) {
        return groomingRequestRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroomingRequestDto updateRequestStatus(Long id, GroomingRequestDto.UpdateRequest updateRequest, Long staffId) {
        GroomingRequest request = groomingRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grooming request not found with id: " + id));
        
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + staffId));
        
        request.setStaff(staff);
        request.setStatus(updateRequest.getStatus());
        if (updateRequest.getScheduledDateTime() != null) {
            request.setScheduledDateTime(updateRequest.getScheduledDateTime());
        }
        if (updateRequest.getStaffNotes() != null) {
            request.setStaffNotes(updateRequest.getStaffNotes());
        }
        
        // If status is completed, set the completed date
        if (updateRequest.getStatus() == GroomingRequestStatus.COMPLETED) {
            request.setCompletedDateTime(LocalDateTime.now());
        }
        
        // Send notification to owner
        if (updateRequest.getStatus() != null) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setUserId(request.getOwner().getId());
            notificationDTO.setMessage("Trạng thái yêu cầu chăm sóc thú cưng của bạn đã được cập nhật thành: " + updateRequest.getStatus());
            notificationService.createNotification(notificationDTO);
        }
        
        request = groomingRequestRepository.save(request);
        return mapToDto(request);
    }

    @Override
    @Transactional
    public void deleteRequest(Long id) {
        groomingRequestRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GroomingRequestDto> getFilteredRequestsWithPagination(
            Long ownerId, Long petId, Long serviceId, Long staffId, GroomingRequestStatus status,
            LocalDateTime startDate, LocalDateTime endDate, String sortBy, String sortDir, int page, int size) {
        

        
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
                Sort.by(sortBy).ascending() : 
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<GroomingRequest> requestPage = groomingRequestRepository.findWithFilters(
                ownerId, petId, serviceId, staffId, status, startDate, endDate, pageable);
        
        return requestPage.map(this::mapToDto);
    }
    
    private GroomingRequestDto mapToDto(GroomingRequest entity) {
        GroomingServiceDto serviceDto = GroomingServiceDto.builder()
                .id(entity.getService().getId())
                .name(entity.getService().getName())
                .description(entity.getService().getDescription())
                .price(entity.getService().getPrice())
                .durationMinutes(entity.getService().getDurationMinutes())
                .isActive(entity.getService().getIsActive())
                .imageUrl(entity.getService().getImageUrl())
                .createdAt(entity.getService().getCreatedAt())
                .updatedAt(entity.getService().getUpdatedAt())
                .build();

        PetDto petDto = PetDto.builder()
                .petId(entity.getPet().getPetId())
                .ownerId(entity.getPet().getOwner().getId())
                .name(entity.getPet().getName())
                .species(entity.getPet().getSpecies())
                .breed(entity.getPet().getBreed())
                .gender(entity.getPet().getGender())
                .birthdate(entity.getPet().getBirthdate())
                .color(entity.getPet().getColor())
                .avatarUrl(entity.getPet().getAvatarUrl())
                .healthNotes(entity.getPet().getHealthNotes())
                .vaccinationHistory(entity.getPet().getVaccinationHistory())
                .nutritionNotes(entity.getPet().getNutritionNotes())
                .createdAt(entity.getPet().getCreatedAt())
                .updatedAt(entity.getPet().getUpdatedAt())
                .build();

        return GroomingRequestDto.builder()
                .id(entity.getId())
                .serviceId(entity.getService().getId())
                .petId(entity.getPet().getPetId())
                .ownerId(entity.getOwner().getId())
                .staffId(entity.getStaff() != null ? entity.getStaff().getId() : null)
                .requestedDateTime(entity.getRequestedDateTime())
                .scheduledDateTime(entity.getScheduledDateTime())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .staffNotes(entity.getStaffNotes())
                .completedDateTime(entity.getCompletedDateTime())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .service(serviceDto)
                .pet(petDto)
                .build();
    }

    @Override
    public CountDTO getRequestCountsByOwnerId(Long ownerId) {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = groomingRequestRepository.countByStatusAndOwnerId(ownerId);
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((GroomingRequestStatus) statusCount[0]).name();
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
        List<Object[]> statusCounts = groomingRequestRepository.countByStatusAll();
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((GroomingRequestStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }
} 