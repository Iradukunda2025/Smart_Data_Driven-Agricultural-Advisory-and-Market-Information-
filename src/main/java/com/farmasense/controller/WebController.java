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

    @GetMapping("/login")
    public String login() {
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
        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FARMER"))) {
            return "redirect:/dashboard/farmer";
        } else if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDOR"))) {
            return "redirect:/dashboard/vendor";
        } else if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/dashboard/admin";
        }
        return "dashboard"; // Fallback
    }

    @GetMapping("/dashboard/farmer")
    public String farmerDashboard(org.springframework.ui.Model model) {
        model.addAttribute("advisories", advisoryRepository.findAll());
        model.addAttribute("marketPrices", marketPriceRepository.findAll());
        model.addAttribute("weatherData", weatherInfoRepository.findAll());
        model.addAttribute("notifications", notificationService.getLatestNotifications());
        return "farmer_dashboard";
    }

    @GetMapping("/dashboard/vendor")
    public String vendorDashboard(org.springframework.ui.Model model) {
        model.addAttribute("marketPrices", marketPriceRepository.findAll());
        return "vendor_dashboard";
    }

    @GetMapping("/dashboard/admin")
    public String adminDashboard(org.springframework.ui.Model model) {
        model.addAttribute("weatherData", weatherInfoRepository.findAll());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalAdvisories", advisoryRepository.count());
        return "admin_dashboard";
    }

    @GetMapping("/admin/users")
    public String manageUsers(org.springframework.ui.Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "users";
    }

    @GetMapping("/admin/advisories")
    public String manageAdvisories(org.springframework.ui.Model model) {
        model.addAttribute("advisories", advisoryRepository.findAll());
        model.addAttribute("crops", cropRepository.findAll());
        return "advisories";
    }

    @GetMapping("/weather")
    public String manageWeather(org.springframework.ui.Model model) {
        model.addAttribute("weatherData", weatherInfoRepository.findAll());
        return "weather";
    }

    @GetMapping("/market")
    public String manageMarket(org.springframework.ui.Model model) {
        model.addAttribute("marketPrices", marketPriceRepository.findAll());
        model.addAttribute("crops", cropRepository.findAll());
        return "market";
    }

    @GetMapping("/vendor/my-prices")
    public String vendorStore(org.springframework.ui.Model model) {
        model.addAttribute("marketPrices", marketPriceRepository.findAll()); // Ideally filtered by vendor, but using all for now until relationship is deeper
        model.addAttribute("crops", cropRepository.findAll());
        return "vendor_store";
    }

    @org.springframework.web.bind.annotation.PostMapping("/admin/weather/sync")
    public String syncWeather() {
        meteoRwandaService.syncWeatherData();
        return "redirect:/dashboard/admin?synced";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @org.springframework.web.bind.annotation.PostMapping("/signup")
    public String registerUser(@org.springframework.web.bind.annotation.RequestParam String username,
                               @org.springframework.web.bind.annotation.RequestParam String password,
                               @org.springframework.web.bind.annotation.RequestParam String role) {
        userService.registerUser(username, password, role);
        return "redirect:/login?success";
    }
    @PostMapping("/admin/advisories/add")
    public String addAdvisory(@RequestParam String title, @RequestParam String description, @RequestParam String date) {
        com.farmasense.model.Advisory advisory = new com.farmasense.model.Advisory();
        advisory.setTitle(title);
        advisory.setDescription(description);
        advisory.setDate(java.time.LocalDate.parse(date));
        advisoryRepository.save(advisory);
        return "redirect:/admin/advisories?success";
    }

    @PostMapping("/admin/advisories/delete")
    public String deleteAdvisory(@RequestParam Long id) {
        advisoryRepository.deleteById(id);
        return "redirect:/admin/advisories?deleted";
    }

    @PostMapping("/admin/weather/add")
    public String addWeather(@RequestParam String region, @RequestParam String forecast, @RequestParam String date) {
        com.farmasense.model.WeatherInfo weather = new com.farmasense.model.WeatherInfo();
        weather.setRegion(region);
        weather.setForecast(forecast);
        weather.setDate(java.time.LocalDate.parse(date));
        weatherInfoRepository.save(weather);
        return "redirect:/weather?success";
    }

    @PostMapping("/admin/weather/delete")
    public String deleteWeather(@RequestParam Long id) {
        weatherInfoRepository.deleteById(id);
        return "redirect:/weather?deleted";
    }

    @PostMapping("/admin/market/add")
    public String addMarketPrice(@RequestParam String crop, @RequestParam Double price, @RequestParam String date) {
        com.farmasense.model.MarketPrice marketPrice = new com.farmasense.model.MarketPrice();
        marketPrice.setCrop(crop);
        marketPrice.setPrice(price);
        marketPrice.setDate(java.time.LocalDate.parse(date));
        marketPriceRepository.save(marketPrice);
        return "redirect:/market?success";
    }

    @PostMapping("/admin/market/delete")
    public String deleteMarketPrice(@RequestParam Long id) {
        marketPriceRepository.deleteById(id);
        return "redirect:/market?deleted";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users?deleted";
    }
}