package com.farmasense.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class WeatherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String region;
    private String forecast;
    private LocalDate date;
    
    @ManyToOne
    @javax.persistence.JoinColumn(name = "admin_id")
    private Admin associatedAdmin;

    public WeatherInfo() {}

    public WeatherInfo(String region, String forecast, LocalDate date, Admin associatedAdmin) {
        this.region = region;
        this.forecast = forecast;
        this.date = date;
        this.associatedAdmin = associatedAdmin;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getForecast() { return forecast; }
    public void setForecast(String forecast) { this.forecast = forecast; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Admin getAssociatedAdmin() { return associatedAdmin; }
    public void setAssociatedAdmin(Admin associatedAdmin) { this.associatedAdmin = associatedAdmin; }
}
