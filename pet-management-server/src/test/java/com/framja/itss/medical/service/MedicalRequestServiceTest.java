package com.framja.itss.medical.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.framja.itss.common.enums.MedicalRequestStatus;
import com.framja.itss.common.enums.RoleName;
import com.framja.itss.medical.dto.request.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.request.MedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateRequestStatusDto;
import com.framja.itss.medical.entity.MedicalRequest;
import com.framja.itss.medical.repository.MedicalAppointmentRepository;
import com.framja.itss.medical.repository.MedicalRequestRepository;
import com.framja.itss.medical.service.impl.MedicalRequestServiceImpl;
import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.service.NotificationService;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.pets.repository.PetRepository;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;

/**
 * Test class cho MedicalRequestService
 * Kiểm tra các chức năng liên quan đến yêu cầu khám bệnh cho thú cưng
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MedicalRequestServiceTest {

    @Mock
    private MedicalRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private MedicalAppointmentRepository appointmentRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private MedicalRequestServiceImpl requestService;

    private MedicalRequest mockRequest;
    private CreateMedicalRequestDto createDto;
    private UpdateMedicalRequestDto updateDto;
    private UpdateRequestStatusDto statusDto;
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

        // Khởi tạo mock yêu cầu khám bệnh
        mockRequest = MedicalRequest.builder()
            .id(1L)
            .owner(mockOwner)
            .pet(mockPet)
            .status(MedicalRequestStatus.PENDING)
            .symptoms("Test symptoms")
            .notes("Test notes")
            .createdAt(LocalDateTime.now())
            .build();

        // Khởi tạo DTO tạo yêu cầu mới
        createDto = CreateMedicalRequestDto.builder()
            .petId(1L)
            .symptoms("Test symptoms")
            .notes("Test notes")
            .preferredDateTime(LocalDateTime.now().plusDays(1))
            .build();

        // Khởi tạo DTO cập nhật yêu cầu
        updateDto = new UpdateMedicalRequestDto();
        updateDto.setSymptoms("Updated symptoms");
        updateDto.setNotes("Updated notes");

        // Khởi tạo DTO cập nhật trạng thái
        statusDto = new UpdateRequestStatusDto();
        statusDto.setStatus(MedicalRequestStatus.ACCEPTED);
        statusDto.setDoctorId(2L);
    }

    /**
     * Test case: Tạo yêu cầu khám bệnh thành công
     * Tình huống: Chủ thú cưng tạo yêu cầu khám bệnh mới
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu đã tạo
     * - Yêu cầu được lưu vào database
     */
    @Test
    void createRequest_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockOwner));
        when(petRepository.findById(1L)).thenReturn(Optional.of(mockPet));
        when(requestRepository.save(any(MedicalRequest.class))).thenReturn(mockRequest);

        MedicalRequestDto result = requestService.createRequest(createDto, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(MedicalRequestStatus.PENDING, result.getStatus());
        verify(requestRepository).save(any(MedicalRequest.class));
    }

    /**
     * Test case: Lấy thông tin yêu cầu theo ID thành công
     * Tình huống: Tìm kiếm yêu cầu với ID tồn tại
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu
     * - ID của yêu cầu trả về phải khớp với ID tìm kiếm
     */
    @Test
    void getRequestById_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));

        MedicalRequestDto result = requestService.getRequestById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(requestRepository).findById(1L);
    }

    /**
     * Test case: Lấy danh sách yêu cầu của chủ thú cưng thành công
     * Tình huống: Tìm kiếm yêu cầu của chủ thú cưng có yêu cầu
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu
     * - Danh sách không rỗng
     * - Số lượng yêu cầu phải đúng
     */
    @Test
    void getRequestsByOwnerId_Success() {
        when(requestRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(mockRequest));

        List<MedicalRequestDto> results = requestService.getRequestsByOwnerId(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(requestRepository).findByOwnerId(1L);
    }

    /**
     * Test case: Lấy danh sách yêu cầu đang chờ xử lý thành công
     * Tình huống: Tìm kiếm các yêu cầu có trạng thái PENDING
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu đang chờ
     * - Tất cả yêu cầu phải có trạng thái PENDING
     */
    @Test
    void getPendingRequests_Success() {
        when(requestRepository.findByStatus(MedicalRequestStatus.PENDING))
            .thenReturn(Arrays.asList(mockRequest));

        List<MedicalRequestDto> results = requestService.getPendingRequests();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(MedicalRequestStatus.PENDING, results.get(0).getStatus());
        verify(requestRepository).findByStatus(MedicalRequestStatus.PENDING);
    }

    /**
     * Test case: Cập nhật trạng thái yêu cầu thành công
     * Tình huống: Cập nhật trạng thái yêu cầu từ PENDING sang ACCEPTED
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu đã cập nhật
     * - Trạng thái yêu cầu phải được cập nhật
     * - Tự động tạo cuộc hẹn khám bệnh mới
     * - Gửi thông báo cho chủ thú cưng
     */
    @Test
    void updateRequestStatus_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockDoctor));
        when(requestRepository.save(any(MedicalRequest.class))).thenReturn(mockRequest);
        when(appointmentRepository.save(any())).thenReturn(null);
        when(notificationService.createNotification(any(NotificationDTO.class))).thenReturn(null);

        MedicalRequestDto result = requestService.updateRequestStatus(1L, statusDto);

        assertNotNull(result);
        assertEquals(MedicalRequestStatus.ACCEPTED, result.getStatus());
        verify(requestRepository).save(any(MedicalRequest.class));
        verify(appointmentRepository).save(any());
        verify(notificationService).createNotification(any(NotificationDTO.class));
    }

    /**
     * Test case: Xóa yêu cầu thành công
     * Tình huống: Chủ thú cưng xóa yêu cầu của mình
     * Kết quả mong đợi:
     * - Yêu cầu bị xóa khỏi database
     * - Không có exception nào được ném ra
     */
    @Test
    void deleteRequest_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));
        doNothing().when(requestRepository).delete(mockRequest);

        assertDoesNotThrow(() -> {
            requestService.deleteRequest(1L, 1L);
        });

        verify(requestRepository).delete(mockRequest);
    }

    /**
     * Test case: Cập nhật thông tin yêu cầu thành công
     * Tình huống: Chủ thú cưng cập nhật thông tin yêu cầu của mình
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu đã cập nhật
     * - Thông tin yêu cầu phải được cập nhật
     */
    @Test
    void updateRequest_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));
        when(petRepository.findById(any())).thenReturn(Optional.of(mockPet));
        when(requestRepository.save(any(MedicalRequest.class))).thenReturn(mockRequest);

        MedicalRequestDto result = requestService.updateRequest(1L, updateDto, 1L);

        assertNotNull(result);
        assertEquals("Updated symptoms", result.getSymptoms());
        assertEquals("Updated notes", result.getNotes());
        verify(requestRepository).save(any(MedicalRequest.class));
    }

    /**
     * Test case: Lấy danh sách yêu cầu theo trạng thái thành công
     * Tình huống: Tìm kiếm yêu cầu của chủ thú cưng theo trạng thái
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu
     * - Tất cả yêu cầu phải có trạng thái tương ứng
     */
    @Test
    void getAllRequests_Success() {
        List<MedicalRequest> requests = Arrays.asList(mockRequest);
        when(requestRepository.findByOwnerId(1L)).thenReturn(requests);

        List<MedicalRequestDto> results = requestService.getAllRequests(1L, MedicalRequestStatus.PENDING);

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(requestRepository).findByOwnerId(1L);
    }
} 