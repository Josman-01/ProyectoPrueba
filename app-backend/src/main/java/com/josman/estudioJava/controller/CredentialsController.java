package com.josman.estudioJava.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josman.estudioJava.dto.ChangePasswordRequest;
import com.josman.estudioJava.dto.ValidEmailRequest;
import com.josman.estudioJava.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/credentials")
@Tag(name="Credenciales", description="Validacion de credenciales, en este caso el Email, para cambio de contraseña")
public class CredentialsController {
    
    Logger logger = LoggerFactory.getLogger(CredentialsController.class);
    
    private final UserService userService;

    public CredentialsController(UserService userService) {
        this.userService = userService;
    }

    //Validacion del correo electronico para antes del cambio de la contraseñ, se usa desde el frontd
    @PostMapping("/validEmail")
    @Operation(summary="Validacion del email",
        description="Validacion del email para el cambio de contraseña",
        requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(description="Validacion del email", required=true,
                    content=@Content(schema= @Schema(implementation=ValidEmailRequest.class))),
                    responses = {
                        @ApiResponse(responseCode="200", description="Validacion de email exitosa",
                        content=@Content(schema=@Schema(implementation=String.class))),
                        @ApiResponse(responseCode="400", description="Email no encontrado",
                        content=@Content(schema=@Schema(implementation=String.class)))
                    })
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
    
    //Cambio de contraseña
    @PostMapping("/change-password")
    @Operation(summary="Cambio de contraseña",
        description="Cambio de contraseña con el correo electronico",
        requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(description="Cambio de contraseña por medio del correo del usuario", required=true,
                    content=@Content(schema= @Schema(implementation=ChangePasswordRequest.class))),
                    responses = {
                        @ApiResponse(responseCode="200", description="Contraseña actualizada exitosamente",
                        content=@Content(schema=@Schema(implementation=String.class))),
                        @ApiResponse(responseCode="400", description="Error al actualizar la contraseña")
                    })
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request.getEmail(), request.getNewPassword());
            logger.info("Contraseña actualizada para el email: {}", request.getEmail());
            return ResponseEntity.ok("Contraseña actualizada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
