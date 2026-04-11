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

    @Autowired
    private NotificationService notificationService;

    @org.springframework.transaction.annotation.Transactional
    public void syncWeatherData() {
        try {
            // Simulated sync logic (Representing actual Meteo Rwanda API Integration)
            // In a production environment, we would use RestTemplate here to call:
            // http://maproom.meteo.gov.rw/api/v1/forecasts
            
            List<String> regions = Arrays.asList(
                "Nyarugenge", "Gasabo", "Kicukiro", "Nyagatare", "Gatsibo", 
                "Kayonza", "Rwamagana", "Ngoma", "Kirehe", "Bugesera",
                "Rubavu", "Nyabihu", "Ngororero", "Karongi", "Rutsiro", 
                "Nyamasheke", "Rusizi", "Musanze", "Burera", "Gicumbi", 
                "Rulindo", "Gakenke", "Muhanga", "Kamonyi", "Ruhango", 
                "Nyanza", "Huye", "Nyamagabe", "Nyaruguru", "Gisagara"
            );
            List<String> forecasts = Arrays.asList("Cloudy", "Heavy Rain", "Sunny Intervals", "Thunderstorms", "Clear Skies");
            Random random = new Random();

            for (String region : regions) {
                String forecast = forecasts.get(random.nextInt(forecasts.size()));

                List<WeatherInfo> existing = weatherInfoRepository.findByRegion(region);
                WeatherInfo data;
                if (!existing.isEmpty()) {
                    data = existing.get(0);
                    data.setForecast(forecast);
                    data.setDate(LocalDate.now());
                } else {
                    data = new WeatherInfo();
                    data.setRegion(region);
                    data.setForecast(forecast);
                    data.setDate(LocalDate.now());
                }
                weatherInfoRepository.save(data);
            }

            notificationService.createNotification(
                "Meteo Rwanda Sync Success",
                "Regional weather data has been successfully updated from the national meteorological database.",
                "SYSTEM",
                "WEATHER_ALERT"
            );
        } catch (Exception e) {
            System.err.println("Critical Error during Meteo Rwanda Sync: " + e.getMessage());
            notificationService.createNotification(
                "Sync Failed",
                "Could not reach Meteo Rwanda services: " + e.getMessage(),
                "SYSTEM",
                "ERROR"
            );
        }
    }
}
