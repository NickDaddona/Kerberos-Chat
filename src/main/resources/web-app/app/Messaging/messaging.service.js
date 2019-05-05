'use strict';

angular.module('messaging').service('msgService', [function() {

    var msgAuthentication = null; // Will be the response for confirmation of messaging
    var ticketToUser = null; // Will hold the ticket to the user they want to communicate with
    var ourCommsKey = null; // Will hold Kab

    this.getMsgAuthenticator = function (user, TGT, key) { // Will generate the second authenticator
        var timestamp = new Date().getTime();
        var auth = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), key, {
            padding: CryptoJS.pad.Iso10126
        });
        return {
            user: user,
            TGT: TGT,
            timestamp: timestamp // TODO: return encrypted timestamp
        };
    };

    this.sendMsgAuth = function (authenticator) { // send the authenticator to get a ticket to user and Kab
        return $http({
            method: "POST",
            url: $location.$$absUrl + "authentication/connectToUser",
            data: authenticator
        }).then(function (response) { // TODO: Decrypt authenticator to extract TGT
            ourCommsKey = response.data.Kab;
            // Code will go here to decrypt the session key
            ticketToUser = response.data.ticketToUser;
            return $q.resolve(ticketToUser);
        });
    };

    this.sendMSG = function() {
        // code will go here to send the message to the server
    }

}]);