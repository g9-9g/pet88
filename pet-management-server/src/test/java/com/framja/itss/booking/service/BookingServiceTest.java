package com.framja.itss.booking.service;

import com.framja.itss.booking.dto.BookingDTO;
import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.repository.BookingRepository;
import com.framja.itss.pet.entity.Pet;
import com.framja.itss.pet.service.PetService;
import com.framja.itss.room.entity.Room;
import com.framja.itss.room.service.RoomService;
import com.framja.itss.user.entity.User;
import com.framja.itss.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private PetService petService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private Pet pet;
    private Room room;
    private Booking booking;
    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        // Setup owner
        owner = new User();
        ReflectionTestUtils.setField(owner, "id", 1L);
        owner.setEmail("owner@example.com");

        // Setup pet
        pet = new Pet();
        ReflectionTestUtils.setField(pet, "id", 1L);
        pet.setName("Test Pet");
        pet.setOwner(owner);

        // Setup room
        room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setRoomNumber("101");
        room.setAvailable(true);

        // Setup booking
        booking = new Booking();
        ReflectionTestUtils.setField(booking, "id", 1L);
        booking.setOwner(owner);
        booking.setPet(pet);
        booking.setRoom(room);
        booking.setCheckInTime(LocalDateTime.now());
        booking.setCheckOutTime(LocalDateTime.now().plusDays(1));
        booking.setStatus(BookingStatus.PENDING);

        // Setup bookingDTO
        bookingDTO = new BookingDTO();
        bookingDTO.setId(1L);
        bookingDTO.setOwnerId(1L);
        bookingDTO.setPetId(1L);
        bookingDTO.setRoomId(1L);
        bookingDTO.setCheckInTime(LocalDateTime.now());
        bookingDTO.setCheckOutTime(LocalDateTime.now().plusDays(1));
        bookingDTO.setStatus(BookingStatus.PENDING);
    }

    @Test
    void getAllBookings_ShouldReturnAllBookings() {
        // Arrange
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        // Act
        List<BookingDTO> result = bookingService.getAllBookings();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository).findAll();
    }

    @Test
    void updateBookingStatus_ShouldUpdateStatus() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        BookingDTO result = bookingService.updateBookingStatus(1L, BookingStatus.CONFIRMED);

        // Assert
        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_ShouldCreateNewBooking() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(owner);
        when(petService.getPetById(1L)).thenReturn(pet);
        when(roomService.getRoomById(1L)).thenReturn(room);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        BookingDTO result = bookingService.createBooking(bookingDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getMyBookings_ShouldReturnOwnerBookings() {
        // Arrange
        when(bookingRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(booking));

        // Act
        List<BookingDTO> result = bookingService.getMyBookings(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository).findByOwnerId(1L);
    }

    @Test
    void getBookingById_ShouldReturnBooking() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        BookingDTO result = bookingService.getBookingById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookingRepository).findById(1L);
    }

    @Test
    void cancelBooking_ShouldCancelBooking() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // Act
        bookingService.cancelBooking(1L, 1L);

        // Assert
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void isOwnerOfBooking_ShouldReturnTrue_WhenOwnerMatches() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        // Act
        boolean result = bookingService.isOwnerOfBooking(1L, 1L);

        // Assert
        assertTrue(result);
        verify(bookingRepository).findById(1L);
    }

    @Test
    void hasOverlappingBooking_ShouldReturnTrue_WhenOverlapExists() {
        // Arrange
        LocalDateTime checkIn = LocalDateTime.now();
        LocalDateTime checkOut = LocalDateTime.now().plusDays(1);
        when(bookingRepository.findOverlappingBookings(1L, checkIn, checkOut))
            .thenReturn(Arrays.asList(booking));

        // Act
        boolean result = bookingService.hasOverlappingBooking(1L, checkIn, checkOut);

        // Assert
        assertTrue(result);
        verify(bookingRepository).findOverlappingBookings(1L, checkIn, checkOut);
    }
} 