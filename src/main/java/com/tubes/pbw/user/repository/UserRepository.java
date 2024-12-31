package com.tubes.pbw.user.repository;

import java.util.Optional;

import com.tubes.pbw.user.model.User;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> findByEmail(String email);
} 