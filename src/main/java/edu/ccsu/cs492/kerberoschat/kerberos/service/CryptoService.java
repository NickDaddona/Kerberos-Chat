package edu.ccsu.cs492.kerberoschat.kerberos.service;

import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
     * Gets the encryption key the user shares with the KDC
     *
     * @param user the user of the encryption key
     * @return the encryption key for this user
     */
    public SecretKey getUserKey(AppUser user) {
        String key = user.getPassword().substring(16);
        return encodeSecretKey(key);
    }

    /**
     * Encodes the passed secretkey as a Hex String
     *
     * @param key the passed encryption key
     * @return a Hex string representation of the encryption key
     */
    public String decodeSecretKey(SecretKey key) {
        return new String(Hex.encode(key.getEncoded()));
    }

    /**
     * Decodes the passed String key and creates a secret key object with it
     *
     * @param key the string key we're wrapping in a SecretKey Object
     * @return a new SecretKey containing the passed key
     */
    public SecretKey encodeSecretKey(String key) {
        byte[] decoded = Hex.decode(key);
        return new SecretKeySpec(decoded, "AES");
    }

    /**
     * Decrypts a ciphertext using AES and a 256-bit key
     *
     * @param cipherTextAndIV the cipherText and iv as a string in the format of iv + cipherText
     * @param key             the secret key that will be used for decryption
     * @return the plaintext as a string after decryption
     */
    public String decryptAESCBC(String cipherTextAndIV, SecretKey key) {
        String iv = this.getIV(cipherTextAndIV);
        String cipherText = this.getCipherText(cipherTextAndIV);
        return this.decryptAESCBC(cipherText, iv, key);
    }

    // Private Method for actually carrying out the decryption
    private String decryptAESCBC(String cipherText, String iv, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Hex.decode(iv));
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return new String(cipher.doFinal(Hex.decode(cipherText)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Method of encryption used is not supported by JVM (AES/CBC/NoPadding)");
            e.printStackTrace();
            return null;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) { // TODO: Determine how to handle exceptions
            e.printStackTrace(); // print the error if the encryption failed
            return null;
        }
    }

    /**
     * Slices the 128-bit iv off the front of the cipherText
     *
     * @param cipherTextAndIV the iv and ciphertext concatenated
     * @return the iv used to encrypt this cipherText
     */
    public String getIV(String cipherTextAndIV) {
        return cipherTextAndIV.substring(0, 32);
    }

    /**
     * Slices the 128-bit iv off the front of the cipherText and just removes the ciphertext
     *
     * @param cipherTextAndIV cipherTextAndIV the iv and ciphertext concatenated
     * @return the ciphertext, minus the IV
     */
    public String getCipherText(String cipherTextAndIV) {
        return cipherTextAndIV.substring(32);
    }
}
