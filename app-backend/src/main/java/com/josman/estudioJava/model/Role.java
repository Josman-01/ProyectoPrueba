package com.josman.estudioJava.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Document(collection = "roles")
public class Role {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private String id;

    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
