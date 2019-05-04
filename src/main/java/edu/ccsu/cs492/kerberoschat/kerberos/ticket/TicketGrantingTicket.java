package edu.ccsu.cs492.kerberoschat.kerberos.ticket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketGrantingTicket {
    /**
     * The name of the user the ticket was granted to
     */
    private String username;

    /**
     * The time the ticket was issued
     */
    private Date timeIssued;

    /**
     * The time the ticket is set to expire
     */
    private Date expiryTime;

    /**
     * How long the ticket is set to last
     */
    private long duration;

    /**
     * The session key for this ticket encoded as a Base64 string
     */
    private String sessionKey;

    @NoArgsConstructor()
    public static class TGTBuilder {
        private String username;
        private Date timeIssued;
        private Date expiryTime;
        private long duration;
        private String sessionKey;

        public TGTBuilder addUserName(String username) {
            this.username = username;
            return this;
        }

        public TGTBuilder addTimeIssued(Date timeIssued) {
            this.timeIssued = timeIssued;
            return this;
        }

        public TGTBuilder addExpiryTime(Date expiryTime) {
            this.expiryTime = expiryTime;
            return this;
        }

        public TGTBuilder addDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public TGTBuilder addSessionKey(String base64SessionKey) {
            this.sessionKey = base64SessionKey;
            return this;
        }

        /**
         * Checks to see if the builder is complete
         *
         * @return true if a TGT is ready to be built, false otherwise
         */
        public boolean isBuildComplete() {
            return username != null && timeIssued != null && expiryTime != null && duration != 0 && sessionKey != null;
        }

        /**
         * Builds and returns a new TicketGrantingTicket based on previously supplied data
         *
         * @return a new, fully build TicketGrantingTicket
         * @throws MalformedTGTException if required TGT fields are not initialized
         */
        public TicketGrantingTicket getBuiltTicket() throws MalformedTGTException {
            if (isBuildComplete()) {
                return new TicketGrantingTicket(username, timeIssued, expiryTime, duration, sessionKey);
            }
            else {
                throw new MalformedTGTException("TGT Builder Failed, required fields not initialized");
            }
        }
    }
}
