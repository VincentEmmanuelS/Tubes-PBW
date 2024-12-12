package com.tubes.pbw;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    
    @GetMapping("/main")
    public String ShowMain(){
        return "MainPage";
    }
    
    @GetMapping("/signup")
    public String LoginPages(){
        return "Signup";
    }
}
