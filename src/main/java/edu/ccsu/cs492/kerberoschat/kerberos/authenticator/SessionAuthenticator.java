package edu.ccsu.cs492.kerberoschat.kerberos.authenticator;

import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketGrantingTicket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionAuthenticator {
    private SecretKey sessionKey;

    private TicketGrantingTicket ticketGrantingTicket;
}
