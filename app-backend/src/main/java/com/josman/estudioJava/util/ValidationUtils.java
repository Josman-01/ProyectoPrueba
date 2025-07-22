package com.josman.estudioJava.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Contraseña: al menos 8 caracteres, al menos una mayúscula, al menos un número.
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");

    // Email: contiene un '@' y un '.' después del '@', no necesariamente exhaustivo pero es un buen inicio.
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    /**
     * Valida si una cadena es nula o está vacía (después de trim).
     * @param str La cadena a validar.
     * @return true si es nula o vacía, false en caso contrario.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}