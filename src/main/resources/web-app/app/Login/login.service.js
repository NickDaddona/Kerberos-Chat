'use strict';

angular.module('login').service('loginService', [function() {

    //TODO: Need to receive salt from the server


    //TODO: Wrap this function with a promise
    this.passHash = function(password) { // Takes the password and hashes it
        var key = CryptoJS.PBKDF2(password, CryptoJS.enc.Hex.parse("d5a80aa436cd51dd"), {
            keySize: 256 / 32,
            iterations: 185000
        });

        var hexKey = key.toString(CryptoJS.enc.Hex);

        return hexKey;
    };

    this.Auth = function(key) { // Will generate the authenticators
        var timestamp = new Date().getTime();
        console.log(timestamp);
        var auth = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), key);
        var decrypted = CryptoJS.AES.decrypt(auth, key);
        console.log(decrypted.toString(CryptoJS.enc.Utf8));
        return auth;
    }

    this.sendAuth = function(auth) {
        //TODO:
        // Code will go here to take the authentication object and send it to the server
        // This should be able to handle the sending of all three auth objects
    }

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
    this.getTicket = function() {
        return Ticket;
    }

}]);