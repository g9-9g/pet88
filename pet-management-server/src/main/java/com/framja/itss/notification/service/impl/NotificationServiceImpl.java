package com.framja.itss.notification.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framja.itss.notification.dto.NotificationDTO;
import com.framja.itss.notification.entity.Notification;
import com.framja.itss.notification.entity.NotificationStatus;
import com.framja.itss.notification.repository.NotificationRepository;
import com.framja.itss.notification.service.NotificationService;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getOwnedNotifications(Long userId, NotificationStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        List<Notification> notifications;
        if (status != null) {
            notifications = notificationRepository.findByUserAndStatus(user, status);
        } else {
            notifications = notificationRepository.findByUser(user);
        }
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO updateNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationRepository.findById(notificationDTO.getId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setMessage(notificationDTO.getMessage());
        notification.setStatus(notificationDTO.getStatus());
        
        return convertToDTO(notificationRepository.save(notification));
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        User user = userRepository.findById(notificationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(notificationDTO.getMessage());
        notification.setStatus(NotificationStatus.UNREAD);
        
        return convertToDTO(notificationRepository.save(notification));
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
            notification.getId(),
            notification.getUser().getId(),
            notification.getMessage(),
            notification.getStatus()
        );
    }
} 