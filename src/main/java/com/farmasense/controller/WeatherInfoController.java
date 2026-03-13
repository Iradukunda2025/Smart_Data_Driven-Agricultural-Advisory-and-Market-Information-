package com.farmasense.controller;

import com.farmasense.model.WeatherInfo;
import com.farmasense.service.WeatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherInfoController {

    @Autowired
    private WeatherInfoService weatherInfoService;

    @GetMapping
    public List<WeatherInfo> getAllWeatherInfo() {
        return weatherInfoService.getAllWeatherInfo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherInfo> getWeatherInfoById(@PathVariable Long id) {
        return weatherInfoService.getWeatherInfoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WeatherInfo createWeatherInfo(@RequestBody WeatherInfo weatherInfo) {
        return weatherInfoService.saveWeatherInfo(weatherInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherInfo> updateWeatherInfo(@PathVariable Long id, @RequestBody WeatherInfo weatherDetails) {
        try {
            return ResponseEntity.ok(weatherInfoService.updateWeatherInfo(id, weatherDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWeatherInfo(@PathVariable Long id) {
        weatherInfoService.deleteWeatherInfo(id);
        return ResponseEntity.ok().build();
    }
}
