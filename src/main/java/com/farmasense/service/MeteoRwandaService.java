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

    public void syncWeatherData() {
        // Simulated sync logic from Meteo Rwanda API
        List<String> regions = Arrays.asList("Kigali", "Musanze", "Rubavu", "Karongi", "Huye");
        List<String> forecasts = Arrays.asList("Sunny", "Cloudy", "Light Rain", "Thunderstorms", "Partly Cloudy");
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
                data = new WeatherInfo(region, forecast, LocalDate.now(), null);
            }
            weatherInfoRepository.save(data);
        }

        notificationService.createNotification(
            "System: Weather Data Synced",
            "Latest weather updates have been pulled from Meteo Rwanda for all regional hubs.",
            "SYSTEM",
            "WEATHER_ALERT"
        );
    }
}
