package edu.ccsu.cs492.kerberoschat.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ccsu.cs492.kerberoschat.kerberos.service.CryptoService;
import edu.ccsu.cs492.kerberoschat.kerberos.service.KerberosService;
import edu.ccsu.cs492.kerberoschat.kerberos.ticket.TicketGrantingTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "message/")
public class MessagingRestController {

    private final CryptoService cryptoService;
    private final KerberosService kerberosService;
    private final MessagingService messagingService;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessagingRestController(CryptoService cryptoService, KerberosService kerberosService, MessagingService messagingService, ObjectMapper objectMapper) {
        this.cryptoService = cryptoService;
        this.kerberosService = kerberosService;
        this.messagingService = messagingService;
        this.objectMapper = objectMapper;
    }

    /**
     * Controller method for sending a message to another user
     *
     * @param payload the json payload to complete the request
     * @return a response entity that determines if the process succeeded or not
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> payload) {
        String recipient = payload.get("recipient");
        String ttuCipherText = payload.get("ticketToUser");
        String message = payload.get("message");
        messagingService.addMessage(recipient, ttuCipherText, message);
        return new ResponseEntity<>(Collections.singletonMap("message", "Message recorded"), HttpStatus.OK);
    }

    /**
     * Controller Method for getting a user's messages
     *
     * @param payload the json payload containing a user's TGT
     * @return a response entity with a conversation if the user has one
     */
    @RequestMapping(value = "getMessages", method = RequestMethod.POST)
    public ResponseEntity<?> getConversation(@RequestBody Map<String, String> payload) throws IOException {
        String encryptedTGT = payload.get("ticketGrantingTicket"); // get the tgt ciphertext
        TicketGrantingTicket TGT = objectMapper.readValue(cryptoService.decryptAESKDC(encryptedTGT), TicketGrantingTicket.class); // decrypt TGT
        if (kerberosService.isTGTValid(TGT)) {
            Conversation conversation = messagingService.getConversation(TGT.getUsername());
            if (conversation != null) {
                return new ResponseEntity<>(conversation, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(Collections.singletonMap("message", "no conversation available"), HttpStatus.NOT_FOUND);
            }
        }
        else {
            return new ResponseEntity<>(Collections.singletonMap("message", "TGT not valid"), HttpStatus.UNAUTHORIZED);
        }
    }
}
