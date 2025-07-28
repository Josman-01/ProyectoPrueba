package com.josman.estudioJava.dto;

public class AuthResponse {

    private String jwtToken;
    private String username;

    public AuthResponse(String jwtToken, String username) {
        this.jwtToken = jwtToken;
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Puedes añadir más información si lo necesitas, como roles, etc.
}