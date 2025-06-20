package com.framja.itss.booking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.entity.Room;
import com.framja.itss.booking.repository.BookingRepository;
import com.framja.itss.booking.repository.RoomRepository;
import com.framja.itss.booking.service.BookingService;
import com.framja.itss.booking.util.BookingFeeCalculator;
import com.framja.itss.exception.ResourceNotFoundException;
import com.framja.itss.pets.service.PetService;
import com.framja.itss.common.dto.CountDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final PetService petService;

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getAllBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getMyBookingsByStatus(Long ownerId, BookingStatus status) {
        return bookingRepository.findByOwnerIdAndStatusOrderByCreatedAtDesc(ownerId, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDTO updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        booking.setStatus(status);
        if (status == BookingStatus.CONFIRMED) {
            booking.setConfirmed(true);
        }
        
        return convertToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        if (hasOverlappingBooking(bookingDTO.getRoomId(), bookingDTO.getCheckInTime(), bookingDTO.getCheckOutTime())) {
            throw new IllegalStateException("Phòng đã được đặt trong khoảng thời gian này");
        }

        updateBookingFromDTO(booking, bookingDTO);
        return convertToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        if (hasOverlappingBooking(bookingDTO.getRoomId(), bookingDTO.getCheckInTime(), bookingDTO.getCheckOutTime())) {
            throw new IllegalStateException("Phòng đã được đặt trong khoảng thời gian này");
        }

        Booking booking = convertToEntity(bookingDTO);
        booking.setStatus(BookingStatus.PENDING);
        booking.setConfirmed(false);
        
        return convertToDTO(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDTO> getMyBookings(Long ownerId) {
        return bookingRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return convertToDTO(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id, Long ownerId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        if (!booking.getOwnerId().equals(ownerId) || !isOwnerOfBooking(id, ownerId)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel a booking that is not in PENDING status");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public boolean isOwnerOfBooking(Long bookingId, Long ownerId) {
        return bookingRepository.existsByIdAndOwnerId(bookingId, ownerId);
    }

    @Override
    public boolean hasOverlappingBooking(Long roomId, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        return bookingRepository.existsOverlappingBooking(roomId, checkInTime, checkOutTime);
    }

    @Override
    public CountDTO getBookingCountsByOwnerId(Long ownerId) {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = bookingRepository.countByStatusAndOwnerId(ownerId);
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((BookingStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }

    @Override
    public CountDTO getBookingCountsAll() {
        CountDTO countDTO = new CountDTO();
        
        // Get status counts
        List<Object[]> statusCounts = bookingRepository.countByStatusAll();
        Map<String, Long> statusCountMap = new HashMap<>();
        
        Long total = 0L;
        for (Object[] statusCount : statusCounts) {
            String status = ((BookingStatus) statusCount[0]).name();
            Long count = (Long) statusCount[1];
            statusCountMap.put(status, count);
            total += count;
        }
        
        countDTO.setTotal(total);
        countDTO.setStatusCounts(statusCountMap);
        return countDTO;
    }

    private BookingDTO convertToDTO(Booking booking) {
        String petName = petService.getPetById(booking.getPetId())
                .map(pet -> pet.getName())
                .orElse("Unknown Pet");

        return BookingDTO.builder()
                .id(booking.getId())
                .petId(booking.getPetId())
                .petName(petName)
                .ownerId(booking.getOwnerId())
                .roomId(booking.getRoom().getId())
                .roomType(booking.getRoom().getType())
                .checkInTime(booking.getCheckInTime())
                .checkOutTime(booking.getCheckOutTime())
                .status(booking.getStatus())
                .isConfirmed(booking.isConfirmed())
                .specialCareNotes(booking.getSpecialCareNotes())
                .estimatedFee(booking.getEstimatedFee())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    private Booking convertToEntity(BookingDTO dto) {
        // Lấy thông tin room từ roomId
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + dto.getRoomId()));

        Booking booking = Booking.builder()
                .id(dto.getId())
                .petId(dto.getPetId())
                .ownerId(dto.getOwnerId())
                .room(room)  // Set room cho booking
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .status(dto.getStatus())
                .isConfirmed(dto.isConfirmed())
                .specialCareNotes(dto.getSpecialCareNotes())
                .build();

        // Tính toán estimatedFee nếu có đủ thông tin
        if (booking.getRoom() != null && booking.getCheckInTime() != null && booking.getCheckOutTime() != null) {
            BigDecimal estimatedFee = BookingFeeCalculator.calculateTotalFee(
                booking.getRoom().getNightlyFee(),
                booking.getRoom().getCleanFee(),
                booking.getRoom().getServiceFee(),
                booking.getCheckInTime(),
                booking.getCheckOutTime(),
                booking.getRoom().getType()
            );
            booking.setEstimatedFee(estimatedFee);
        }

        return booking;
    }

    private void updateBookingFromDTO(Booking booking, BookingDTO dto) {
        booking.setPetId(dto.getPetId());
        booking.setCheckInTime(dto.getCheckInTime());
        booking.setCheckOutTime(dto.getCheckOutTime());
        booking.setSpecialCareNotes(dto.getSpecialCareNotes());

        // Cập nhật room nếu roomId thay đổi
        if (dto.getRoomId() != null && !dto.getRoomId().equals(booking.getRoom().getId())) {
            Room newRoom = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + dto.getRoomId()));
            booking.setRoom(newRoom);
        }
    }
} 