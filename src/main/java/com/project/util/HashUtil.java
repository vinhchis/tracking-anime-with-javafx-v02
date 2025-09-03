package com.project.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil {
    private static final int K = 10;

    public static String encode(String plainPassword) {
        if (plainPassword == null) return null;
        String salt = BCrypt.gensalt(K);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static boolean decode(String plainPassword, String hashed) {
        if (plainPassword == null || hashed == null) return false;
        try {
            return BCrypt.checkpw(plainPassword, hashed);
        } catch (IllegalArgumentException ex) {
            // invalid hash format
            return false;
        }
    }

}
