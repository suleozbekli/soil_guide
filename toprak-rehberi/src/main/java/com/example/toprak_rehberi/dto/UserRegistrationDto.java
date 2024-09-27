package com.example.toprak_rehberi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;

    public UserRegistrationDto(String username, String password,String confirmPassword,String email) {
        this.username = username;
        this.password = password;
        this.confirmPassword=confirmPassword;
        this.email = email;
    }

    public UserRegistrationDto() {
    }
}