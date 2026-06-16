package com.fidelity.mts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication endpoints.
 * Handles user login and credential validation.
 */
@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    /**
     * Authenticate user with Basic Auth credentials.
     * @return "Authenticated" if credentials are valid
     */
    @GetMapping("/auth")
    public ResponseEntity<String> authenticate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Check if user is authenticated
        if (auth != null && auth.isAuthenticated() && 
            !auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok("Authenticated");
        }
        
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    /**
     * Login endpoint for frontend.
     * Uses Spring Security's basic authentication.
     * @return User details if authenticated
     */
    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<String> login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && 
            !auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.ok("{\"message\":\"Login successful\",\"user\":\"" + 
                auth.getName() + "\"}");
        }
        
        return ResponseEntity.status(401).body("{\"message\":\"Invalid credentials\"}");
    }
}
