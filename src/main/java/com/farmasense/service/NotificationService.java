package com.farmasense.service;

import com.farmasense.model.Notification;
import com.farmasense.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(String title, String message, String senderRole, String type) {
        Notification notification = new Notification(title, message, senderRole, type);
        notificationRepository.save(notification);
    }

    public List<Notification> getLatestNotifications() {
        return notificationRepository.findAllByOrderByTimestampDesc();
    }
    
    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }
}
