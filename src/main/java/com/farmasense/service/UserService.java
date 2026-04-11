package com.farmasense.service;
 
import com.farmasense.model.*;
import com.farmasense.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.regex.Pattern;
 
@Service
public class UserService {
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private RoleRepository roleRepository;
 
    @Autowired
    private FarmerRepository farmerRepository;
 
    @Autowired
    private VendorRepository vendorRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;
 
    @Autowired
    private EmailService emailService;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
 
    @Transactional
    public void registerUser(String username, String email, String fullName, String phoneNumber, String password, String roleName) {
        // Advanced Validation
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username '" + username + "' is already taken!");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email '" + email + "' is already registered!");
        }
        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            throw new RuntimeException("Invalid email format!");
        }
        if (password.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long!");
        }
 
        // Create User (Enabled immediately as OTP is removed)
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true); // ENABLED immediately
        user.setVerified(true); // VERIFIED immediately
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
 
        String normalizedRole = roleName.toUpperCase();
        if (!normalizedRole.startsWith("ROLE_")) {
            normalizedRole = "ROLE_" + normalizedRole;
        }
        final String finalRoleName = normalizedRole;
        Role role = roleRepository.findByName(finalRoleName)
                .orElseGet(() -> roleRepository.save(new Role(finalRoleName)));
  
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        userRepository.save(user);
  
        // Workflow: Create Profile based on role
        if (finalRoleName.contains("FARMER")) {
            Farmer farmer = new Farmer();
            farmer.setName(fullName);
            farmer.setContact(phoneNumber);
            farmer.setLocation("Not Specified");
            farmerRepository.save(farmer);
        } else if (finalRoleName.contains("VENDOR")) {
            Vendor vendor = new Vendor();
            vendor.setName(fullName);
            vendor.setContact(phoneNumber);
            vendorRepository.save(vendor);
        }
    }
 
    @Transactional
    public void verifyOtp(String username, String code) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
 
        if (user.isVerified()) {
            throw new RuntimeException("Account already verified!");
        }
 
        if (user.getVerificationCodeExpiry().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("OTP code has expired!");
        }
 
        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid OTP code!");
        }
 
        user.setVerified(true);
        user.setEnabled(true);
        userRepository.save(user);
    }
}
