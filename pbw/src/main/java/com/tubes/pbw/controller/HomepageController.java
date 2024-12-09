package com.tubes.pbw.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;

@Controller
public class HomepageController {
    
    @GetMapping("/home")
    public String homepage() {
        return "homepage";
    }

}
