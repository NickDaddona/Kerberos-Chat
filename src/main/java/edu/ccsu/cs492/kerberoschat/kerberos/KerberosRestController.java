package edu.ccsu.cs492.kerberoschat.kerberos;

import edu.ccsu.cs492.kerberoschat.user.exception.AppUserNotFoundException;
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

    @Autowired
    public KerberosRestController(KerberosService kerberosService) {
        this.kerberosService = kerberosService;
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
}
