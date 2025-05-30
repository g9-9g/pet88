package com.framja.itss.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.BookingStatus;

public interface BookingService {
    // Admin/Staff operations
    List<BookingDTO> getAllBookings();
    BookingDTO updateBookingStatus(Long id, BookingStatus status);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    
    // Owner operations
    BookingDTO createBooking(BookingDTO bookingDTO);
    List<BookingDTO> getMyBookings(Long ownerId);
    BookingDTO getBookingById(Long id);
    void cancelBooking(Long id, Long ownerId);
    
    // Common operations
    boolean isOwnerOfBooking(Long bookingId, Long ownerId);
    
    // Check booking overlap
    boolean hasOverlappingBooking(Long roomId, LocalDateTime checkInTime, LocalDateTime checkOutTime);
} 