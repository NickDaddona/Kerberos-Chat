'use strict';

angular.module('login').controller('loginController', ['$scope', 'loginService', '$location', function($scope, loginService, $location) {

    $scope.phrase = "";
    $scope.generateAuth = function(phrase) {
        loginService.passAuth(phrase);
        $location.path('/message');
    }

    $scope.generateAuth1 = function () {
        loginService.passAuth1();
    }

}]);