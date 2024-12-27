package com.tubes.pbw.repository;

import java.util.Optional;

import com.tubes.pbw.model.User;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> findByEmail(String email);
} 