package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketGrantingTicket {
    private String username;

    private Date timeIssued;

    private Date expiryTime;

    private long issuedTime;

    private SecretKey sessionKey;
}
