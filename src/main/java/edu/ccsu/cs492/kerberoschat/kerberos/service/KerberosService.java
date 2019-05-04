package edu.ccsu.cs492.kerberoschat.kerberos.service;

import edu.ccsu.cs492.kerberoschat.kerberos.ticket.MalformedTGTException;
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

    /**
     * The window a timestamp will be accepted in milliseconds
     */
    private final long timestampWindow;

    @Autowired
    public KerberosService(AppUserService appUserService) {
        this.appUserService = appUserService;
        this.ticketDuration = 60 * 60 * 1000;
        this.timestampWindow = 5 * 60 * 1000;
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
     * @param key  the session key as a base64 string
     * @return a new ticket granting ticket for communication with the KDC
     */
    public TicketGrantingTicket createTGT(AppUser user, String key) throws MalformedTGTException {
        Date issueTime = new Date(); // get the current time
        Date expiryTime = new Date(issueTime.getTime() + ticketDuration);
        return new TicketGrantingTicket.TGTBuilder()
                .addUserName(user.getUserName())
                .addTimeIssued(issueTime)
                .addExpiryTime(expiryTime)
                .addDuration(ticketDuration)
                .addSessionKey(key)
                .getBuiltTicket();
    }

    /**
     * Determines if a TicketGrantingTicket is still valid
     *
     * @param TGT the TicketGrantingTicket that's being tested
     * @return true if the ticket is valid, false otherwise
     */
    public boolean isTGTValid(TicketGrantingTicket TGT) {
        return TGT.getTimeIssued().getTime() + TGT.getDuration() > new Date().getTime();
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
        Date currentTime = new Date();
        return currentTime.getTime() < timestamp.getTime() + timestampWindow && timestamp.getTime() < currentTime.getTime();
    }
}
