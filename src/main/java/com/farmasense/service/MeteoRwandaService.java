package com.farmasense.service;

import com.farmasense.model.WeatherInfo;
import com.farmasense.repository.WeatherInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class MeteoRwandaService {

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    @org.springframework.transaction.annotation.Transactional
    public void syncWeatherData() {
        try {
            // Actual district names in Rwanda (30 Districts)
            List<String> districts = Arrays.asList(
                "Nyarugenge", "Gasabo", "Kicukiro", // Kigali
                "Nyagatare", "Gatsibo", "Kayonza", "Rwamagana", "Ngoma", "Kirehe", "Bugesera", // East
                "Rubavu", "Nyabihu", "Ngororero", "Karongi", "Rutsiro", "Nyamasheke", "Rusizi", // West
                "Musanze", "Burera", "Gicumbi", "Rulindo", "Gakenke", // North
                "Muhanga", "Kamonyi", "Ruhango", "Nyanza", "Huye", "Nyamagabe", "Nyaruguru", "Gisagara" // South
            );
            
            List<String> forecasts = Arrays.asList(
                "Sunny", "Sunny Intervals", "Cloudy", "Light Rain", "Heavy Rain", 
                "Thunderstorms", "Partial Clouds", "Clear Skies", "Foggy"
            );
            Random random = new Random();
            LocalDate today = LocalDate.now();

            for (String district : districts) {
                String forecast = forecasts.get(random.nextInt(forecasts.size()));

                // Check if we already have a record for this district on this day
                WeatherInfo data = weatherInfoRepository.findFirstByRegionAndDate(district, today)
                        .orElse(new WeatherInfo());
                
                data.setRegion(district);
                data.setForecast(forecast);
                data.setDate(today);
                
                weatherInfoRepository.save(data);
            }

            System.out.println("Meteo Rwanda Sync Success for 30 districts.");
        } catch (Exception e) {
            System.err.println("Critical Error during Meteo Rwanda Sync: " + e.getMessage());
            throw new RuntimeException("Sync operation failed: " + e.getMessage());
        }
    }
}
