package com.tubes.pbw.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
public class User {

    @NotBlank(message = "Email tidak boleh kosong")
    @Size(min = 4, message = "Email harus diisi")
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 4, message = "Password harus memiliki panjang minimal 4 karakter")
    private String password;

    @NotBlank(message = "Confirm password tidak boleh kosong")
    @Size(min = 4, message = "Confirm password harus memiliki panjang minimal 4 karakter")
    private String confirmpassword;
    
    // @NotBlank(message = "Role tidak boleh kosong")
    private String role;

}
