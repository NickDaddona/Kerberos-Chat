'use strict';

angular.module('ticket').service('ticketService', [function() {

    var userKey = null;
    var ticketGrantingTicket = null;
    var sessionKey = null;
    var ticketToUser = null;
    var commsKey = null;


    this.setUserKey = function(Uk) {
        userKey = Uk;
    }

    this.getUserKey = function() {
        return userKey;
    }
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

    this.setTicketToUser = function(TTU) {
        ticketToUser = TTU;
    };

    this.getTicketToUser = function() {
        return ticketToUser;
    };

    this.setCommsKey = function(Ck) {
        commsKey = Ck;
    };

    this.getCommsKey = function() {
        return commsKey;
    };

}]);