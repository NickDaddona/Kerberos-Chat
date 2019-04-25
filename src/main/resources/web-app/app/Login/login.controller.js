'use strict';

angular.module('login').controller('loginController', ['$scope', 'loginService', function($scope, loginService) {

    $scope.phrase = "";
    $scope.generateAuth = function(phrase) {
        loginService.passAuth(phrase);
    }

}]);