
package com.fashionstore.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash a plain-text password before storing it.
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(
                plainPassword,
                BCrypt.gensalt(12)
        );
    }

    // Verify a password entered by the customer.
    public static boolean verifyPassword(
            String plainPassword,
            String hashedPassword) {

        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(
                    plainPassword,
                    hashedPassword
            );
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
