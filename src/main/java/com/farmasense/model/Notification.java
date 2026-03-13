package com.farmasense.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String message;
    private String senderRole; // ADMIN, VENDOR, SYSTEM
    private String type; // ADVISORY, MARKET_UPDATE, WEATHER_ALERT
    private LocalDateTime timestamp;
    private boolean isRead = false;

    public Notification() {}

    public Notification(String title, String message, String senderRole, String type) {
        this.title = title;
        this.message = message;
        this.senderRole = senderRole;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getSenderRole() { return senderRole; }
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
