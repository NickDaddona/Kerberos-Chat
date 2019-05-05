'use strict';

angular.module('crypto').service('cryptoService', [
    '$q',
    function ($q) {
        this.encrypt = function (plaintext, key) {
            return $q.when(CryptoJS.AES.encrypt(CryptoJS.enc.Utf8.parse(plaintext), CryptoJS.enc.Hex.parse(key), {
                iv: CryptoJS.lib.WordArray.random(16),
                mode: CryptoJS.mode.CBC
            }));
        };

        this.decrypt = function (cipherTextandIV, key) {
            var iv = cipherTextandIV.substr(0, 32);
            var cipherText = cipherTextandIV.substr(32);
            return $q.when(CryptoJS.AES.decrypt(CryptoJS.lib.CipherParams.create({
                ciphertext: CryptoJS.enc.Hex.parse(cipherText)
            }), CryptoJS.enc.Hex.parse(key), {
                iv: CryptoJS.enc.Hex.parse(iv),
                mode: CryptoJS.mode.CBC
            }));
        };
    }
]);
