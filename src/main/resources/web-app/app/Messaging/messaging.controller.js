'use strict';

angular.module('messaging').controller('msgController', ['msgService', 'loginService', '$scope', function(msgService, loginService, $scope) {

    var Ss = null; // The reply from auth2 will saved here
    var TGT = null; // Will hold TGT
    $scope.recipient = "";
    $scope.msgFlag = true; // Flag for ng-show

    $scope.processMsgAuth = function(user, TGT, key) {
        msgService.sendMsgAuth(getMsgAuthenticator($scope.recipient, TGT, key));
        $scope.msgFlag = false;
    }

    $scope.sendMsg = function(msg) {
        msgService.sendMsg(msg);
    }

    $scope.getSessionKey = function() {
        Ss = loginService.getSessionKey();
        return Ss;
    }

    $scope.getTGT = function() {
        TGT = loginService.getTGT();
        return TGT;
    }

}]);