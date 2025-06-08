package com.framja.itss.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.common.dto.CountDTO;

public interface BookingService {
    // Admin/Staff operations
    List<BookingDTO> getAllBookings();
    List<BookingDTO> getAllBookingsByStatus(BookingStatus status);
    BookingDTO updateBookingStatus(Long id, BookingStatus status);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    
    // Owner operations
    BookingDTO createBooking(BookingDTO bookingDTO);
    List<BookingDTO> getMyBookings(Long ownerId);
    List<BookingDTO> getMyBookingsByStatus(Long ownerId, BookingStatus status);
    BookingDTO getBookingById(Long id);
    void cancelBooking(Long id, Long ownerId);
    
    // Common operations
    boolean isOwnerOfBooking(Long bookingId, Long ownerId);
    
    // Check booking overlap
    boolean hasOverlappingBooking(Long roomId, LocalDateTime checkInTime, LocalDateTime checkOutTime);

    // Count operations
    CountDTO getBookingCountsAll();
    CountDTO getBookingCountsByOwnerId(Long ownerId);
} 