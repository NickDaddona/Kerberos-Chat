package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
public class TicketGrantingTicket {
    /**
     * The name of the user the ticket was granted to
     */
    private String username;

    /**
     * The time the ticket was issued
     */
    private Date timeIssued;

    /**
     * The time the ticket is set to expire
     */
    private Date expiryTime;

    /**
     * How long the ticket is set to last
     */
    private long duration;

    /**
     * The session key for this ticket
     * // TODO: Should consider changing this to a string to allow for easier encryption
     */
    private SecretKey sessionKey;

    /**
     * Creates a new ticket granting ticket for the specified user with the specified duration
     *
     * @param username   the name of the user being granted the ticket
     * @param duration   the duration in milliseconds the ticket will last
     * @param sessionKey the key the user and the KDC will use for the session
     */
    public TicketGrantingTicket(String username, long duration, SecretKey sessionKey) {
        this.username = username;
        this.duration = duration;
        this.timeIssued = new Date();
        this.expiryTime = new Date(timeIssued.getTime() + duration);
        this.sessionKey = sessionKey;
    }

    /**
     * Determines if the ticket is still valid
     *
     * @return true if its valid, false otherwise
     */
    public boolean isValid() {
        return timeIssued.getTime() + duration < expiryTime.getTime();
    }
}
