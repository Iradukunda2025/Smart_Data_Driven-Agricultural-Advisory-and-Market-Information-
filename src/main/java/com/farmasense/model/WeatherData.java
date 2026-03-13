package com.farmasense.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String location;
    private Double temperature;
    private Double rainfall; // in mm
    private String forecast;
    private LocalDate date;

    public WeatherData() {}

    public WeatherData(String location, Double temperature, Double rainfall, String forecast, LocalDate date) {
        this.location = location;
        this.temperature = temperature;
        this.rainfall = rainfall;
        this.forecast = forecast;
        this.date = date;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getRainfall() { return rainfall; }
    public void setRainfall(Double rainfall) { this.rainfall = rainfall; }
    public String getForecast() { return forecast; }
    public void setForecast(String forecast) { this.forecast = forecast; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
