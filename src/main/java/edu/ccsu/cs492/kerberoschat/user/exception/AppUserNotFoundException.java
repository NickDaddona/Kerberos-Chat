package edu.ccsu.cs492.kerberoschat.user.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppUserNotFoundException extends Exception {

    public AppUserNotFoundException(String message) {
        super(message);
    }
}
