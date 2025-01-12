package com.tubes.pbw.user.repository;

import java.util.Optional;
import com.tubes.pbw.user.model.UserDetail;

public interface UserDetailRepository {
    Optional<UserDetail> findByEmail(String email);
    void save(UserDetail userDetail);
}
