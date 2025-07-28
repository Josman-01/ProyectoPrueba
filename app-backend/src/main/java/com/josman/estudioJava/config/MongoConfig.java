package com.josman.estudioJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory; // Importa esta clase
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory mongoFactory, // Inyecta MongoDatabaseFactory
            MongoMappingContext context) {

        // Crea el TypeMapper pasando 'null' para el campo _class
        DefaultMongoTypeMapper typeMapper = new DefaultMongoTypeMapper(null);

        // Usa el constructor correcto que acepta MongoDatabaseFactory y MongoMappingContext
        MappingMongoConverter converter = new MappingMongoConverter(mongoFactory, context);
        converter.setTypeMapper(typeMapper); // Establece el TypeMapper personalizado

        return converter;
    }
}