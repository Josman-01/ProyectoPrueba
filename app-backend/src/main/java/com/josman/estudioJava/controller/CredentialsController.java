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

import com.josman.estudioJava.dto.ChangePasswordRequest;
import com.josman.estudioJava.dto.ValidEmailRequest;
import com.josman.estudioJava.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/credentials")
public class CredentialsController {
    
    Logger logger = LoggerFactory.getLogger(CredentialsController.class);
    
    private final UserService userService;

    public CredentialsController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/validEmail")
    public ResponseEntity<String> validateEmail(@RequestBody ValidEmailRequest request) {
        logger.debug("Petición de validación de email recibida para el email: {}", request.getEmail());
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            logger.info("Validación de correcta para el email: {}.", request.getEmail());
            return ResponseEntity.ok("Validación de email, exitosa.");
        } else {
            logger.warn("Email no encontrado: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email no encontrado.");
        }
    }
    

    // Para cambiar contraseña, podrías tener otro endpoint:
    
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.updatePassword(request.getEmail(), request.getNewPassword());
            logger.info("Contraseña actualizada para el email: {}", request.getEmail());
            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
