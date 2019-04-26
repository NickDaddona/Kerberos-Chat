package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
import edu.ccsu.cs492.kerberoschat.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@Service
public class KerberosService {
    private final AppUserService appUserService;

    @Autowired
    public KerberosService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * Gets the salt of the user's password for a user to authenticate themselves
     *
     * @param userName the username associated with the desired user's salt
     * @return the salt used in computing the user's password
     */
    public String getUserSalt(String userName) throws AppUserNotFoundException {
        AppUser user = appUserService.getUser(userName);
        return user.getPassword().substring(0, 16); // first 16 hex characters of hash are the salt
    }

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
}
