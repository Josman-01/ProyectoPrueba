package com.josman.estudioJava.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josman.estudioJava.dto.LoginRequest;
import com.josman.estudioJava.dto.RegisterRequest;
import com.josman.estudioJava.service.UserService;



@RestController
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen
@RequestMapping("/api/auth") // Un prefijo común para endpoints de autenticación
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        try {
            userService.registerNewUser(request.getUsername(), request.getPassword(), request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        logger.debug("Petición de login recibida para el usuario: {}", request.getUsername());
        if (userService.validateUserCredentials(request.getUsername(), request.getPassword())) {
            logger.info("Credenciales válidas para el usuario: {}", request.getUsername());
            // En un sistema real, aquí generarías y devolverías un token JWT
            return ResponseEntity.ok("Inicio de sesión exitoso."); // Código 200 OK
        } else {
            logger.warn("Credenciales inválidas para el usuario: {}", request.getUsername());
            // Es una buena práctica retornar 401 Unauthorized para credenciales inválidas
            // en lugar de 400 Bad Request, para evitar dar pistas sobre si el usuario existe o no.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña inválidos."); // Código 401 Unauthorized
        }
    }
}