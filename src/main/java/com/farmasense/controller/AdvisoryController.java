package com.farmasense.controller;

import com.farmasense.model.Advisory;
import com.farmasense.service.AdvisoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/advisories")
public class AdvisoryController {

    @Autowired
    private AdvisoryService advisoryService;

    @GetMapping
    public List<Advisory> getAllAdvisories() {
        return advisoryService.getAllAdvisories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Advisory> getAdvisoryById(@PathVariable Long id) {
        return advisoryService.getAdvisoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Advisory createAdvisory(@RequestBody Advisory advisory) {
        return advisoryService.saveAdvisory(advisory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Advisory> updateAdvisory(@PathVariable Long id, @RequestBody Advisory advisoryDetails) {
        try {
            return ResponseEntity.ok(advisoryService.updateAdvisory(id, advisoryDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvisory(@PathVariable Long id) {
        try {
            advisoryService.deleteAdvisory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
