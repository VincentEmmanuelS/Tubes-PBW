package com.tubes.pbw.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDetail {

    private String firstName;
    private String lastName;
    private LocalDate tanggalLahir;
    private String gender;
    private String region;
    private String email;
}
