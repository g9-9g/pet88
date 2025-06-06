package com.framja.itss.notification.dto;

import com.framja.itss.notification.entity.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private NotificationStatus status;
} 