'use strict';

angular.module('login').service('loginService', [
    'cryptoService', 'ticketService', '$http', '$location', '$q',
    function (cryptoService, ticketService, $http, $location, $q) {

        this.passHash = function (password, salt) { // Takes the password and hashes it
            return $q.when(CryptoJS.PBKDF2(password, CryptoJS.enc.Hex.parse(salt.toString()), {
                keySize: 256 / 32,
                iterations: 185000
            }));
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
            return $q.when(CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), key, {
                iv: CryptoJS.lib.WordArray.random(16)
            })).then(function (ct) {
                return $q.resolve({
                    username: username,
                    timestamp: ct.iv.toString() + ct.ciphertext.toString()
                });
            });
        };

        this.sendAuth = function (authenticator, key) { // send the authenticator to get a TGT
            return $http({
                method: "POST",
                url: $location.$$absUrl + "authentication/authenticate",
                data: authenticator
            }).then(function (response) {
                cryptoService.decrypt(response.data.authenticator, key.toString(CryptoJS.enc.Hex)).then(function(authenticator) {
                    var auth = JSON.parse(authenticator.toString(CryptoJS.enc.Utf8));
                    ticketService.setSessionKey(auth.sessionKey);
                    ticketService.setTGT(auth.ticketGrantingTicket);
                });
                return $q.resolve();
            });
        };
    }
]);