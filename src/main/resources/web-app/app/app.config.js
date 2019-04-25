'use strict';

angular.module('app').config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider){
    $routeProvider.when("/", {
        controller: "loginController",
        templateUrl: "Login/Login.html"
    });
    $locationProvider.html5Mode(true);
}]);