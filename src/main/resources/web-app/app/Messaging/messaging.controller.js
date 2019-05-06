'use strict';

angular.module('messaging').controller('msgController', [
    'msgService', 'loginService', 'ticketService', '$scope',
    function (msgService, loginService, ticketService, $scope) {
        var Ss = null; // The reply from auth2 will saved here
        var recipient = null;
        $scope.recipient = "";
        $scope.msgFlag = true; // Flag for ng-hide
        $scope.message = "";

        $scope.processMsgAuth = function () {
            msgService.getMsgAuthenticator($scope.recipient).then(function (authenticator) {
                msgService.sendMsgAuth(authenticator).then(function () {
                    $scope.msgFlag = false;
                    recipient = $scope.recipient;
                })
            });
        };

        $scope.processCommsAuth = function () {
            msgService.getCommsAuthenticator(recipient, ticketService.getTicketToUser(), $scope.message).then(function (msgPackage) {
                msgService.sendCommsAuthenticator(msgPackage);
                $scope.commsFlag = false;
            });
        };

        $scope.getMsgs = function () {
            msgService.getMessages(ticketService.getTGT()).then(function (res) {
                console.log(res);
                $scope.sender = res.ticketToMe.username;
                $scope.msgs = res.messages;
            });
        };
    }
]);