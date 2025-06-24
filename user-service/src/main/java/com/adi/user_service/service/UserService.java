package com.adi.user_service.service;

import com.adi.user_service.config.JWTFilter;
import com.adi.user_service.model.LoginRequest;
import com.adi.user_service.model.LoginResponse;
import com.adi.user_service.model.RegisterRequest;
import com.adi.user_service.model.User;
import com.adi.user_service.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private UserRepo repo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public void register(RegisterRequest request) {
        if(repo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsAdmin(false);

        repo.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = repo.findByUsername(request.getUsername());

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())
        ) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtFilter.generateToken(user);
        return new LoginResponse(token, user.getUsername(), user.getIsAdmin());
    }

    public String getUserEmail(int userId) {
        User user = repo.findById(userId).get();
        return user.getUsername();
    }
}
