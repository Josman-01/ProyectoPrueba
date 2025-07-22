package com.josman.estudioJava.service;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.josman.estudioJava.model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class mongodb {
    
    private static final Logger logger = LoggerFactory.getLogger(mongodb.class);

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;
    @Value("${spring.data.mongodb.database:credenciales}")
    private String dbName;
    @Value("${mongodb.collection.usuarios:user_login}")
    private String collectionName;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private final MongoTemplate mongoTemplate;

    public mongodb(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(dbName);
            collection = database.getCollection(collectionName);
            logger.info("Conexión exitosa a MongoDB. Base de datos: {}, Colección: {}", dbName, collectionName);
        } catch (Exception e) {
            logger.error("Error al conectar a MongoDB: " + e.getMessage());
            throw new IllegalStateException("Fallo al conectar con MongoDB", e);
        }
    }

    @PreDestroy
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("Conexión a MongoDB cerrada.");
        }
    }

    public void saveOrUpdateUser(User user) {
        mongoTemplate.save(user, collectionName);
        logger.info("Usuario '{}' guardado/actualizado en MongoDB.", user.getUsername());
    }

}
