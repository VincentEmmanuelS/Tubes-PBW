package com.tubes.pbw.admin.controller;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tubes.pbw.admin.model.Admin;
import com.tubes.pbw.admin.service.AdminService;
import com.tubes.pbw.auth.RequiredRole;
import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@EnableAspectJAutoProxy
public class LoginAdminController {

    @Autowired
    private UserService userService;

    // @GetMapping("/admin")
    // public String loginAdmin() {
    //     return "admin/login";
    // }

    @GetMapping("/login_admin")
    public String loginAdmin(HttpSession session, Model model) {

        if (session.getAttribute("admin") != null) {
            return "redirect:/onboarding_admin";
        }
        return "admin/login";

    }

    // @GetMapping("/onboarding_admin")
    // public String onboardingAdmin(HttpSession session, Model model) {
    //     return "admin/onboarding";
    // }

    @GetMapping("/manage_member")
    @RequiredRole("admin")
    public String manageMember() {
        return "admin/manageMember";
    }

    // @GetMapping("/add_event")
    // public String addEvent() {
    //     return "admin/eventEntry";
    // }

    
    @PostMapping("/login_admin")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {

        Optional<User> adminOptional = userService.login(email, password);
        
        if (adminOptional.isPresent()) {
            User admin= adminOptional.get();

            session.setAttribute("user", admin);
            session.setAttribute("role", admin.getRole());
            session.setAttribute("email", admin.getEmail());

            return "redirect:/onboarding_admin";
        }
        else {
            model.addAttribute("status", "failed");
            return "admin/login";
        }

    }

    @GetMapping("/logout_admin")
    public String logoutAdmin(HttpSession session) {
        session.invalidate();
        return "redirect:/login_admin";
    }

}
