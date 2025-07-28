package com.josman.estudioJava.service;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria; // Importar Criteria
import org.springframework.data.mongodb.core.query.Query; // Importar Query
import org.springframework.stereotype.Service;

import com.josman.estudioJava.model.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Service
public class MongoDBService {
    
    private static final Logger logger = LoggerFactory.getLogger(MongoDBService.class);

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017}")
    private String mongoUri;
    @Value("${spring.data.mongodb.database:credenciales}")
    private String databaseName;

    @Value("${mongodb.collection.datos:user_login}")
    private String collectionName;

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    private final MongoTemplate mongoTemplate;

    public MongoDBService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            mongoClient = MongoClients.create(mongoUri);
            database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);
            logger.info("Conexión exitosa a MongoDB. Base de datos: {}, Colección: {}", databaseName, collectionName);
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

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return El objeto User si se encuentra, o null si no existe.
     */
    public User findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.findOne(query, User.class, collectionName);
    }

    /**
     * Verifica si un usuario con el nombre de usuario dado ya existe en la base de datos.
     * @param username El nombre de usuario a verificar.
     * @return true si el usuario existe, false en caso contrario.
     */
    public boolean userExists(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return mongoTemplate.exists(query, User.class, collectionName);
    }

    /**
     * Verifica si un usuario con el correo electrónico dado ya existe en la base de datos.
     * @param email El correo electrónico a verificar.
     * @return true si el correo electrónico ya está registrado, false en caso contrario.
     */
    public boolean emailExists(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.exists(query, User.class, collectionName);
    }
}