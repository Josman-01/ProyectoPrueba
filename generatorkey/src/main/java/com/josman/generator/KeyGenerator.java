package com.josman.generator;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded(); // 32 bytes = 256 bits
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Tu clave secreta JWT (Base64): " + base64Key);
    }
}