'use strict';

angular.module('login').service('loginService', [
    '$http', '$location', '$q',
    function ($http, $location, $q) {
        var ticketGrantingTicket = null; // the ticket granting ticket the user will use to communicate with the server

        this.passHash = function (password, salt) { // Takes the password and hashes it
            return $q.when(CryptoJS.PBKDF2(password, CryptoJS.enc.Hex.parse(salt.toString()), {
                keySize: 256 / 32,
                iterations: 185000
            })).then(function (hash) { // return the key as a hex string
                return $q.resolve(hash.toString(CryptoJS.enc.Hex));
            });
        };

        this.getSalt = function (username) {
            return $http({
                method: "POST",
                url: $location.$$absUrl + "authentication/getSalt",
                data: username
            }).then(function (response) { // resolve the salt if successful
                return $q.resolve(response.data.salt);
            });
        };

        this.getAuthenticator = function (username, key) { // Will generate the authenticators
            var timestamp = new Date().getTime();
            var auth = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), key);
            var decrypted = CryptoJS.AES.decrypt(auth, key);
            console.log(decrypted.toString(CryptoJS.enc.Utf8));
            return {
                username: username,
                timestamp: timestamp // TODO: return encrypted timestamp
            };
        };

        this.sendAuth = function (authenticator) { // send the authenticator to get a TGT
            return $http({
                method: "POST",
                url: $location.$$absUrl + "authentication/authenticate",
                data: authenticator
            }).then(function (response) { // TODO: Decrypt authenticator to extract TGT
                ticketGrantingTicket = response.data.ticketGrantingTicket;
                return $q.resolve(ticketGrantingTicket);
            });
        };

        // Saving this code for later reference
        /*
        this.passAuth1 = function() {
            var timestamp = new Date().getTime();
            console.log(timestamp);
            console.log(CryptoJS.enc.Utf8.parse(timestamp));
            var encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), "Secret Passphrase");
            var decrypted = CryptoJS.AES.decrypt(encrypted, "Secret Passphrase");
            console.log(decrypted.toString(CryptoJS.enc.Utf8));
        }
        */

        // This will be used to pass the ticket to the messaging controller
        this.getTicket = function () {
            return ticketGrantingTicket;
        };
    }
]);