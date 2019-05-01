package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

/**
 * Exception class used in the case of an improperly build TicketGrantingTicket
 */
public class MalformedTGTException extends Exception {

    public MalformedTGTException(String message) {
        super(message);
    }
}
