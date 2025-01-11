package com.tubes.pbw.user.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String registerView(User user){
        return "user/signup";
    }

    @PostMapping("/signup")
    public String register(@Validated User user, BindingResult bindingResult) {

        user.setRole("user");

        if (!user.getPassword().equals(user.getConfirmpassword())) {
            bindingResult.rejectValue(
                "confirmpassword", 
                "PasswordMismatch", 
                "Password do not match"
            );
            return "user/signup";
        }

        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        // memastikan input pengguna aman dari SQL injection atau XSS
        if (user.getEmail().contains("<") || user.getEmail().contains(">")) {
            bindingResult.rejectValue(
                "email",
                "InvalidEmailFormat",
                "Invalid email format"
            );
            return "user/signup";
        }

        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            bindingResult.rejectValue(
                "email", 
                "EmailRegistered", 
                "Email already exists"
            );
            return "user/signup";
        }

        boolean isRegistered = userService.register(user);
        if (isRegistered) {
            return "redirect:/onboarding";
        }
        else {
            return "user/signup";
        }

    }

    // @GetMapping("/onboarding")
    // public String dashboardPage(HttpSession session, Model model){
    //     return "onboarding";
    // }
}
