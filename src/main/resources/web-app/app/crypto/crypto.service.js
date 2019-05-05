'use strict';

angular.module('crypto').service('cryptoService', [
    '$q',
    function ($q) {
        var sessionKey = null;

        this.encrypt = function (plaintext, key) {
            console.log(key);
            return $q.when(CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(plaintext), CryptoJS.enc.Hex.parse(key), {
                iv: CryptoJS.lib.WordArray.random(16),
                mode: CryptoJS.mode.CBC
            }));
        };

        this.decrypt = function (cipherTextandIV, key) {
            console.log(cipherTextandIV);
            var iv = cipherTextandIV.substr(0, 32);
            var cipherText = cipherTextandIV.substr(32);
            console.log(iv);
            console.log(cipherText);
            return $q.when(CryptoJS.AES.decrypt(CryptoJS.enc.Hex.parse(cipherText), CryptoJS.enc.Hex.parse(key), {
                iv: CryptoJS.enc.Hex.parse(iv),
                mode: CryptoJS.mode.CBC
            })).then(function(pt) {
                console.log(CryptoJS.enc.Utf8.stringify(pt));
            });
        };
    }
]);
