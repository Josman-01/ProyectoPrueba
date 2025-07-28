package com.josman.estudioJava.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josman.estudioJava.dto.AuthRequest;
import com.josman.estudioJava.dto.AuthResponse;
import com.josman.estudioJava.model.User;
import com.josman.estudioJava.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/auth")
@Tag(name="Autenticaciones y Registro", description="Registros de nuevos usuarios y guardados en la base de datos, login de usuarios y autenticacion al inicio de sesion, con la informacion de laa base de datos")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary= "Registro de usuarios",
        description="Registro de usuario, correo y contreseña de un usuario, donde se le asigna tambien su rol de usuario, asi mismo la contraseña ingresada es encriptada para su seguridad.",
        requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(description="Usuario registrado exitosamente + username", required=true,
                    content=@Content(schema= @Schema(implementation=User.class))),
        responses = {
            @ApiResponse(responseCode="201", description="Usuario registrado exitosamente",
            content=@Content(schema=@Schema(implementation=String.class))),
            @ApiResponse(responseCode="400", description="Error al registrar usuario",
            content=@Content(schema=@Schema(implementation=String.class))),
            @ApiResponse(responseCode="500", description="Error en el servidor")
        })
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = authService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente: " + registeredUser.getUsername());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary= "Login de usuarios",
        description="Login de usuario",
        requestBody=@io.swagger.v3.oas.annotations.parameters.RequestBody(description="Inicio de sesion de usuario, donde se valida la informacion y se genera un token con la informacion necesaria para mantener la sesion", required=true,
                    content=@Content(schema= @Schema(implementation=AuthRequest.class))),
                    responses = {
                        @ApiResponse(responseCode="200", description="Inicio de sesion de usuario",
                        content=@Content(schema=@Schema(implementation=AuthResponse.class))),
                        @ApiResponse(responseCode="401", description="Credenciales inválidas",
                        content=@Content(schema=@Schema(implementation=String.class)))
                    })
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response); // Devuelve el JWT y el username
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: " + e.getMessage());
        }
    }
    
    @GetMapping("/mis-datos")
    @Operation(summary="Validacion del token generado en el endpoint /login",
        description="Validacion del token generado en el endpoint /login",
        responses = {
            @ApiResponse(responseCode="200", description="Autenticado",
            content=@Content(schema=@Schema(implementation=String.class))),
            @ApiResponse(responseCode="401")
        })
    public ResponseEntity<String> checkAuth() { // Usamos Principal directamente
      return ResponseEntity.ok("Autenticado");
    }
}