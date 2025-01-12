package com.tubes.pbw.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import jakarta.servlet.http.HttpSession;

import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.service.UserService;

@Controller
@EnableAspectJAutoProxy
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homepage() {
        return "user/homepage";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {

        if (session.getAttribute("user") != null) {
            return "redirect:/onboarding";
        }
        return "user/login";

    }

    // @GetMapping("/onboarding")
    // // @RequiredRole("user")
    // public String dashboardPage(HttpSession session, Model model) {
        
    //     return "user/onboarding";

    // }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {

        // User user = userService.login(email, password);
        Optional<User> userOptional = userService.login(email, password);

        if (userOptional.isPresent()) {
            // session.setAttribute("user", user.get());
            User user = userOptional.get();
            // System.out.println(user);

            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            return "redirect:/onboarding";
        }
        else {
            // model.addAttribute("status", "failed");
            model.addAttribute("accountLocked", true);
            return "user/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();       // Deleting session
        return "redirect:/";

    }

}
