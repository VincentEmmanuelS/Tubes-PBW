package com.tubes.pbw.admin.service;

import com.tubes.pbw.admin.model.UserDetail;
import com.tubes.pbw.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    @Autowired
    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDetail> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public UserDetail toggleUserStatus(String email) {
        UserDetail user = userRepository.getUserByEmail(email);
        if (user != null) {
            boolean newStatus = !user.isActive(); 
            user.setActive(newStatus);
            return userRepository.updateUserStatus(user);   // Update status di DB
        }
        throw new RuntimeException("User not found with email: " + email);
    }
}