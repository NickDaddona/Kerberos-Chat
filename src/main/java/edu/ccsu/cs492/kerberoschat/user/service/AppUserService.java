package edu.ccsu.cs492.kerberoschat.user.service;

import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
import edu.ccsu.cs492.kerberoschat.user.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Provides services related to manipulating users in the database
 */
@Service
public class AppUserService {

    /**
     * Crud Repository for accessing users in the database
     */
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Attempts to find a user with the supplied username in the database
     *
     * @param userName the username that's used for searching the database
     * @return an AppUser object containing the user's information
     * @throws AppUserNotFoundException if no user is found with the supplied username
     */
    public AppUser getUser(String userName) throws AppUserNotFoundException {
        Optional<AppUser> appUserOptional = appUserRepository.findById(userName);
        return appUserOptional.orElseThrow(() -> new AppUserNotFoundException("User with username " + userName + " not found"));
    }
}
