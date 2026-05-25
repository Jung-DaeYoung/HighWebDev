package com.example.HighWebDev.config;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails != null;
    }

    @ModelAttribute("username")
    public String username(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails != null ? userDetails.getUsername() : "";
    }
}