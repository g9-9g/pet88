package com.framja.itss.booking.service;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.BookingStatus;

import java.util.List;

public interface BookingService {
    // Admin/Staff operations
    List<BookingDTO> getAllBookings();
    BookingDTO updateBookingStatus(Long id, BookingStatus status);
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    
    // Owner operations
    BookingDTO createBooking(BookingDTO bookingDTO);
    List<BookingDTO> getMyBookings(Long ownerId);
    BookingDTO getBookingById(Long id, Long ownerId);
    void cancelBooking(Long id, Long ownerId);
    
    // Common operations
    boolean isOwnerOfBooking(Long bookingId, Long ownerId);
} 