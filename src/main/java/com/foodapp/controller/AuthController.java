package com.foodapp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {

        Map<String, Object> response = new HashMap<>();

        response.put("username", auth.getName());

        response.put(
                "role",
                auth.getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority());

        return response;
    }
}
