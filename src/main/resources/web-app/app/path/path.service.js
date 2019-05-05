angular.module('path').service('pathService', [
    '$location',
    function ($location) {
        var absPath = $location.$$absUrl;

        this.getRootPath = function () {
            return absPath;
        }
    }
]);