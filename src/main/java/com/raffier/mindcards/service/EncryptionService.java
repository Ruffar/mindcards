package com.raffier.mindcards.service;

import com.raffier.mindcards.model.table.User;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {

    private final SecretKeyFactory keyFactory;

    private EncryptionService() throws NoSuchAlgorithmException {
        keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    }

    // Encryption //
    public String encryptPassword(String plainPassword, User user) {
        try {

            byte[] userIdBytes = Integer.toBinaryString(user.getUserId()).getBytes();
            //Create a salt of 16 bytes using userId
            byte[] salt = new byte[16];
            for (int i = 0; i < 16; i++) {
                if (userIdBytes.length-1 <= i) { //If the byte from userId exists then add it to the salt
                    salt[i] = userIdBytes[i];
                }
            }

            KeySpec keySpec = new PBEKeySpec(plainPassword.toCharArray(), salt, 2^16, 256);
            SecretKey key = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");

            return Base64.getEncoder().encodeToString(key.getEncoded());

        } catch (InvalidKeySpecException e) {
            throw new RuntimeException();
        }
    }

    public boolean passwordMatches(String untestedPassword, User user) {

        String untestedEncrypted = encryptPassword(untestedPassword, user);
        return untestedEncrypted.matches(user.getPassword());

    }

}