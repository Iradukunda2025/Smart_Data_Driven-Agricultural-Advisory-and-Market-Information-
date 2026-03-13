package com.farmasense.service;

import com.farmasense.model.*;
import com.farmasense.repository.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DataInitializationService {

    @Autowired
    private CropRepository cropRepository;
    @Autowired
    private FarmerRepository farmerRepository;
    @Autowired
    private MarketPriceRepository marketPriceRepository;
    @Autowired
    private WeatherInfoRepository weatherInfoRepository;
    @Autowired
    private AdvisoryRepository advisoryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initData() {
        // Initialize Roles
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role userRole = createRoleIfNotFound("ROLE_USER");
        Role farmerRole = createRoleIfNotFound("ROLE_FARMER");
        Role vendorRole = createRoleIfNotFound("ROLE_VENDOR");

        // Initialize Crops
        Crop wheat = createCropIfNotFound("Wheat", "Cereal", "Winter");
        Crop maize = createCropIfNotFound("Maize", "Cereal", "Summer");
        Crop tomato = createCropIfNotFound("Tomato", "Vegetable", "Spring");

        // Initialize Farmer
        if (farmerRepository.findByContact("0788123456").isEmpty()) {
            Farmer farmer1 = new Farmer("Jean Pierre", "Kigali", "0788123456");
            farmerRepository.save(farmer1);
        }

        // Initialize Weather Info
        if (weatherInfoRepository.count() == 0) {
            weatherInfoRepository.save(new WeatherInfo("Kigali", "Partly Cloudy", LocalDate.now(), null));
            weatherInfoRepository.save(new WeatherInfo("Musanze", "Rainy", LocalDate.now(), null));
        }

        // Initialize Market Prices
        if (marketPriceRepository.count() == 0) {
            marketPriceRepository.save(new MarketPrice("Wheat", 500.0, LocalDate.now(), null));
            marketPriceRepository.save(new MarketPrice("Maize", 350.0, LocalDate.now(), null));
            marketPriceRepository.save(new MarketPrice("Tomato", 800.0, LocalDate.now(), null));
        }

        // Initialize Advisories
        if (advisoryRepository.count() == 0) {
            advisoryRepository.save(new Advisory("Optimal Planting", "Optimal time for planting maize in Northern Province.", LocalDate.now(), null));
            advisoryRepository.save(new Advisory("Pest Control", "Monitor for rust disease in wheat due to recent humidity.", LocalDate.now().minusDays(1), null));
            advisoryRepository.save(new Advisory("Marketing Info", "Tomato prices are high in Musanze, consider harvesting.", LocalDate.now(), null));
        }

        // Initialize Admin User
        if (userRepository.findByUsername("admin").isEmpty()) {
            java.util.Set<Role> roles = new java.util.HashSet<>();
            roles.add(adminRole);
            User admin = new User("admin", passwordEncoder.encode("admin123"), roles);
            userRepository.save(admin);
        }
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> roleRepository.save(new Role(name)));
    }

    private Crop createCropIfNotFound(String name, String type, String season) {
        return cropRepository.findByName(name)
                .orElseGet(() -> cropRepository.save(new Crop(name, type, season)));
    }
}
