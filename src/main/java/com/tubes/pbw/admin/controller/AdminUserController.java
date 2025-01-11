package com.tubes.pbw.admin.controller;

import com.tubes.pbw.admin.model.UserDetail;
import com.tubes.pbw.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class AdminUserController {

    private final AdminUserService userService;

    @Autowired
    public AdminUserController(AdminUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDetail>> getAllUsers() {
        List<UserDetail> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Endpoint buat update status active user
    @PatchMapping("/{email}/status")
    public ResponseEntity<UserDetail> toggleUserStatus(@PathVariable("email") String email) {
        UserDetail updatedUser = userService.toggleUserStatus(email);
        return ResponseEntity.ok(updatedUser);
    }
}