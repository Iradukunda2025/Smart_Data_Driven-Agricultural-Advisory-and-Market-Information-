package com.farmasense.controller;

import com.farmasense.model.Farmer;
import com.farmasense.service.FarmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @GetMapping
    public List<Farmer> getAllFarmers() {
        return farmerService.getAllFarmers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farmer> getFarmerById(@PathVariable Long id) {
        return farmerService.getFarmerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Farmer createFarmer(@RequestBody Farmer farmer) {
        return farmerService.saveFarmer(farmer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Farmer> updateFarmer(@PathVariable Long id, @RequestBody Farmer farmerDetails) {
        try {
            return ResponseEntity.ok(farmerService.updateFarmer(id, farmerDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarmer(@PathVariable Long id) {
        try {
            farmerService.deleteFarmer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
