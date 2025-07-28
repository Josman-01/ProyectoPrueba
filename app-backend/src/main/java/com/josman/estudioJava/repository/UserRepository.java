package com.josman.estudioJava.repository;

import java.util.Optional; // Importa tu entidad de MongoDB

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.josman.estudioJava.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}