package com.tubes.pbw.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tubes.pbw.auth.RequiredRole;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    @RequiredRole("user")
    public String profilePage(){
        return "user/profile";
    }
}
