package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.kerberos.authenticator.Authenticator;
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
     * @param userName the name of the user that's trying to authenticate
     * @return the salt of the user's password, if there is a user for the supplied name
     */
    @RequestMapping(value = "getSalt", method = RequestMethod.POST)
    public ResponseEntity<String> getSalt(@RequestBody String userName) {
        try {
            String salt = kerberosService.getUserSalt(userName);
            return new ResponseEntity<>(salt, HttpStatus.OK);
        } catch (AppUserNotFoundException e) { // Bad request status returned to not indicate if there is a user for that name
            return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<String> getTimeStamp(@RequestBody Authenticator authenticator) {
        try {
            AppUser user = appUserService.getUser(authenticator.getUserName());
            if (kerberosService.isTimestampValid(authenticator.getTimestamp())) {
                return new ResponseEntity<>("Timestamp expired", HttpStatus.UNAUTHORIZED);
            }
            else {
                return new ResponseEntity<>("Authenticated", HttpStatus.OK);
            }
        } catch (AppUserNotFoundException e) {
            return new ResponseEntity<>("Failure to Authenticate", HttpStatus.UNAUTHORIZED);
        }
    }
}
