package com.example.toprak_rehberi.controllers;

import com.example.toprak_rehberi.dto.UserRegistrationDto;
import com.example.toprak_rehberi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Toprak Rehberi API!";
    }


}
