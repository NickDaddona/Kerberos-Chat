package edu.ccsu.cs492.kerberoschat.kerberos.authenticator;

import lombok.Data;

import java.util.Date;

@Data
public class Authenticator {
    private String userName;

    private Date timestamp;
}
