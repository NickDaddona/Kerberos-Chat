'use strict';

angular.module('messaging').service('msgService', [
    'cryptoService', 'pathService', 'ticketService', '$http', '$q',
    function (cryptoService, pathService, ticketService, $http, $q) {
        var ticketToUser = null; // Will hold the ticket to the user they want to communicate with
        var ourCommsKey = null; // Will hold Kab

        this.getMsgAuthenticator = function (user) { // Will generate the second authenticator
            var authenticator = JSON.stringify({
                username: user,
                timestamp: new Date().getTime()
            });
            return cryptoService.encrypt(authenticator, ticketService.getSessionKey()).then(function (ct) {
                return $q.resolve({ // resolve the completed authenticator
                    authenticator: ct.iv.toString() + ct.ciphertext.toString(),
                    ticketGrantingTicket: ticketService.getTGT()
                });
            });
        };

        this.sendMsgAuth = function (authenticator) { // send the authenticator to get a ticket to user and Kab
            return $http({
                method: "POST",
                url: pathService.getRootPath() + "authentication/connectToUser",
                data: authenticator
            }).then(function (response) { // TODO: Decrypt authenticator to extract TGT
                return cryptoService.decrypt(response.data.chatTicket, ticketService.getSessionKey()).then(function (plaintext) {
                    var authenticator = JSON.stringify(plaintext.toString(CryptoJS.enc.Utf8));
                    ourCommsKey = authenticator.sessionKey;
                    ticketToUser = authenticator.ticketToUser;
                    return $q.resolve();
                });
            });
        };

        this.sendMSG = function () {
            // code will go here to send the message to the server
        }
    }]
);