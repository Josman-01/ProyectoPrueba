package com.josman.estudioJava.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // Opcional, si el nombre del campo en Mongo es diferente

@Document(collection = "users") // Indica que esta clase se mapea a la colección 'users' en MongoDB
public class User {

    @Id // Marca el campo como el ID principal del documento en MongoDB (generalmente un String)
    private String id; // MongoDB usa ObjectId por defecto, que Spring mapea a String

    @Field("username") // Opcional: si el campo en la BD se llama diferente a la propiedad
    private String username;

    private String password; // Aquí se guardará la contraseña ENCRIPTADA

    private String email;
    // Puedes añadir otros campos
    // private String email;
    // private List<String> roles;

    // Getters y Setters
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

    // Constructor vacío
    public User() {
        this.id = UUID.randomUUID().toString(); // Genera un ID único
    }

    // Constructor con campos
    public User(String username, String password, String email) {
        this(); // Llama al constructor vacío para inicializar el ID
        this.username = username;
        this.password = password;
        this.email = email;
    }
}