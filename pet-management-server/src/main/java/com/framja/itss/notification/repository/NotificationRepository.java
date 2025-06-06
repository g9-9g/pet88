package com.framja.itss.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.framja.itss.notification.entity.Notification;
import com.framja.itss.notification.entity.NotificationStatus;
import com.framja.itss.users.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndStatus(User user, NotificationStatus status);
} 