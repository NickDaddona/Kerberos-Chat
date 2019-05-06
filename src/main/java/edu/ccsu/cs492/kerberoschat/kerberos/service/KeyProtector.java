package edu.ccsu.cs492.kerberoschat.kerberos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Component
@PropertySource("classpath:application-secret.properties")
class KeyProtector {
    /**
     * The secret key for the KDC
     */
    private SecretKey kdcSecretKey;

    KeyProtector(@Value("${kdc.aes.key}") String kdcKey) {
        kdcSecretKey = new SecretKeySpec(Hex.decode(kdcKey), "AES");
    }

    public SecretKey getKdcSecretKey() {
        return kdcSecretKey;
    }
}
