package com.example.TaskManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Endpoint: GET /api/auth/me
    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        // Returns the username of the logged-in user
        return ResponseEntity.ok(principal.getName());
    }
}
