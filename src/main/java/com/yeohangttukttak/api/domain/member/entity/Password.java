package com.yeohangttukttak.api.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Getter
@Embeddable
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_BYTE_SIZE = 16;
    private static final int ITERATIONS = 10;
    private static final String PASSWORD_FORMAT = "$%d$%s%s"; // 새로운 포맷 문자열

    private String password;

    public static Password create(String plainText) {
        String salt = generateSalt();
        String password = encrypt(plainText, salt, ITERATIONS);

        return new Password(password);
    }

    public boolean validate(String plainText) {
        String[] parts = password.split("\\$");
        int iterations = Integer.parseInt(parts[1]);
        String salt = parts[2].substring(0, 24);

        String hashed = encrypt(plainText, salt, iterations);
        return this.password.equals(hashed);
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String encrypt(String plainText, String salt, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] bytes = (plainText + salt).getBytes();
            int rounds = (int) Math.pow(2, iterations);

            for (int iter = 0; iter < rounds; iter++) {
                bytes = md.digest(bytes);
            }

            String hash = Base64.getEncoder().encodeToString(bytes);
            return String.format(PASSWORD_FORMAT, iterations, salt, hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
}