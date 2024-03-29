'use strict';

angular.module('login').controller('loginController', [
    'loginService', 'ticketService', '$location', '$scope',
    function (loginService, ticketService, $location, $scope) {
        $scope.password = ""; // Stores the password
        $scope.username = ""; // This will save the user's name put into the username input box

        /**
         * Attempts to authenticate the user with the KDC, based on a set of promises provided by the loginService
         * @param {string} username the username of the user
         * @param {string} password the password of the user
         */
        $scope.processAuth = function (username, password) {
            loginService.getSalt(username).then(function (salt) { // get the user's salt
                loginService.passHash(password, salt).then(function (hash) { // derive the user's password
                    loginService.getAuthenticator(username, hash).then(function (authenticator) {
                        loginService.sendAuth(authenticator, hash).then(function () { // send a request for a TGT
                            loginService.setUser($scope.username);
                            ticketService.setUserKey(hash.toString(CryptoJS.enc.Hex));
                            $location.path("/message"); // authenticated, so redirect to messenger page
                        });
                    });
                });
            });
        };

    }
]);