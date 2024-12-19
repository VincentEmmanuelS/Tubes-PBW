package com.tubes.pbw.user;

import java.util.Optional;

public interface UserRepository {
    void save(User user) throws Exception;
    Optional<User> findByEmail(String email);
} 