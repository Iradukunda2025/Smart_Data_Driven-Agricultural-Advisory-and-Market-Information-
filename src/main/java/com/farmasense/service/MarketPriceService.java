package com.farmasense.service;

import com.farmasense.model.MarketPrice;
import com.farmasense.repository.MarketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarketPriceService {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private NotificationService notificationService;

    public List<MarketPrice> getAllMarketPrices() {
        return marketPriceRepository.findAll();
    }

    public Optional<MarketPrice> getMarketPriceById(Long id) {
        return marketPriceRepository.findById(id);
    }

    public MarketPrice saveMarketPrice(MarketPrice marketPrice) {
        MarketPrice saved = marketPriceRepository.save(marketPrice);
        notificationService.createNotification(
            "Market Update: " + saved.getCrop(),
            "Price for " + saved.getCrop() + " is currently " + saved.getPrice() + " RWF.",
            "VENDOR",
            "MARKET_UPDATE"
        );
        return saved;
    }

    public MarketPrice updateMarketPrice(Long id, MarketPrice marketPriceDetails) {
        MarketPrice marketPrice = marketPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarketPrice not found for id :: " + id));
        
        marketPrice.setCrop(marketPriceDetails.getCrop());
        marketPrice.setPrice(marketPriceDetails.getPrice());
        marketPrice.setDate(marketPriceDetails.getDate());
        marketPrice.setAssociatedVendor(marketPriceDetails.getAssociatedVendor());
        
        MarketPrice updated = marketPriceRepository.save(marketPrice);
        notificationService.createNotification(
            "Price Adjustment: " + updated.getCrop(),
            "New price for " + updated.getCrop() + ": " + updated.getPrice() + " RWF.",
            "VENDOR",
            "MARKET_UPDATE"
        );
        return updated;
    }

    public void deleteMarketPrice(Long id) {
        MarketPrice marketPrice = marketPriceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MarketPrice not found for id :: " + id));
        marketPriceRepository.delete(marketPrice);
    }
}
