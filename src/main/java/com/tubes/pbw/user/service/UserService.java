package com.tubes.pbw.user.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tubes.pbw.user.model.User;
import com.tubes.pbw.user.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean register(User user) {
        // return true;

        try {

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;

        }

    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> login(String email, String password) {

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            User existingUser = user.get();

            if (!existingUser.getActive()) {
                return Optional.empty();
            }
    
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                // return user;
                return Optional.of(existingUser);
            }
        }

        // return null;
        return Optional.empty();
    }

}
