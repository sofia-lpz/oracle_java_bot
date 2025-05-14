package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.dto.LoginUserDto;
import com.springboot.MyTodoList.model.dto.RegisterUserDto;
import com.springboot.MyTodoList.model.User;
import com.springboot.MyTodoList.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {

        if (userRepository.findByPhoneNumber(input.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("User with this phone number already exists");
        }
        User user = new User()
                .setName(input.getName())
                .setPhoneNumber(input.getPhoneNumber())
                .setRole(input.getRole())
                .setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getPhoneNumber(),
                        input.getPassword()
                )
        );

        return userRepository.findByPhoneNumber(input.getPhoneNumber())
                .orElseThrow();
    }

public void logout(HttpServletRequest request) {
    SecurityContextHolder.clearContext();
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
}
}