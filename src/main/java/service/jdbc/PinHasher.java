/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.jdbc;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 *
 * @author XPC
 */
public final class PinHasher {
    private PinHasher() {}

    public static byte[] salt() {
        byte[] s = new byte[16];
        new SecureRandom().nextBytes(s);
        return s;
    }

    public static byte[] hash(char[] pin, byte[] salt) {
        try {
            var spec = new PBEKeySpec(pin, salt, 120_000, 256);
            var skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error generando hash de PIN", e);
        }
    }

    public static boolean verify(char[] pin, byte[] salt, byte[] expected) {
        var calc = hash(pin, salt);
        return MessageDigest.isEqual(calc, expected);
    }
}

