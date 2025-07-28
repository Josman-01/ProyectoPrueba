package com.josman.estudioJava.config;

import com.josman.estudioJava.model.Role;
import com.josman.estudioJava.repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class RoleInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RoleInitializer.class);

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        // Inicializar ROLE_USER
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
            logger.info("ROLE_USER ha sido inicializado en la base de datos.");
        } else {
            logger.info("ROLE_USER ya existe en la base de datos.");
        }

        // Inicializar ROLE_ADMIN
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            logger.info("ROLE_ADMIN ha sido inicializado en la base de datos.");
        } else {
            logger.info("ROLE_ADMIN ya existe en la base de datos.");
        }
    }
}