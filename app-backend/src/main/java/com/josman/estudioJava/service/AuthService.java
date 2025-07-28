package com.josman.estudioJava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager; // Tu clase User
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.josman.estudioJava.dto.AuthRequest;
import com.josman.estudioJava.dto.AuthResponse;
import com.josman.estudioJava.model.User;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService (UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, CustomUserDetailsService customUserDetailsService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
    }

    // Método de registro
    public User register(User user) {
        return userService.registerNewUser(user);
    }

    // Método de login
    public AuthResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String jwtToken = jwtService.generateToken(authentication);
        logger.info("Token JWT generado: {}", jwtToken);
        logger.info("Autenticacion: {}", authentication);

        return new AuthResponse(jwtToken, request.getUsername());
    }

    //Muestra de datos
}