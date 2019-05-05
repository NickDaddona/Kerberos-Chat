'use strict';

angular.module('ticket').service('ticketService', [function() {

    var ticketGrantingTicket = null;
    var sessionKey = null;

    this.setTGT = function(TGT) {
        ticketGrantingTicket = TGT;
    };

    this.getTGT = function() {
        return ticketGrantingTicket;
    };

    this.setSessionKey = function(key) {
        sessionKey = key;
    };

    this.getSessionKey = function() {
        return sessionKey;
    };

}]);