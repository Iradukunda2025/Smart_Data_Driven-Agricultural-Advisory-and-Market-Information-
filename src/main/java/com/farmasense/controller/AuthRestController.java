package com.farmasense.controller;

import com.farmasense.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API for Authentication (Signup / Login)
 * 
 * Used for Postman testing and external API integration.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ──────────────────────────────────────────
    // POST /api/auth/signup
    // Expects JSON: { "username": "...", "email": "...", "fullName": "...", "phoneNumber": "...", "password": "...", "role": "FARMER/VENDOR" }
    // ──────────────────────────────────────────
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.registerUser(
                request.get("username"),
                request.get("email"),
                request.get("fullName"),
                request.get("phoneNumber"),
                request.get("password"),
                request.get("role")
            );
            response.put("message", "User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.verifyOtp(request.get("username"), request.get("code"));
            response.put("message", "Account verified successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ──────────────────────────────────────────
    // POST /api/auth/login
    // Expects JSON: { "username": "...", "password": "..." }
    // ──────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.get("username"), request.get("password"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            response.put("message", "Login successful");
            response.put("username", authentication.getName());
            response.put("roles", authentication.getAuthorities().toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
