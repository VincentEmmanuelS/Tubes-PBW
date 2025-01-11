package com.tubes.pbw.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String region;
    private String email;
    private boolean active;
}