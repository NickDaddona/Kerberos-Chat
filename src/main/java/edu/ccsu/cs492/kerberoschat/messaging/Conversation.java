package edu.ccsu.cs492.kerberoschat.messaging;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Conversation {

    /**
     * List of Messages in the conversation
     */
    @Getter
    private List<String> messages;

    /**
     * Encrypted TicketToUser they were sent with
     */
    @Setter
    @Getter
    private String ticketToUser;

    public Conversation(String ticketToUser) {
        this.messages = new ArrayList<>();
        this.ticketToUser = ticketToUser;
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
