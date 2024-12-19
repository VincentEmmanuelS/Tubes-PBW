package com.tubes.pbw.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import jakarta.validation.Valid;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String registerView(User user){
        return "signup";
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
            return "signup";
        }

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        Optional<User> existingUser = userService.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            bindingResult.rejectValue(
                "email", 
                "EmailRegistered", 
                "Email already exists"
            );
            return "signup";
        }

        boolean isRegistered = userService.register(user);
        if (isRegistered) {
            return "onboarding";
        }
        else {
            return "signup";
        }

    }

    // @GetMapping("/onboarding")
    // public String dashboardPage(HttpSession session, Model model){
    //     return "onboarding";
    // }
}
