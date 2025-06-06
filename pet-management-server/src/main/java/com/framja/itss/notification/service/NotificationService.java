package com.framja.itss.notification.service;

import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.entity.NotificationStatus;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getOwnedNotifications(Long userId, NotificationStatus status);
    NotificationDTO updateNotification(NotificationDTO notificationDTO);
    void deleteNotification(Long id);
    NotificationDTO createNotification(NotificationDTO notificationDTO);
} 