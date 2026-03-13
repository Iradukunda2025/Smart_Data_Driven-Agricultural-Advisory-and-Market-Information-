package com.farmasense.service;

import com.farmasense.model.Farmer;
import com.farmasense.repository.FarmerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    public Optional<Farmer> getFarmerById(Long id) {
        return farmerRepository.findById(id);
    }

    public Farmer saveFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public Farmer updateFarmer(Long id, Farmer farmerDetails) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found for id :: " + id));
        
        farmer.setName(farmerDetails.getName());
        farmer.setLocation(farmerDetails.getLocation());
        farmer.setContact(farmerDetails.getContact());
        
        return farmerRepository.save(farmer);
    }

    public void deleteFarmer(Long id) {
        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmer not found for id :: " + id));
        farmerRepository.delete(farmer);
    }
}
