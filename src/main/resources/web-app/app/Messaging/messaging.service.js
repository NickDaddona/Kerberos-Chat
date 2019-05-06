'use strict';

angular.module('messaging').service('msgService', [
    'cryptoService', 'pathService', 'ticketService', '$http', '$q',
    function (cryptoService, pathService, ticketService, $http, $q) {
        var userTicket = null; // Will hold the ticket to the user they want to communicate with
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
                    var authenticator = JSON.parse(plaintext.toString(CryptoJS.enc.Utf8));
                    ticketService.setCommsKey(authenticator.sessionKey);
                    ticketService.setTicketToUser(authenticator.ticketToUser);
                    return $q.resolve();
                });
            });
        };

        this.getCommsAuthenticator = function (username, ticket, msg) {
            return cryptoService.encrypt(msg, ticketService.getCommsKey()).then(function (ct) {
                var msgPackage = JSON.stringify({
                    recipient: username,
                    ticketToUser: ticket,
                    message: ct.iv.toString() + ct.ciphertext.toString()
                });
                return $q.resolve(msgPackage);
            });
        };

        this.sendCommsAuthenticator = function (msgPackage) {
            return $http({
                method: "POST",
                url: pathService.getRootPath() + "message/sendMessage",
                data: msgPackage
            }).then(function (response) {
                console.log(response.data);
            })
        };

        this.getMessages = function (TGT) {
            return $http({
                method: "POST",
                url: pathService.getRootPath() + "message/getMessages",
                data: TGT
            }).then(function (response) {
                console.log(response.data);
            })
        };
    }]
);