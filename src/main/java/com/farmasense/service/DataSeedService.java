package com.farmasense.service;

import com.farmasense.model.*;
import com.farmasense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

@Component
public class DataSeedService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Autowired
    private AdvisoryRepository advisoryRepository;

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        Role farmerRole = createRoleIfNotFound("ROLE_FARMER");
        Role vendorRole = createRoleIfNotFound("ROLE_VENDOR");
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");

        // Seed Users
        createUserIfNotFound("farmer", "password", farmerRole);
        createUserIfNotFound("vendor", "password", vendorRole);
        createUserIfNotFound("admin", "admin", adminRole);

        // Seed Crops
        Crop maize = createCropIfNotFound("Maize", "Cereal grain");
        Crop beans = createCropIfNotFound("Beans", "Legume");
        Crop potato = createCropIfNotFound("Potato", "Tuber");
        Crop cassava = createCropIfNotFound("Cassava", "Root vegetable");

        // Seed Market Prices
        if (marketPriceRepository.count() == 0) {
            marketPriceRepository.save(new MarketPrice("Maize", 350.0, LocalDate.now(), null));
            marketPriceRepository.save(new MarketPrice("Beans", 800.0, LocalDate.now(), null));
            marketPriceRepository.save(new MarketPrice("Potato", 450.0, LocalDate.now(), null));
            marketPriceRepository.save(new MarketPrice("Cassava", 300.0, LocalDate.now(), null));
        }

        // Seed Advisory
        if (advisoryRepository.count() == 0) {
            advisoryRepository.save(new Advisory("Planting Alert", "Apply urea fertilizer this week for maize.", LocalDate.now(), null));
            advisoryRepository.save(new Advisory("Pest Warning", "Monitor beans for aphids.", LocalDate.now(), null));
            advisoryRepository.save(new Advisory("Maintenance", "Ensure proper earthing up for potato.", LocalDate.now(), null));
        }

        // Seed Weather Data
        if (weatherInfoRepository.count() == 0) {
            weatherInfoRepository.save(new WeatherInfo("Kigali", "Sunny", LocalDate.now(), null));
            weatherInfoRepository.save(new WeatherInfo("Musanze", "Rainy", LocalDate.now(), null));
            weatherInfoRepository.save(new WeatherInfo("Huye", "Cloudy", LocalDate.now(), null));
        }
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> roleRepository.save(new Role(name)));
    }

    private void createUserIfNotFound(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setRoles(new HashSet<>(Collections.singletonList(role)));
            userRepository.save(user);
        }
    }

    private Crop createCropIfNotFound(String name, String type) {
        // Assuming Crop has a findByName method or we just check blindly. 
        // For simplicity in this seed, we can check basic existence if repo allows or just save instructions.
        // Let's assume standard JPA.
        // Since I haven't seen CropRepository custom methods, I'll rely on findAll or just existing data.
        // To be safe and simple, let's just create them if count is 0, or simplistic check.
        // Actually best to add findByName to CropRepository if not checking existing.
        // I will trust that standard saving is fine for now or I'll add findByName to CropRepository in a moment.
        // For now, let's check if there's any crop with that name using stream if the list is small, or just add findByName.
        // I'll add findByName to CropRepository next step to be clean.
        return cropRepository.findByName(name).orElseGet(() -> cropRepository.save(new Crop(name, type, "All Year"))); // Default season
    }
}
