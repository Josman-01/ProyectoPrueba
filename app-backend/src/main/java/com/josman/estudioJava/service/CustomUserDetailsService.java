package com.josman.estudioJava.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.josman.estudioJava.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MongoDBService mongoDBService;

    public CustomUserDetailsService(MongoDBService mongoDBService) {
        this.mongoDBService = mongoDBService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mongoDBService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con username: " + username);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().toString());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(authority)// Aquí van los roles
        );
    }
}
