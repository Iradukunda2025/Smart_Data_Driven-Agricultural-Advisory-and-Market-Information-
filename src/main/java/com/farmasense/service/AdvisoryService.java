package com.farmasense.service;

import com.farmasense.model.*;
import com.farmasense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdvisoryService {

    @Autowired
    private AdvisoryRepository advisoryRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    public List<Advisory> getAllAdvisories() {
        return advisoryRepository.findAll();
    }
    
    public List<Advisory> generateAdvisoriesForFarmer(String region) {
         List<Advisory> advisories = new ArrayList<>();
         List<WeatherInfo> weatherList = weatherInfoRepository.findByRegion(region);
         
         if (weatherList.isEmpty()) return advisories;

         WeatherInfo latestWeather = weatherList.get(0);
         
         // Logic based on forecast text since rainfall/temp were removed in strict alignment
         if (latestWeather.getForecast().toLowerCase().contains("rain")) {
             advisories.add(new Advisory("Weather Alert", "Rain expected in " + region + ". Adjust irrigation.", LocalDate.now(), null));
         } else if (latestWeather.getForecast().toLowerCase().contains("sunny")) {
             advisories.add(new Advisory("Weather Alert", "High sun exposure. Ensure proper mulching.", LocalDate.now(), null));
         }
         
         return advisories;
    }

    public List<MarketPrice> getMarketPrices() {
        return marketPriceRepository.findAll();
    }

    public Optional<Advisory> getAdvisoryById(Long id) {
        return advisoryRepository.findById(id);
    }

    public Advisory saveAdvisory(Advisory advisory) {
        Advisory saved = advisoryRepository.save(advisory);
        notificationService.createNotification(
            "New Advisory: " + saved.getTitle(),
            saved.getDescription(),
            "ADMIN",
            "ADVISORY"
        );
        return saved;
    }

    public Advisory updateAdvisory(Long id, Advisory advisoryDetails) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found for id :: " + id));
        
        advisory.setTitle(advisoryDetails.getTitle());
        advisory.setDescription(advisoryDetails.getDescription());
        advisory.setDate(advisoryDetails.getDate());
        advisory.setAssociatedAdmin(advisoryDetails.getAssociatedAdmin());
        
        Advisory updated = advisoryRepository.save(advisory);
        notificationService.createNotification(
            "Updated Advisory: " + updated.getTitle(),
            updated.getDescription(),
            "ADMIN",
            "ADVISORY"
        );
        return updated;
    }

    public void deleteAdvisory(Long id) {
        Advisory advisory = advisoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Advisory not found for id :: " + id));
        advisoryRepository.delete(advisory);
    }
}
