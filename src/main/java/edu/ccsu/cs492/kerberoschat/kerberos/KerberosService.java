package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketGrantingTicket;
import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
import edu.ccsu.cs492.kerberoschat.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class KerberosService {
    private final AppUserService appUserService;

    /**
     * Duration Tickets will last in milliseconds
     */
    private final long ticketDuration;

    @Autowired
    public KerberosService(AppUserService appUserService) {
        this.appUserService = appUserService;
        ticketDuration = 60 * 60 * 1000;
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
     * Creates a new TicketGrantingTicket that will allow an authenticated user to carry out operations with the KDC
     *
     * @param user the authenticated user who will receive the ticket
     * @return a new ticket granting ticket for communication with the KDC
     */
    public TicketGrantingTicket createTGT(AppUser user) {
        return new TicketGrantingTicket(user.getUserName(), ticketDuration, this.generateSessionKey());
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

    /**
     * Determines if a timestamp sent by a user is valid, that is the time specified has passed, but the current time is
     * within five minutes of the timestamp
     *
     * @param timestamp the timestamp that's being checked
     * @return true if the timestamp is valid, false otherwise
     */
    public boolean isTimestampValid(Date timestamp) {
        int fiveMinutes = 300000; // five minutes in milliseconds
        Date systemTimestamp = new Date();
        return systemTimestamp.getTime() > timestamp.getTime() + fiveMinutes || systemTimestamp.getTime() < timestamp.getTime();
    }
}
