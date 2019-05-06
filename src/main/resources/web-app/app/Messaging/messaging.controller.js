'use strict';

angular.module('messaging').controller('msgController', [
    'msgService', 'loginService', '$scope',
    function (msgService, loginService, $scope) {
        var Ss = null; // The reply from auth2 will saved here
        $scope.recipient = "";
        $scope.msgFlag = true; // Flag for ng-show

        $scope.processMsgAuth = function () {
            msgService.getMsgAuthenticator($scope.recipient).then(function(authenticator) {
                console.log(authenticator);
                msgService.sendMsgAuth(authenticator).then(function() {
                    $scope.msgFlag = false;
                })
            });
        };

        $scope.sendMsg = function (msg) {
            msgService.sendMsg(msg);
        };
    }
]);