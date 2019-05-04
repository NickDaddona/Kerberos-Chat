'use strict';

angular.module('messaging').controller('msgController', ['msgService', 'loginService', function(msgService, loginService) {

    var Ticket = undefined;// The reply from auth2 will saved here

    $scope.sendMsg = function(msg) {
        msgService.sendMsg(msg);
    }

    $scope.getTicket = function() {
        Ticket = loginService.getTicket()
        return Ticket;
    }

}]);