package com.farmasense.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Advisory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private LocalDate date;

    @ManyToOne
    @javax.persistence.JoinColumn(name = "admin_id")
    private Admin associatedAdmin;

    public Advisory() {}

    public Advisory(String title, String description, LocalDate date, Admin associatedAdmin) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.associatedAdmin = associatedAdmin;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Admin getAssociatedAdmin() { return associatedAdmin; }
    public void setAssociatedAdmin(Admin associatedAdmin) { this.associatedAdmin = associatedAdmin; }
}
