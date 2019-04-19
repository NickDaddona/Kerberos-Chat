package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
import edu.ccsu.cs492.kerberoschat.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return user.getPassword().substring(0, 15);
    }
}
