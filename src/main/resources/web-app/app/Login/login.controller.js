'use strict';

angular.module('login').controller('loginController', [
    '$scope', 'loginService', '$location',
    function ($scope, loginService, $location) {
        //TODO: User cannot leave the login page until the TGT is sent and approved
        $scope.password = ""; // Stores the password

        $scope.user = ""; // This will save the user's name put into the username input box

        // There is a better way to do this, find it; allow this does seem to work
        $scope.generateAuth = function (username, password) {
            loginService.getSalt(username).then(function (salt) { // get the user's salt
                loginService.passHash(password, salt).then(function (hash) { // derive the user's password
                    loginService.sendAuth(loginService.getAuthenticator(username, hash)).then(function() { // send a request for a TGT
                        $location.path("/message"); // authenticated, so redirect to messenger page
                    });
                });
            });
        };
    }
]);