package com.josman.estudioJava.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Importa tu filtro JWT
import org.springframework.security.authentication.AuthenticationManager; // Importa tu UserDetailsService personalizado
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain; // Habilita la seguridad web
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Para configurar la política de sesión
import org.springframework.web.cors.CorsConfiguration; // Interfaz de Spring Security
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.josman.estudioJava.security.JwtAuthenticationFilter;
import com.josman.estudioJava.security.JwtEntryPoint;
import com.josman.estudioJava.service.CustomUserDetailsService;
import com.josman.estudioJava.service.MongoDBService;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web de Sprin
public class SecurityConfig {

    // Inyectamos el filtro JWT y el servicio de MongoDB (para el CustomUserDetailsService)
    private JwtAuthenticationFilter jwtAuthFilter;
    // Inyectamos el servicio de MongoDB
    private final MongoDBService mongoDBService; // Necesario para instanciar CustomUserDetailsService

    public JwtAuthenticationFilter JwtAuthFilter() {
        return jwtAuthFilter;
    }

    public SecurityConfig(MongoDBService mongoDBService, JwtAuthenticationFilter jwtAuthFilter) {
        this.mongoDBService = mongoDBService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Define el codificador de contraseñas (BCrypt es recomendado)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define el UserDetailsService personalizado que Spring Security usará para cargar usuarios
    @Bean
    public UserDetailsService userDetailsService() {
        // Aquí instanciamos tu CustomUserDetailsService, pasándole el MongoDBService
        return new CustomUserDetailsService(mongoDBService);
    }

    // Define el AuthenticationManager, que es el punto central para la autenticación
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Configuración de la cadena de filtros de seguridad
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.requestMatchers(
                "/api/auth/register",
                "/api/auth/login",
                "/api/credentials/validEmail",
                "/api/credentials/change-password",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/api-docs/swagger-config",
                "/api-docs",
                "/webjars/**"
            ).permitAll()
            .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(new JwtEntryPoint()))
            .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}