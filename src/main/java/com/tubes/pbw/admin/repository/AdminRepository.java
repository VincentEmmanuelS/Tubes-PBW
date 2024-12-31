package com.tubes.pbw.admin.repository;

import java.util.Optional;

import com.tubes.pbw.admin.model.Admin;

public interface AdminRepository {
    Optional<Admin> findByEmail(String email);
}
