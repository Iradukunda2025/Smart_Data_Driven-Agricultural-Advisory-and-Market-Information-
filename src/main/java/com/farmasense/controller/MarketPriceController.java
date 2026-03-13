package com.farmasense.controller;

import com.farmasense.model.MarketPrice;
import com.farmasense.service.MarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market-prices")
public class MarketPriceController {

    @Autowired
    private MarketPriceService marketPriceService;

    @GetMapping
    public List<MarketPrice> getAllMarketPrices() {
        return marketPriceService.getAllMarketPrices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketPrice> getMarketPriceById(@PathVariable Long id) {
        return marketPriceService.getMarketPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MarketPrice createMarketPrice(@RequestBody MarketPrice marketPrice) {
        return marketPriceService.saveMarketPrice(marketPrice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarketPrice> updateMarketPrice(@PathVariable Long id, @RequestBody MarketPrice marketPriceDetails) {
        try {
            return ResponseEntity.ok(marketPriceService.updateMarketPrice(id, marketPriceDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarketPrice(@PathVariable Long id) {
        try {
            marketPriceService.deleteMarketPrice(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
