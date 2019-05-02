'use strict';

angular.module('login').controller('loginController', ['$scope', 'loginService', '$location', function($scope, loginService, $location) {

    //TODO: User cannot leave the login page until the TGT is sent and approved

    $scope.password = ""; // Stores the password

    $scope.user = ""; // This will save the user's name put into the username input box

    var TGT = undefined; // TGT object will be saved here

    // There is a better way to do this, find it; allow this does seem to work
    $scope.generateAuth = function(password) {
        loginService.Auth(loginService.passHash(password));
        //$location.path('/message');
    }

    // Will not work this way, need output from generate auth function
    $scope.sendAuth = function (auth) {
        loginService.sendAuth(auth);
    }

}]);