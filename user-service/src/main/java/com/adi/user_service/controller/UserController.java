package com.adi.user_service.controller;

import com.adi.user_service.model.LoginRequest;
import com.adi.user_service.model.LoginResponse;
import com.adi.user_service.model.RegisterRequest;
import com.adi.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        service.register(request);
        String username = request.getUsername();
        return new ResponseEntity<>("Username " + username + " has been registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(service.login(request), HttpStatus.OK);
    }

    @GetMapping("/email/{userId}")
    public ResponseEntity<String> getUserEmail(@PathVariable int userId) {
        return new ResponseEntity<>(service.getUserEmail(userId), HttpStatus.OK);
    }
}
