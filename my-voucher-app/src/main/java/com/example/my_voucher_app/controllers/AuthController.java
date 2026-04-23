package com.example.my_voucher_app.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.my_voucher_app.dto.SignupRequest;
import com.example.my_voucher_app.model.Role;
import com.example.my_voucher_app.model.User;
import com.example.my_voucher_app.repo.RoleRepository;
import com.example.my_voucher_app.repo.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // Check if user already exists
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirst_name(signupRequest.getFirst_name());
        user.setLast_name(signupRequest.getLast_name());
        
        // Hash password
        user.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        
        // Assign role
        Role role = roleRepository.findByName(signupRequest.getRole())
            .orElse(roleRepository.findByName("USER").orElseThrow(() -> 
                new RuntimeException("Default USER role not found")));
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        
        // Save user
        userRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
}
