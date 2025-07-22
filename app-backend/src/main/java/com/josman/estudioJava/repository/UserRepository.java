package com.josman.estudioJava.repository;

import java.util.Optional; // Importa tu entidad de MongoDB

import org.springframework.data.mongodb.repository.MongoRepository;

import com.josman.estudioJava.model.User;

public interface UserRepository extends MongoRepository<User, String> { // El segundo tipo es el tipo del ID
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}