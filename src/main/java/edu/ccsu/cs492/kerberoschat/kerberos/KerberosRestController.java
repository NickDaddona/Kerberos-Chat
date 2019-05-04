package edu.ccsu.cs492.kerberoschat.kerberos;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ccsu.cs492.kerberoschat.kerberos.authenticator.Authenticator;
import edu.ccsu.cs492.kerberoschat.kerberos.authenticator.SessionAuthenticator;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.ChatTicket;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.MalformedTGTException;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketGrantingTicket;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketToUser;
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

    private final ObjectMapper objectMapper;

    @Autowired
    public KerberosRestController(KerberosService kerberosService, AppUserService appUserService, ObjectMapper objectMapper) {
        this.kerberosService = kerberosService;
        this.appUserService = appUserService;
        this.objectMapper = objectMapper;
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

    /**
     * Uses a previously created TGT and authenticator in order to build a ticket to a user that can be used to talk to another user
     *
     * @param payload the JSON payload that will be decrypted before operating on
     * @return a ticket to a user, encrypted with the TGT's session key, if the process is successful
     */
    @RequestMapping(value = "connectToUser", method = RequestMethod.POST)
    public ResponseEntity<?> getTicketToUser(@RequestBody Map<String, Object> payload) {
        Authenticator authenticator = objectMapper.convertValue(payload.get("authenticator"), Authenticator.class); //Convert JSON portion to Objects // TODO: will have to decrypt JSON received
        TicketGrantingTicket TGT = objectMapper.convertValue(payload.get("ticketGrantingTicket"), TicketGrantingTicket.class);
        if (kerberosService.isTimestampValid(authenticator.getTimestamp()) && kerberosService.isTGTValid(TGT)) { // continue if the TGT and timestamps are valid
            try {
                AppUser receiver = appUserService.getUser(authenticator.getUsername());
                String sessionKey = Base64.getEncoder().encodeToString(kerberosService.generateSessionKey().getEncoded());
                TicketToUser ticketToUser = new TicketToUser(receiver.getUserName(), sessionKey);
                ChatTicket chatTicket = new ChatTicket(TGT.getUsername(), sessionKey, ticketToUser);
                return new ResponseEntity<>(Collections.singletonMap("chatTicket", chatTicket), HttpStatus.OK);
            } catch (AppUserNotFoundException e) { // the user the authenticated user wants to connect to doesn't exist
                return new ResponseEntity<>(Collections.singletonMap("reason", "No User exists with this name"), HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<>(Collections.singletonMap("reason", "Invalid Timestamp"), HttpStatus.UNAUTHORIZED);
        }
    }
}
