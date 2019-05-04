package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A response ticket containing everything a user needs to chat with another user securely
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatTicket {

    /**
     * The user the requester has opened a connection with
     */
    String username;

    /**
     * The session key that will be used for all communication
     */
    String sessionKey;

    /**
     * The ticket that will be sent to the receiving user
     */
    TicketToUser ticketToUser;
}
