'use strict';

angular.module('messaging').service('msgService', [function() {

    var msgAuthentication = null; // Will be the response for confirmation of messaging

    // The user makes a request to communicate with another user
    this.sendUandT = function(recipient, ticket) {
        // code will go here to send the request to communicate with another user
    }

    this.sendMSG = function() {
        // code will go here to send the message to the server
    }

}]);