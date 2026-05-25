package com.example.HighWebDev.controller;

import com.example.HighWebDev.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String root() {
        return "redirect:/schedules";
    }

    @GetMapping("/auth/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
        return "auth/login";
    }

    @GetMapping("/auth/signup")
    public String signupPage() {
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         Model model) {
        try {
            memberService.signup(username, password);
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/signup";
        }
    }
}