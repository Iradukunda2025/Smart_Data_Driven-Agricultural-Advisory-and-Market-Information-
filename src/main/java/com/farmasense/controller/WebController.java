package com.farmasense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Controller is working!";
    }

    @GetMapping("/api/ping")
    @ResponseBody
    public String ping() {
        return "FarmaSense System is Online and Active";
    }

    @GetMapping("/login")
    public String login(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @Autowired
    private com.farmasense.service.UserService userService;

    @Autowired
    private com.farmasense.repository.AdvisoryRepository advisoryRepository;

    @Autowired
    private com.farmasense.repository.MarketPriceRepository marketPriceRepository;

    @Autowired
    private com.farmasense.repository.WeatherInfoRepository weatherInfoRepository;

    @Autowired
    private com.farmasense.service.NotificationService notificationService;

    @Autowired
    private com.farmasense.repository.RoleRepository roleRepository;

    @Autowired
    private com.farmasense.service.MeteoRwandaService meteoRwandaService;

    @Autowired
    private com.farmasense.repository.UserRepository userRepository;

    @Autowired
    private com.farmasense.repository.FarmerRepository farmerRepository;

    @Autowired
    private com.farmasense.repository.VendorRepository vendorRepository;

    @Autowired
    private com.farmasense.repository.CropRepository cropRepository;

    @GetMapping("/dashboard")
    public String dashboard(org.springframework.security.core.Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String roles = authentication.getAuthorities().toString().toUpperCase();
                System.out.println("!!! LOGIN SUCCESS: " + authentication.getName() + " [ " + roles + " ]");
                
                if (roles.contains("ADMIN")) return "redirect:/dashboard/admin";
                if (roles.contains("VENDOR")) return "redirect:/dashboard/vendor";
                if (roles.contains("FARMER")) return "redirect:/dashboard/farmer";
            }
            return "redirect:/login";
        } catch (Exception e) {
            System.err.println("!!! REDIRECT ERROR: " + e.getMessage());
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/dashboard/farmer")
    public String farmerDashboard(org.springframework.security.core.Authentication authentication, org.springframework.ui.Model model) {
        try {
            String username = authentication.getName();
            com.farmasense.model.User user = userRepository.findByUsername(username).orElse(null);
            com.farmasense.model.Farmer farmer = null;
            
            if (user != null) {
                farmer = farmerRepository.findByContact(user.getPhoneNumber()).orElse(null);
            }

            java.util.List<com.farmasense.model.WeatherInfo> weatherRecords;
            if (farmer != null && farmer.getLocation() != null && !farmer.getLocation().equalsIgnoreCase("Not Specified")) {
                weatherRecords = weatherInfoRepository.findByRegionContainingIgnoreCase(farmer.getLocation());
                model.addAttribute("userRegion", farmer.getLocation());
            } else {
                weatherRecords = weatherInfoRepository.findAll();
                model.addAttribute("userRegion", "All Regions");
            }

            model.addAttribute("farmerProfile", farmer);
            model.addAttribute("advisories", advisoryRepository != null ? advisoryRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("marketPrices", marketPriceRepository != null ? marketPriceRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", weatherRecords);
            model.addAttribute("notifications", notificationService != null ? notificationService.getLatestNotifications() : java.util.Collections.emptyList());
            return "farmer_dashboard";
        } catch (Exception e) {
            System.err.println("!!! FARMER DASHBOARD ERROR: " + e.getMessage());
            model.addAttribute("advisories", java.util.Collections.emptyList());
            model.addAttribute("marketPrices", java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", java.util.Collections.emptyList());
            model.addAttribute("notifications", java.util.Collections.emptyList());
            return "farmer_dashboard";
        }
    }

    @GetMapping("/dashboard/vendor")
    public String vendorDashboard(org.springframework.security.core.Authentication authentication, org.springframework.ui.Model model) {
        try {
            String username = authentication.getName();
            com.farmasense.model.User user = userRepository.findByUsername(username).orElse(null);
            com.farmasense.model.Vendor vendor = null;
            
            if (user != null) {
                vendor = vendorRepository.findByContact(user.getPhoneNumber()).orElse(null);
            }

            java.util.List<?> myPrices = java.util.Collections.emptyList();
            if (vendor != null) {
                myPrices = marketPriceRepository.findByAssociatedVendor(vendor);
            }

            model.addAttribute("vendorProfile", vendor);
            model.addAttribute("marketPrices", myPrices);
            model.addAttribute("allMarketPrices", marketPriceRepository.findAll());
            model.addAttribute("notifications", notificationService != null ? notificationService.getLatestNotifications() : java.util.Collections.emptyList());
            
            // Shared fragment stabilizers
            model.addAttribute("users", java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", java.util.Collections.emptyList());
            model.addAttribute("advisories", java.util.Collections.emptyList());
            
            return "vendor_dashboard";
        } catch (Exception e) {
            System.err.println("!!! VENDOR DASHBOARD ERROR: " + e.getMessage());
            model.addAttribute("marketPrices", java.util.Collections.emptyList());
            model.addAttribute("notifications", java.util.Collections.emptyList());
            model.addAttribute("users", java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", java.util.Collections.emptyList());
            model.addAttribute("advisories", java.util.Collections.emptyList());
            return "vendor_dashboard";
        }
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(org.springframework.ui.Model model) {
        try {
            // Metrics
            long userCount = userRepository.count();
            long farmerCount = farmerRepository.count();
            long vendorCount = vendorRepository.count();
            long advisoryCount = advisoryRepository.count();
            long weatherCount = weatherInfoRepository.count();

            model.addAttribute("userCount", userCount);
            model.addAttribute("farmerCount", farmerCount);
            model.addAttribute("vendorCount", vendorCount);
            model.addAttribute("advisoryCount", advisoryCount);
            model.addAttribute("weatherCount", weatherCount);

            model.addAttribute("users", 
                userRepository != null ? userRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("marketPrices", 
                marketPriceRepository != null ? marketPriceRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", 
                weatherInfoRepository != null ? weatherInfoRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("advisories", 
                advisoryRepository != null ? advisoryRepository.findAll() : java.util.Collections.emptyList());
            model.addAttribute("notifications", 
                notificationService != null ? notificationService.getLatestNotifications() : java.util.Collections.emptyList());
            return "admin_dashboard";
        } catch (Exception e) {
            System.err.println("!!! ADMIN DASHBOARD ERROR: " + e.getMessage());
            model.addAttribute("users", java.util.Collections.emptyList());
            model.addAttribute("marketPrices", java.util.Collections.emptyList());
            model.addAttribute("weatherRecords", java.util.Collections.emptyList());
            model.addAttribute("advisories", java.util.Collections.emptyList());
            model.addAttribute("notifications", java.util.Collections.emptyList());
            return "admin_dashboard";
        }
    }

    @GetMapping("/advisories")
    public String viewAdvisories(org.springframework.ui.Model model) {
        model.addAttribute("advisories", advisoryRepository != null ? advisoryRepository.findAll() : java.util.Collections.emptyList());
        model.addAttribute("crops", cropRepository != null ? cropRepository.findAll() : java.util.Collections.emptyList());
        model.addAttribute("marketPrices", java.util.Collections.emptyList());
        model.addAttribute("weatherRecords", java.util.Collections.emptyList());
        model.addAttribute("notifications", java.util.Collections.emptyList());
        return "advisories";
    }

    @GetMapping("/weather")
    public String manageWeather(@RequestParam(required = false) String search, 
                                @RequestParam(required = false) String date,
                                org.springframework.ui.Model model) {
        java.util.List<com.farmasense.model.WeatherInfo> weatherRecords;
        
        try {
            if (search != null && !search.isEmpty() && date != null && !date.isEmpty()) {
                weatherRecords = weatherInfoRepository.findByRegionContainingIgnoreCaseAndDate(search, java.time.LocalDate.parse(date));
            } else if (search != null && !search.isEmpty()) {
                weatherRecords = weatherInfoRepository.findByRegionContainingIgnoreCase(search);
            } else if (date != null && !date.isEmpty()) {
                weatherRecords = weatherInfoRepository.findByDate(java.time.LocalDate.parse(date));
            } else {
                weatherRecords = weatherInfoRepository.findAll();
            }
        } catch (Exception e) {
            weatherRecords = weatherInfoRepository.findAll();
        }

        model.addAttribute("weatherRecords", weatherRecords);
        model.addAttribute("weatherData", weatherRecords); // Keep both for safety
        model.addAttribute("search", search);
        model.addAttribute("date", date);
        
        // Stabilizers for fragments
        model.addAttribute("marketPrices", java.util.Collections.emptyList());
        model.addAttribute("advisories", java.util.Collections.emptyList());
        model.addAttribute("notifications", java.util.Collections.emptyList());
        
        return "weather";
    }

    @GetMapping("/market")
    public String manageMarket(org.springframework.ui.Model model) {
        model.addAttribute("marketPrices", marketPriceRepository != null ? marketPriceRepository.findAll() : java.util.Collections.emptyList());
        model.addAttribute("crops", cropRepository != null ? cropRepository.findAll() : java.util.Collections.emptyList());
        model.addAttribute("advisories", java.util.Collections.emptyList());
        model.addAttribute("weatherRecords", java.util.Collections.emptyList());
        model.addAttribute("notifications", java.util.Collections.emptyList());
        return "market";
    }

    @GetMapping("/vendor/my-prices")
    public String vendorStore(org.springframework.ui.Model model) {
        model.addAttribute("marketPrices", marketPriceRepository.findAll()); 
        model.addAttribute("notifications", notificationService.getLatestNotifications());
        return "vendor_store";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/weather/sync")
    public String syncWeather(java.security.Principal principal) {
        try {
            meteoRwandaService.syncWeatherData();
            return "redirect:/weather?synced";
        } catch (Exception e) {
            String rawMsg = e.getMessage() != null ? e.getMessage() : "Sync failed";
            String encodedMsg = "error";
            try {
                encodedMsg = java.net.URLEncoder.encode(rawMsg, "UTF-8");
            } catch (Exception ex) {}
            return "redirect:/weather?error=" + encodedMsg;
        }
    }

    @GetMapping("/signup")
    public String signup(org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard";
        }
        return "signup";
    }

    @org.springframework.web.bind.annotation.PostMapping("/signup")
    public String registerUser(@org.springframework.web.bind.annotation.RequestParam String username,
                               @org.springframework.web.bind.annotation.RequestParam String email,
                               @org.springframework.web.bind.annotation.RequestParam String fullName,
                               @org.springframework.web.bind.annotation.RequestParam String phoneNumber,
                               @org.springframework.web.bind.annotation.RequestParam String password,
                               @org.springframework.web.bind.annotation.RequestParam String role,
                               org.springframework.ui.Model model) {
        try {
            userService.registerUser(username, email, fullName, phoneNumber, password, role);
            // Redirect to OTP verification page after successful registration
            return "redirect:/verify-otp?username=" + username + "&email=" + email;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtpPage(@RequestParam String username, @RequestParam(required = false) String email, org.springframework.ui.Model model) {
        model.addAttribute("username", username);
        model.addAttribute("email", email);
        return "verify_otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String username, @RequestParam String code, org.springframework.ui.Model model) {
        try {
            userService.verifyOtp(username, code);
            return "redirect:/login?verified";
        } catch (Exception e) {
            model.addAttribute("username", username);
            model.addAttribute("error", e.getMessage());
            return "verify_otp";
        }
    }
    @PostMapping("/admin/advisories/add")
    public String addAdvisory(@RequestParam String title, @RequestParam String description, @RequestParam String date, java.security.Principal principal) {
        try {
            com.farmasense.model.Advisory advisory = new com.farmasense.model.Advisory();
            advisory.setTitle(title);
            advisory.setDescription(description);
            if (date != null && !date.isEmpty()) {
                advisory.setDate(java.time.LocalDate.parse(date));
            } else {
                advisory.setDate(java.time.LocalDate.now());
            }
            advisoryRepository.save(advisory);
            
            String updater = (principal != null) ? principal.getName() : "Expert";
            if (notificationService != null) {
                notificationService.createNotification("New Advisory", "A new agricultural advisory has been posted: " + title + " (by " + updater + ")", "ADMIN", "ADVISORY");
            }
            return "redirect:/advisories?success";
        } catch (Exception e) {
            return "redirect:/advisories?error";
        }
    }

    @PostMapping("/admin/advisories/delete")
    public String deleteAdvisory(@RequestParam Long id) {
        try {
            if (advisoryRepository.existsById(id)) {
                advisoryRepository.deleteById(id);
                return "redirect:/advisories?deleted";
            }
        } catch (Exception e) {
            return "redirect:/advisories?error";
        }
        return "redirect:/advisories";
    }

    @PostMapping("/admin/advisories/update")
    public String updateAdvisory(@RequestParam Long id, @RequestParam String title, @RequestParam String description, @RequestParam String date) {
        try {
            com.farmasense.model.Advisory advisory = advisoryRepository.findById(id).orElseThrow();
            advisory.setTitle(title);
            advisory.setDescription(description);
            advisory.setDate(java.time.LocalDate.parse(date));
            advisoryRepository.save(advisory);
            return "redirect:/advisories?updated";
        } catch (Exception e) {
            return "redirect:/advisories?error=" + (e.getMessage() != null ? e.getMessage() : "Update failed");
        }
    }

    @PostMapping("/admin/weather/add")
    public String addWeather(@RequestParam String region, @RequestParam String forecast, @RequestParam String date, java.security.Principal principal) {
        try {
            com.farmasense.model.WeatherInfo weather = new com.farmasense.model.WeatherInfo();
            weather.setRegion(region);
            weather.setForecast(forecast);
            weather.setDate(java.time.LocalDate.parse(date));
            weatherInfoRepository.save(weather);
            return "redirect:/weather?success";
        } catch (Exception e) {
            return "redirect:/weather?error=" + (e.getMessage() != null ? e.getMessage() : "Unknown error");
        }
    }

    @PostMapping("/admin/weather/update")
    public String updateWeather(@RequestParam Long id, @RequestParam String region, @RequestParam String forecast, @RequestParam String date) {
        try {
            com.farmasense.model.WeatherInfo weather = weatherInfoRepository.findById(id).orElseThrow();
            weather.setRegion(region);
            weather.setForecast(forecast);
            weather.setDate(java.time.LocalDate.parse(date));
            weatherInfoRepository.save(weather);
            return "redirect:/weather?updated";
        } catch (Exception e) {
            return "redirect:/weather?error=" + (e.getMessage() != null ? e.getMessage() : "Update failed");
        }
    }

    @PostMapping("/admin/weather/delete")
    public String deleteWeather(@RequestParam Long id) {
        try {
            if (weatherInfoRepository.existsById(id)) {
                weatherInfoRepository.deleteById(id);
                return "redirect:/weather?deleted";
            }
        } catch (Exception e) {
            return "redirect:/weather?error";
        }
        return "redirect:/weather";
    }

    @PostMapping("/admin/market/add")
    public String addMarketPrice(@RequestParam String crop, @RequestParam Double price, @RequestParam String date, java.security.Principal principal) {
        try {
            com.farmasense.model.MarketPrice marketPrice = new com.farmasense.model.MarketPrice();
            marketPrice.setCrop(crop);
            marketPrice.setPrice(price);
            if (date != null && !date.isEmpty()) {
                marketPrice.setDate(java.time.LocalDate.parse(date));
            } else {
                marketPrice.setDate(java.time.LocalDate.now());
            }
            marketPriceRepository.save(marketPrice);
            return "redirect:/market?success";
        } catch (Exception e) {
            return "redirect:/market?error";
        }
    }

    @PostMapping("/admin/market/delete")
    public String deleteMarketPrice(@RequestParam Long id) {
        try {
            if (marketPriceRepository.existsById(id)) {
                marketPriceRepository.deleteById(id);
                return "redirect:/market?deleted";
            }
        } catch (Exception e) {
            return "redirect:/market?error";
        }
        return "redirect:/market";
    }

    @PostMapping("/admin/market/update")
    public String updateMarketPrice(@RequestParam Long id, @RequestParam String crop, @RequestParam Double price, @RequestParam String date) {
        try {
            com.farmasense.model.MarketPrice marketPrice = marketPriceRepository.findById(id).orElseThrow();
            marketPrice.setCrop(crop);
            marketPrice.setPrice(price);
            if (date != null && !date.isEmpty()) {
                marketPrice.setDate(java.time.LocalDate.parse(date));
            }
            marketPriceRepository.save(marketPrice);
            return "redirect:/market?updated";
        } catch (Exception e) {
            return "redirect:/market?error";
        }
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam Long id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                return "redirect:/admin/users?deleted";
            }
        } catch (Exception e) {
            return "redirect:/admin/users?error";
        }
        return "redirect:/admin/users";
    }
}