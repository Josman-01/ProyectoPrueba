package com.josman.estudioJava.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Document(collection = "datosUsuarios") // Indica que esta clase se mapea a la colección 'users' en MongoDB
public class User {

    @Id // Marca el campo como el ID principal del documento en MongoDB
    private String id; // MongoDB usa ObjectId por defecto, que Spring mapea a String

    private String username;

    private String password; // Aquí se guardará la contraseña ENCRIPTADA

    private String email;

    // Opcional: Para roles de usuario (ej. "ROLE_USER", "ROLE_ADMIN")
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    //Gettes y Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String password, String email, Role role) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}