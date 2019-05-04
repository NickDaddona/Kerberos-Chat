package edu.ccsu.cs492.kerberoschat.kerberos.service;

import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class CryptoService {

    /**
     * Generates a new AES-256 Key for a session that is being established
     *
     * @return a new SecretKey object that contains the generated key
     */
    public SecretKey generateSessionKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256); // TODO: may need to provide secure random different than one the class creates
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) { // TODO: Determine strategy for handling exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encodes the passed secretkey as a Base64 String
     * @param key the passed encryption key
     * @return a Base64 string representation of the encryption key
     */
    public String decodeSecretKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
