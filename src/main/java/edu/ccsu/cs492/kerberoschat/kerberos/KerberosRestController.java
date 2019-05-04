package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.kerberos.authenticator.Authenticator;
import edu.ccsu.cs492.kerberoschat.kerberos.authenticator.SessionAuthenticator;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.MalformedTGTException;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketGrantingTicket;
import edu.ccsu.cs492.kerberoschat.user.entity.AppUser;
import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
import edu.ccsu.cs492.kerberoschat.user.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

/**
 * Rest Controller related to Kerberos Authentication
 */
@RestController
@RequestMapping(value = "authentication/")
public class KerberosRestController {

    private final KerberosService kerberosService;

    private final AppUserService appUserService;

    @Autowired
    public KerberosRestController(KerberosService kerberosService, AppUserService appUserService) {
        this.kerberosService = kerberosService;
        this.appUserService = appUserService;
    }

    /**
     * Obtains and returns the salt for the specified user in order to derive an encryption key for authentication
     *
     * @param username the name of the user that's trying to authenticate
     * @return the salt of the user's password, if there is a user for the supplied name
     */
    @RequestMapping(value = "getSalt", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Map<String, String>> getSalt(@RequestBody String username) {
        try {
            String salt = kerberosService.getUserSalt(username);
            return new ResponseEntity<>(Collections.singletonMap("salt", salt), HttpStatus.OK);
        } catch (AppUserNotFoundException e) { // Bad request status returned to not indicate if there is a user for that name
            return new ResponseEntity<>(Collections.singletonMap("reason", "bad request"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Starts the authentication process for Kerberos. If its successful, a user will recieve a new SessionAuthenticator
     * with their TicketGrantingTicket and the session key they will be using with the KDC
     *
     * @param authenticator the user's authenticator
     * @return a response containing a session authenticator if successful, a response indicating failure otherwise
     */
    @RequestMapping(value = "authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@RequestBody Authenticator authenticator) {
        try {
            AppUser user = appUserService.getUser(authenticator.getUsername());
            if (kerberosService.isTimestampValid(authenticator.getTimestamp())) {
                SecretKey sessionKey = kerberosService.generateSessionKey(); // generate a new session key for the user
                String encodedKey = Base64.getEncoder().encodeToString(sessionKey.getEncoded()); // encode as base64
                TicketGrantingTicket TGT = kerberosService.createTGT(user, encodedKey); // create a TGT for the user
                SessionAuthenticator sessionAuthenticator = new SessionAuthenticator(encodedKey, TGT); // package TGT
                return new ResponseEntity<>(sessionAuthenticator, HttpStatus.OK); // send response
            }
            else { // timestamp is invalid or expired
                return new ResponseEntity<>(Collections.singletonMap("reason", "Invalid Timestamp"), HttpStatus.UNAUTHORIZED);
            }
        } catch (AppUserNotFoundException | MalformedTGTException e) { // error building a tgt or no use was found in the database
            return new ResponseEntity<>(Collections.singletonMap("reason", "Failure to Authenticate"), HttpStatus.UNAUTHORIZED);
        }
    }
}
