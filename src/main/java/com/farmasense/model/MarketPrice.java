package com.farmasense.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class MarketPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String crop;
    private Double price;
    private LocalDate date;

    @ManyToOne
    @javax.persistence.JoinColumn(name = "vendor_id")
    private Vendor associatedVendor;

    public MarketPrice() {}

    public MarketPrice(String crop, Double price, LocalDate date, Vendor associatedVendor) {
        this.crop = crop;
        this.price = price;
        this.date = date;
        this.associatedVendor = associatedVendor;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCrop() { return crop; }
    public void setCrop(String crop) { this.crop = crop; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Vendor getAssociatedVendor() { return associatedVendor; }
    public void setAssociatedVendor(Vendor associatedVendor) { this.associatedVendor = associatedVendor; }
}
