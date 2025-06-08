package com.framja.itss.booking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.common.dto.CountDTO;
import com.framja.itss.booking.dto.RoomDTO;
import com.framja.itss.booking.dto.RoomRequest;
import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.entity.Room;
import com.framja.itss.booking.entity.RoomType;
import com.framja.itss.booking.repository.RoomRepository;
import com.framja.itss.booking.service.RoomService;
import com.framja.itss.booking.util.BookingFeeCalculator;
import com.framja.itss.exception.ResourceNotFoundException;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return convertToDTO(room);
    }

    @Override
    public RoomDTO createRoom(RoomRequest roomRequest) {
        Room room = Room.builder()
                .description(roomRequest.getDescription())
                .type(roomRequest.getType())
                .nightlyFee(roomRequest.getNightlyFee())
                .cleanFee(roomRequest.getCleanFee())
                .serviceFee(roomRequest.getServiceFee())
                .averageFee(BookingFeeCalculator.calculateAverageFeePerDay(roomRequest.getNightlyFee(), roomRequest.getCleanFee(), roomRequest.getServiceFee(), roomRequest.getType()))
                .isAvailable(roomRequest.isAvailable())
                .build();
                
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        if (roomRequest.getDescription() != null) {
            room.setDescription(roomRequest.getDescription());
        }
        if (roomRequest.getType() != null) {
            room.setType(roomRequest.getType());
        }
        if (roomRequest.getNightlyFee() != null) {
            room.setNightlyFee(roomRequest.getNightlyFee());
        }
        if (roomRequest.getCleanFee() != null) {
            room.setCleanFee(roomRequest.getCleanFee());
        }
        if (roomRequest.getServiceFee() != null) {
            room.setServiceFee(roomRequest.getServiceFee());
        }
        
        room.setAvailable(roomRequest.isAvailable());

        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        roomRepository.delete(room);
    }

    /*
     *  SELECT * FROM rooms r
        WHERE r.is_available = true
        AND r.id NOT IN (
            SELECT b.room_id FROM bookings b
            WHERE (
                b.check_in_time <= :endTime
                AND b.check_out_time >= :startTime
            )
            AND b.status NOT IN ('CANCELLED', 'COMPLETED')
        )
     */
    @Override
    public List<RoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, RoomType type, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDir) {
        Specification<Room> spec = (root, query, cb) -> {
            Predicate predicate = cb.isTrue(root.get("isAvailable"));
            
            // Exclude rooms with overlapping bookings
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);
            subquery.select(bookingRoot.get("room").get("id"));
            Predicate overlap = cb.and(
                cb.lessThanOrEqualTo(bookingRoot.get("checkInTime"), endTime),
                cb.greaterThanOrEqualTo(bookingRoot.get("checkOutTime"), startTime)
            );
            Predicate notCancelledOrCompleted = bookingRoot.get("status").in(BookingStatus.CANCELLED, BookingStatus.COMPLETED).not();
            subquery.where(cb.and(overlap, notCancelledOrCompleted));
            predicate = cb.and(predicate, root.get("id").in(subquery).not());
            
            // Apply room type filter 
            if (type != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type"), type));
            }
            
            // Use averageFee field for price filtering
            if (minPrice != null || maxPrice != null) {
                if (minPrice != null) {
                    predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("averageFee"), minPrice));
                }
                if (maxPrice != null) {
                    predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("averageFee"), maxPrice));
                }
            }
            
            return predicate;
        };
        
        if (sortBy == null || sortBy.isEmpty()) sortBy = "nightlyFee";
        Sort.Direction direction = (sortDir == null || !"desc".equalsIgnoreCase(sortDir)) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        
        // Get filtered results directly from the database
        return roomRepository.findAll(spec, sort).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO updateRoomAvailability(Long id, boolean isAvailable) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        room.setAvailable(isAvailable);
        Room updatedRoom = roomRepository.save(room);
        return convertToDTO(updatedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public CountDTO getRoomCountsByType() {
        CountDTO countDTO = new CountDTO();
        
        // Get type counts
        List<Object[]> typeCounts = roomRepository.countByType();
        Map<String, Long> typeCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] typeCount : typeCounts) {
            String type = ((RoomType) typeCount[0]).name();
            Long count = (Long) typeCount[1];
            typeCountMap.put(type, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(typeCountMap);
        return countDTO;
    }

    private RoomDTO convertToDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .description(room.getDescription())
                .type(room.getType())
                .nightlyFee(room.getNightlyFee())
                .cleanFee(room.getCleanFee())
                .serviceFee(room.getServiceFee())
                .averageFee(room.getAverageFee())
                .isAvailable(room.isAvailable())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
} 