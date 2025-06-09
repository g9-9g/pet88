package com.framja.itss.medical.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.framja.itss.common.enums.AppointmentStatus;
import com.framja.itss.common.enums.RoleName;
import com.framja.itss.medical.dto.appointment.CreateAppointmentDto;
import com.framja.itss.medical.dto.appointment.MedicalAppointmentDto;
import com.framja.itss.medical.entity.MedicalAppointment;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.service.impl.MedicalAppointmentServiceImpl;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.users.entity.User;

/**
 * Test class cho MedicalAppointmentService
 * Kiểm tra các chức năng liên quan đến lịch hẹn khám bệnh cho thú cưng
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MedicalAppointmentServiceTest {

    @Mock
    private MedicalAppointmentRepository appointmentRepository;

    @InjectMocks
    private MedicalAppointmentServiceImpl appointmentService;

    private MedicalAppointment mockAppointment;
    private CreateAppointmentDto createDto;
    private User mockOwner;
    private User mockDoctor;
    private Pet mockPet;

    @BeforeEach
    void setUp() {
        // Khởi tạo mock chủ thú cưng
        mockOwner = User.builder()
            .id(1L)
            .username("owner")
            .password("password")
            .role(RoleName.ROLE_PET_OWNER)
            .build();
        
        // Khởi tạo mock bác sĩ thú y
        mockDoctor = User.builder()
            .id(2L)
            .username("doctor")
            .password("password")
            .role(RoleName.ROLE_VET)
            .build();
        
        // Khởi tạo mock thú cưng
        mockPet = Pet.builder()
            .petId(1L)
            .name("Test Pet")
            .species("Dog")
            .owner(mockOwner)
            .build();

        // Khởi tạo mock lịch hẹn
        mockAppointment = MedicalAppointment.builder()
            .id(1L)
            .owner(mockOwner)
            .doctor(mockDoctor)
            .pet(mockPet)
            .status(AppointmentStatus.SCHEDULED)
            .appointmentDateTime(LocalDateTime.now())
            .build();

        // Khởi tạo DTO tạo lịch hẹn mới
        createDto = new CreateAppointmentDto();
        createDto.setPetId(1L);
        createDto.setAppointmentDateTime(LocalDateTime.now());
    }

    /**
     * Test case: Lấy thông tin lịch hẹn theo ID thành công
     * Tình huống: Tìm kiếm lịch hẹn với ID tồn tại
     * Kết quả mong đợi:
     * - Trả về thông tin lịch hẹn
     * - ID của lịch hẹn trả về phải khớp với ID tìm kiếm
     */
    @Test
    void getAppointmentById_Success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(mockAppointment));

        MedicalAppointmentDto result = appointmentService.getAppointmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(appointmentRepository).findById(1L);
    }

    /**
     * Test case: Lấy thông tin lịch hẹn theo ID thất bại
     * Tình huống: Tìm kiếm lịch hẹn với ID không tồn tại
     * Kết quả mong đợi:
     * - Ném ra RuntimeException
     * - Không tìm thấy lịch hẹn
     */
    @Test
    void getAppointmentById_NotFound() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            appointmentService.getAppointmentById(999L);
        });

        verify(appointmentRepository).findById(999L);
    }

    /**
     * Test case: Lấy danh sách lịch hẹn của chủ thú cưng thành công
     * Tình huống: Tìm kiếm lịch hẹn của chủ thú cưng có lịch hẹn
     * Kết quả mong đợi:
     * - Trả về danh sách lịch hẹn
     * - Danh sách không rỗng
     * - Số lượng lịch hẹn phải đúng
     */
    @Test
    void getAppointmentsByOwnerId_Success() {
        when(appointmentRepository.findByOwnerIdAndStatus(1L, AppointmentStatus.SCHEDULED))
            .thenReturn(Arrays.asList(mockAppointment));

        List<MedicalAppointmentDto> results = appointmentService.getAppointmentsByOwnerId(1L, AppointmentStatus.SCHEDULED);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(appointmentRepository).findByOwnerIdAndStatus(1L, AppointmentStatus.SCHEDULED);
    }

    /**
     * Test case: Lấy danh sách lịch hẹn của chủ thú cưng khi không có lịch hẹn
     * Tình huống: Tìm kiếm lịch hẹn của chủ thú cưng không có lịch hẹn nào
     * Kết quả mong đợi:
     * - Trả về danh sách rỗng
     * - Không có lịch hẹn nào được tìm thấy
     */
    @Test
    void getAppointmentsByOwnerId_Empty() {
        when(appointmentRepository.findByOwnerIdAndStatus(1L, AppointmentStatus.SCHEDULED))
            .thenReturn(Arrays.asList());

        List<MedicalAppointmentDto> results = appointmentService.getAppointmentsByOwnerId(1L, AppointmentStatus.SCHEDULED);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(appointmentRepository).findByOwnerIdAndStatus(1L, AppointmentStatus.SCHEDULED);
    }
} 