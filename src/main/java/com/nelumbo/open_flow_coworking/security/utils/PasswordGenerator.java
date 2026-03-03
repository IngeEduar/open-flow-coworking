package com.nelumbo.open_flow_coworking.security.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {
    private final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String hash(String password, String salt) {
        String finalValue = password + ":" + salt;

        return argon2.hash(
                2,
                65536,
                1,
                finalValue
        );
    }

    public boolean verify(String password, String salt, String hash) {
        String finalValue = password + ":" + salt;
        return argon2.verify(hash, finalValue);
    }

    public String generateSalt() {
        int length = 50;

        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHANUMERIC.length());
            builder.append(ALPHANUMERIC.charAt(index));
        }

        return builder.toString();
    }
}
