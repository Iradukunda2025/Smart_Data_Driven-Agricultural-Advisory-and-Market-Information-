package com.farmasense.service;

import com.farmasense.model.WeatherInfo;
import com.farmasense.repository.WeatherInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherInfoService {

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    @Autowired
    private NotificationService notificationService;

    public List<WeatherInfo> getAllWeatherInfo() {
        return weatherInfoRepository.findAll();
    }

    public List<WeatherInfo> getWeatherByRegion(String region) {
        return weatherInfoRepository.findByRegion(region);
    }

    public Optional<WeatherInfo> getWeatherInfoById(Long id) {
        return weatherInfoRepository.findById(id);
    }

    public WeatherInfo saveWeatherInfo(WeatherInfo weatherInfo) {
        WeatherInfo saved = weatherInfoRepository.save(weatherInfo);
        notificationService.createNotification(
            "New Weather Update: " + saved.getRegion(),
            "Forecast: " + saved.getForecast() + " on " + saved.getDate(),
            "SYSTEM",
            "WEATHER_ALERT"
        );
        return saved;
    }

    public WeatherInfo updateWeatherInfo(Long id, WeatherInfo weatherDetails) {
        WeatherInfo weatherInfo = weatherInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeatherInfo not found for id :: " + id));
        
        weatherInfo.setRegion(weatherDetails.getRegion());
        weatherInfo.setForecast(weatherDetails.getForecast());
        weatherInfo.setDate(weatherDetails.getDate());
        weatherInfo.setAssociatedAdmin(weatherDetails.getAssociatedAdmin());
        
        WeatherInfo updated = weatherInfoRepository.save(weatherInfo);
        notificationService.createNotification(
            "Weather Forecast Changed: " + updated.getRegion(),
            "Updated Forecast: " + updated.getForecast(),
            "SYSTEM",
            "WEATHER_ALERT"
        );
        return updated;
    }

    public void deleteWeatherInfo(Long id) {
        weatherInfoRepository.deleteById(id);
    }
}
