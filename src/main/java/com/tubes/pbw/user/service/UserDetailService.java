package com.tubes.pbw.user.service;

import com.tubes.pbw.user.model.UserDetail;
import com.tubes.pbw.user.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;

    @Autowired
    public UserDetailService(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    public Optional<UserDetail> findByEmail(String email) {
        return userDetailRepository.findByEmail(email);
    }

    public void save(UserDetail userDetail) {
        userDetailRepository.save(userDetail);
    }
}
