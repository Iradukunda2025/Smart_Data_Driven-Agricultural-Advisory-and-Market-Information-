package com.farmasense.controller;

import com.farmasense.model.MarketPrice;
import com.farmasense.service.MarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST API for Market Prices.
 *
 * Base URL: /api/v1/market-prices
 *
 * Endpoints:
 *   GET    /api/v1/market-prices              – list all prices
 *   GET    /api/v1/market-prices/search        – filter by crop and/or date range
 *   GET    /api/v1/market-prices/{id}          – get single price record
 *   POST   /api/v1/market-prices              – create new price record (ADMIN / VENDOR)
 *   PUT    /api/v1/market-prices/{id}          – update price record   (ADMIN / VENDOR)
 *   DELETE /api/v1/market-prices/{id}          – delete price record   (ADMIN only)
 */
@RestController
@RequestMapping("/api/v1/market-prices")
public class MarketPriceController {

    @Autowired
    private MarketPriceService marketPriceService;

    // ──────────────────────────────────────────
    // GET /api/v1/market-prices
    // Returns all market price records.
    // Accessible by ADMIN, VENDOR, FARMER.
    // ──────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<MarketPrice>> getAllMarketPrices() {
        return ResponseEntity.ok(marketPriceService.getAllMarketPrices());
    }

    // ──────────────────────────────────────────
    // GET /api/v1/market-prices/search
    //
    // Optional query parameters:
    //   crop  – filter by crop name     (case-insensitive, partial not supported)
    //   from  – start date  (yyyy-MM-dd)
    //   to    – end date    (yyyy-MM-dd)
    //
    // Examples:
    //   /api/v1/market-prices/search?crop=Maize
    //   /api/v1/market-prices/search?from=2026-01-01&to=2026-04-30
    //   /api/v1/market-prices/search?crop=Maize&from=2026-01-01&to=2026-04-30
    // ──────────────────────────────────────────
    @GetMapping("/search")
    public ResponseEntity<List<MarketPrice>> search(
            @RequestParam(required = false) String crop,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<MarketPrice> results = marketPriceService.getByFilters(crop, from, to);
        return ResponseEntity.ok(results);
    }

    // ──────────────────────────────────────────
    // GET /api/v1/market-prices/{id}
    // Returns one price record or 404 Not Found.
    // ──────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<MarketPrice> getMarketPriceById(@PathVariable Long id) {
        return marketPriceService.getMarketPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ──────────────────────────────────────────
    // POST /api/v1/market-prices
    // Creates a new market price record.
    // Requires ADMIN or VENDOR role.
    //
    // Request body (JSON):
    // {
    //   "crop": "Maize",
    //   "price": 350.0,
    //   "date": "2026-04-11",
    //   "associatedVendor": { "id": 1 }
    // }
    // ──────────────────────────────────────────
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDOR')")
    public ResponseEntity<MarketPrice> createMarketPrice(@RequestBody MarketPrice marketPrice) {
        MarketPrice created = marketPriceService.saveMarketPrice(marketPrice);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ──────────────────────────────────────────
    // PUT /api/v1/market-prices/{id}
    // Updates an existing price record.
    // Requires ADMIN or VENDOR role.
    //
    // Request body (JSON) — same structure as POST.
    // ──────────────────────────────────────────
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDOR')")
    public ResponseEntity<MarketPrice> updateMarketPrice(
            @PathVariable Long id,
            @RequestBody MarketPrice marketPriceDetails) {
        try {
            return ResponseEntity.ok(marketPriceService.updateMarketPrice(id, marketPriceDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ──────────────────────────────────────────
    // DELETE /api/v1/market-prices/{id}
    // Deletes a price record.
    // Requires ADMIN role only.
    // Returns 204 No Content on success.
    // ──────────────────────────────────────────
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMarketPrice(@PathVariable Long id) {
        try {
            marketPriceService.deleteMarketPrice(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
