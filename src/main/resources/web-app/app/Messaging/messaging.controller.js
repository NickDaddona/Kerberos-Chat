'use strict';

angular.module('messaging').controller('msgController', [
    'msgService', 'loginService', 'ticketService', '$scope',
    function (msgService, loginService, ticketService, $scope) {
        var Ss = null; // The reply from auth2 will saved here
        $scope.recipient = "";
        $scope.msgFlag = true; // Flag for ng-hide
        $scope.message = "";

        $scope.processMsgAuth = function () {
            msgService.getMsgAuthenticator($scope.recipient).then(function(authenticator) {
                msgService.sendMsgAuth(authenticator).then(function() {
                    $scope.msgFlag = false;
                })
            });
        };

        $scope.processCommsAuth = function () {
            msgService.getCommsAuthenticator(loginService.getUser(), ticketService.getTicketToUser(), $scope.message).then(function(msgPackage){
                msgService.sendCommsAuthenticator(msgPackage);
                $scope.commsFlag = false;
            });
        };

        $scope.getMsgs = function () {
            msgService.getMessages(ticketService.getTGT());
        };
    }
]);