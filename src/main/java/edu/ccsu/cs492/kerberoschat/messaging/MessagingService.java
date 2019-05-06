package edu.ccsu.cs492.kerberoschat.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class MessagingService {

    private Map<String, Map<String, Conversation>> conversationMap;

    @Autowired
    public MessagingService() {
        conversationMap = new HashMap<>();
    }

    /**
     * Adds a message to a conversation
     * @param username the user that will be receiving messages
     * @param ticketToUser the ticket to said user
     * @param message the encrypted message
     */
    public void addMessage(String username, String ticketToUser, String message) {
        conversationMap.computeIfAbsent(username, k -> new HashMap<>());
        Map<String, Conversation> userMap = conversationMap.get(username);
        if (userMap.get(ticketToUser) == null) {
            userMap.put(ticketToUser, new Conversation(ticketToUser));
        }
        userMap.get(ticketToUser).addMessage(message); // add the message
    }

    /**
     * Gets and returns one of the user's conversations
     * @param username the username the messages were sent to
     * @return a Conversation sent to the user
     */
    public Conversation getConversation(String username) {
        if (conversationMap.get(username) == null || conversationMap.get(username).isEmpty()) {
            return null;
        }
        else {
            Iterator<Entry<String, Conversation>> conversationIterator = conversationMap.get(username).entrySet().iterator();
            Entry<String, Conversation> sendToUser = conversationIterator.next();
            conversationMap.get(username).remove(sendToUser.getKey()); // remove the conversation from the map
            return sendToUser.getValue();
        }
    }
}
