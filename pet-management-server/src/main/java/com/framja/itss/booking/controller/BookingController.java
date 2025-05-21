package com.framja.itss.booking.controller;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDTO));
    }

    // Owner endpoints
    @PostMapping
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<BookingDTO> createBooking(
            @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<List<BookingDTO>> getMyBookings(
            @RequestParam Long ownerId) {
        return ResponseEntity.ok(bookingService.getMyBookings(ownerId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long id,
            @RequestParam Long ownerId) {
        return ResponseEntity.ok(bookingService.getBookingById(id, ownerId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long id,
            @RequestParam Long ownerId) {
        bookingService.cancelBooking(id, ownerId);
        return ResponseEntity.ok().build();
    }
} 