package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Ticket that allows a user to decrypt incoming messages
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketToUser {
    /**
     * The username of the sender of the ticket
     */
    private String username;

    /**
     * The session key for the incoming messages
     */
    private String sessionKey;
}
