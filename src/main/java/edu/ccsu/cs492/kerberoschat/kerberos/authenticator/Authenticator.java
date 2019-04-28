package edu.ccsu.cs492.kerberoschat.kerberos.authenticator;

import lombok.Data;

import java.util.Date;

/**
 * Used for a representation of a Request sent by a user for authentication
 */
@Data
public class Authenticator {
    /**
     * The Name of the user that wishes to be authenticated
     */
    private String userName;

    /**
     * A timestamp encrypted with the hash of the user's password
     */
    private Date timestamp;
}
