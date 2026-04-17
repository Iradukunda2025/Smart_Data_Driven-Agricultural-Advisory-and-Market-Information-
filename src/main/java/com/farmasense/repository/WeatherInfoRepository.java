package com.farmasense.repository;

import com.farmasense.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
    List<WeatherInfo> findByRegion(String region);
    List<WeatherInfo> findByRegionContainingIgnoreCase(String region);
    List<WeatherInfo> findByDate(LocalDate date);
    List<WeatherInfo> findByRegionContainingIgnoreCaseAndDate(String region, LocalDate date);
    java.util.Optional<WeatherInfo> findFirstByRegionAndDate(String region, LocalDate date);
}
