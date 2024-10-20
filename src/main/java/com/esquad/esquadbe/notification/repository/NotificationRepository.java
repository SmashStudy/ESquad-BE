package com.esquad.esquadbe.notification.repository;

import com.esquad.esquadbe.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
