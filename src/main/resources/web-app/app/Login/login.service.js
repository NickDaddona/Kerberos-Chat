'use strict';

angular.module('login').service('loginService', [function() {

    //TODO: Need to receive salt from the server

    //Takes the password and hashes it
    //TODO: Wrap this function with a promise
    this.passAuth = function(password) {
        var key = CryptoJS.PBKDF2(password, CryptoJS.enc.Hex.parse("d5a80aa436cd51dd"), {
            keySize: 256 / 32,
            iterations: 185000
        });

        var hexKey = key.toString(CryptoJS.enc.Hex);
        console.log(hexKey);

        return hexKey;
    };

    this.passAuth1 = function() {
        var timestamp = new Date().getTime();
        console.log(timestamp);
        console.log(CryptoJS.enc.Utf8.parse(timestamp));
        var encrypted = CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(timestamp), "Secret Passphrase");
        var decrypted = CryptoJS.AES.decrypt(encrypted, "Secret Passphrase");
        console.log(decrypted.toString(CryptoJS.enc.Utf8));
    }

}]);