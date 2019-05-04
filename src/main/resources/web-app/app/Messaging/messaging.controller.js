'use strict';

angular.module('messaging').controller('msgController', ['msgService', 'loginService', '$scope', function(msgService, loginService, $scope) {

    var Ticket = undefined;// The reply from auth2 will saved here
    $scope.msgFlag = true; // Flag for ng-if

    $scope.sendUandT = function(user, ticket) {
        msgService.sendUandT(user, ticket);
        $scope.msgFlag = false;
    }

    $scope.sendMsg = function(msg) {
        msgService.sendMsg(msg);
    }

    $scope.getTicket = function() {
        Ticket = loginService.getTicket();
        return Ticket;
    }

}]);