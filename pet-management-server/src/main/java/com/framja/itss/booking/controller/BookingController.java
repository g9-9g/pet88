package com.framja.itss.booking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.service.BookingService;
import com.framja.itss.users.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_PET_OWNER')")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_PET_OWNER')")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody BookingStatus status) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, status));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF', 'ROLE_PET_OWNER')")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable Long id,
            @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDTO));
    }

    // Owner endpoints
    @PostMapping
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<BookingDTO> createBooking(
        @RequestBody BookingDTO bookingDTO,
        @AuthenticationPrincipal User user) {
        bookingDTO.setOwnerId(user.getId());
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<List<BookingDTO>> getMyBookings(
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingService.getMyBookings(user.getId()));
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        bookingService.cancelBooking(id, user.getId());
        return ResponseEntity.ok().build();
    }
} 