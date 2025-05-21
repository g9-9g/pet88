package com.framja.itss.booking.service.impl;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.repository.BookingRepository;
import com.framja.itss.booking.service.BookingService;
import com.framja.itss.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
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
        
        updateBookingFromDTO(booking, bookingDTO);
        return convertToDTO(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
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
    public BookingDTO getBookingById(Long id, Long ownerId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        if (!booking.getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        
        return convertToDTO(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id, Long ownerId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        if (!booking.getOwnerId().equals(ownerId)) {
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

    private BookingDTO convertToDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .petId(booking.getPetId())
                .ownerId(booking.getOwnerId())
                .roomId(booking.getRoom().getId())
                .checkInTime(booking.getCheckInTime())
                .checkOutTime(booking.getCheckOutTime())
                .status(booking.getStatus())
                .isConfirmed(booking.isConfirmed())
                .specialCareNotes(booking.getSpecialCareNotes())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    private Booking convertToEntity(BookingDTO dto) {
        return Booking.builder()
                .id(dto.getId())
                .petId(dto.getPetId())
                .ownerId(dto.getOwnerId())
                .checkInTime(dto.getCheckInTime())
                .checkOutTime(dto.getCheckOutTime())
                .status(dto.getStatus())
                .isConfirmed(dto.isConfirmed())
                .specialCareNotes(dto.getSpecialCareNotes())
                .build();
    }

    private void updateBookingFromDTO(Booking booking, BookingDTO dto) {
        booking.setPetId(dto.getPetId());
        booking.setCheckInTime(dto.getCheckInTime());
        booking.setCheckOutTime(dto.getCheckOutTime());
        booking.setSpecialCareNotes(dto.getSpecialCareNotes());
    }
} 