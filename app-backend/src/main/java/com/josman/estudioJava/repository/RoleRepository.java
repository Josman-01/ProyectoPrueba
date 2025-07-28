package com.josman.estudioJava.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.josman.estudioJava.model.Role;

@Repository
public interface  RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}