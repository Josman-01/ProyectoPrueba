package com.josman.estudioJava.service;

import java.util.Optional;
import java.util.regex.Matcher;

import org.slf4j.Logger; // Importa tu clase de utilidades
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josman.estudioJava.model.User;
import com.josman.estudioJava.repository.UserRepository;
import com.josman.estudioJava.util.ValidationUtils; // Para usar Pattern

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(String username, String rawPassword, String email) {
        logger.info("Intentando registrar un nuevo usuario con nombre de usuario: {}", username);

        // --- Validaciones de Entrada ---
        if (ValidationUtils.isNullOrEmpty(username)) {
            logger.error("Error de validación: El nombre de usuario no puede ser nulo o vacío.");
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío.");
        }
        if (username.length() < 5) {
            logger.error("Error de validación: El nombre de usuario debe tener al menos 5 caracteres.");
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 5 caracteres.");
        }

        if (ValidationUtils.isNullOrEmpty(rawPassword)) {
            logger.error("Error de validación: La contraseña no puede ser nula o vacía.");
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }
        Matcher passwordMatcher = ValidationUtils.PASSWORD_PATTERN.matcher(rawPassword);
        if (!passwordMatcher.matches()) {
            logger.error("Error de validación: La contraseña no cumple con los requisitos de seguridad.");
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.");
        }

        if (ValidationUtils.isNullOrEmpty(email)) {
            logger.error("Error de validación: El email no puede ser nulo o vacío.");
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        }
        Matcher emailMatcher = ValidationUtils.EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            logger.error("Error de validación: El formato del email es inválido.");
            throw new IllegalArgumentException("El formato del email es inválido.");
        }

        // --- Fin de Validaciones de Entrada ---

        if (userRepository.findByUsername(username).isPresent()) {
            logger.error("El nombre de usuario ya existe: {}", username);
            throw new RuntimeException("El nombre de usuario ya existe: " + username);
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setEmail(email);

        logger.info("Guardando nuevo usuario en la base de datos: {}", newUser.getUsername());
        return userRepository.save(newUser);
    }

    public boolean validateUserCredentials(String username, String rawPassword) {
        logger.info("Intento de inicio de sesión para el usuario: {}", username);

        // Opcional: Podrías añadir validación de null/empty aquí también para el login,
        // aunque un login con campos vacíos probablemente fallaría igual al no encontrar el usuario.
        if (ValidationUtils.isNullOrEmpty(username) || ValidationUtils.isNullOrEmpty(rawPassword)) {
            logger.warn("Fallo de inicio de sesión: Nombre de usuario o contraseña vacíos.");
            return false;
        }

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            logger.warn("Fallo de inicio de sesión: Usuario '{}' no encontrado.", username);
            return false;
        }

        User user = userOptional.get();
        // Compara la contraseña en texto plano con la contraseña encriptada almacenada
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.info("Inicio de sesión exitoso para el usuario: {}", username);
            return true;
        } else {
            logger.warn("Fallo de inicio de sesión: Contraseña incorrecta para el usuario '{}'.", username);
            return false;
        }
    }

    public boolean changePassword(String email, String newRawPassword) {
        logger.info("Intento de cambio de contraseña para el usuario: {}", email);

        // --- Validaciones de Entrada ---
        if (ValidationUtils.isNullOrEmpty(email)) {
            logger.error("Error de validación: El email no puede ser nulo o vacío para cambiar la contraseña.");
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        }
        Matcher emailMatcher = ValidationUtils.EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            logger.error("Error de validación: El formato del email es inválido para cambiar la contraseña.");
            throw new IllegalArgumentException("El formato del email es inválido.");
        }

        if (ValidationUtils.isNullOrEmpty(newRawPassword)) {
            logger.error("Error de validación: La nueva contraseña no puede ser nula o vacía.");
            throw new IllegalArgumentException("La nueva contraseña no puede ser nula o vacía.");
        }
        Matcher passwordMatcher = ValidationUtils.PASSWORD_PATTERN.matcher(newRawPassword);
        if (!passwordMatcher.matches()) {
            logger.error("Error de validación: La nueva contraseña no cumple con los requisitos de seguridad.");
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres, una mayúscula y un número.");
        }
        // --- Fin de Validaciones de Entrada ---

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()){
            logger.warn("Fallo de cambio de contraseña: Usuario con email '{}' no encontrado.", email);
            return false;
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newRawPassword));

        try {
            userRepository.save(user);
            logger.info("Contraseña cambiada exitosamente para el usuario con email: {}", email);
            return true;
        } catch (Exception e) {
            logger.warn("Fallo de cambio de contraseña: No se pudo actualizar la contraseña para el usuario '{}': '{}'", email, e.getMessage());
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updatePassword(String email, String newRawPassword) {
        // Este método ahora contiene lógica duplicada con changePassword.
        // Es mejor llamar a changePassword si solo necesitas cambiar la contraseña por email.
        // O este podría ser un método interno auxiliar sin validaciones para casos específicos.
        // Por ahora, lo dejaré con las validaciones si lo usas directamente.

        // --- Validaciones de Entrada (duplicadas si usas changePassword) ---
        if (ValidationUtils.isNullOrEmpty(email)) {
            logger.error("Error de validación: El email no puede ser nulo o vacío en updatePassword.");
            throw new IllegalArgumentException("El email no puede ser nulo o vacío.");
        }
        Matcher emailMatcher = ValidationUtils.EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            logger.error("Error de validación: El formato del email es inválido en updatePassword.");
            throw new IllegalArgumentException("El formato del email es inválido.");
        }

        if (ValidationUtils.isNullOrEmpty(newRawPassword)) {
            logger.error("Error de validación: La nueva contraseña no puede ser nula o vacía en updatePassword.");
            throw new IllegalArgumentException("La nueva contraseña no puede ser nula o vacía.");
        }
        Matcher passwordMatcher = ValidationUtils.PASSWORD_PATTERN.matcher(newRawPassword);
        if (!passwordMatcher.matches()) {
            logger.error("Error de validación: La nueva contraseña no cumple con los requisitos de seguridad en updatePassword.");
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 8 caracteres, una mayúscula y un número.");
        }
        // --- Fin de Validaciones de Entrada ---

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no encontrado: " + email));
        user.setPassword(passwordEncoder.encode(newRawPassword));
        return userRepository.save(user);
    }
}
