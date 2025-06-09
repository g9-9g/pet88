package com.framja.itss.notification.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.entity.Notification;
import com.framja.itss.notification.entity.NotificationStatus;
import com.framja.itss.notification.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationDTO notificationDTO;
    private Notification notification;

    @BeforeEach
    void setUp() {
        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setUserId(1L);
        notificationDTO.setMessage("Test notification");
        notificationDTO.setStatus(NotificationStatus.UNREAD);

        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(1L);
        notification.setMessage("Test notification");
        notification.setStatus(NotificationStatus.UNREAD);
    }

    @Test
    @DisplayName("Should get all notifications successfully")
    void getAllNotifications_Success() {
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification));

        List<NotificationDTO> result = notificationService.getAllNotifications();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO.getMessage(), result.get(0).getMessage());
    }

    @Test
    @DisplayName("Should get owned notifications successfully")
    void getOwnedNotifications_Success() {
        when(notificationRepository.findByUserIdAndStatus(1L, NotificationStatus.UNREAD))
            .thenReturn(Arrays.asList(notification));

        List<NotificationDTO> result = notificationService.getOwnedNotifications(1L, NotificationStatus.UNREAD);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO.getMessage(), result.get(0).getMessage());
    }

    @Test
    @DisplayName("Should update notification successfully")
    void updateNotification_Success() {
        when(notificationRepository.findById(1L)).thenReturn(java.util.Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationDTO updatedDTO = new NotificationDTO();
        updatedDTO.setMessage("Updated notification");
        NotificationDTO result = notificationService.updateNotification(updatedDTO);

        assertNotNull(result);
        assertEquals("Updated notification", result.getMessage());
    }

    @Test
    @DisplayName("Should delete notification successfully")
    void deleteNotification_Success() {
        doNothing().when(notificationRepository).deleteById(1L);

        notificationService.deleteNotification(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should create notification successfully")
    void createNotification_Success() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationDTO result = notificationService.createNotification(notificationDTO);

        assertNotNull(result);
        assertEquals(notificationDTO.getMessage(), result.getMessage());
        verify(notificationRepository).save(any(Notification.class));
    }
} 