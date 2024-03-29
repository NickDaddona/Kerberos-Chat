package edu.ccsu.cs492.kerberoschat.kerberos.authenticator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A response object for when a user is authenticated, encrypted with the hash of the authenticated user's password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionAuthenticator {

    /**
     * The session key the user will use to communicate with the KDC encoded as a base64 string
     */
    private String sessionKey;

    /**
     * The encrypted TGT
     */
    private String ticketGrantingTicket;
}
