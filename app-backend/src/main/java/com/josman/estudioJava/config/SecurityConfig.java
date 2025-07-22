package com.josman.estudioJava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilita CSRF (Cross-Site Request Forgery) para API REST.
            // Ten cuidado en producción, asegúrate de entender las implicaciones de seguridad.
            .csrf(csrf -> csrf.disable())
            // Configura las reglas de autorización para las peticiones HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite que el endpoint /api/auth/register sea accesible sin autenticación
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/credentials/change-password").permitAll()
                .requestMatchers("/api/credentials/validEmail").permitAll()
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            );
            // Si quieres habilitar un formulario de login o Basic Auth para pruebas:
            // .httpBasic(Customizer.withDefaults()); // Habilita autenticación Basic
            // .formLogin(Customizer.withDefaults()); // Habilita formulario de login

        return http.build();
    }
}