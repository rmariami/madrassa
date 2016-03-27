(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ScholarDetailController', ScholarDetailController);

    ScholarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Scholar', 'PersonInCharge'];

    function ScholarDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Scholar, PersonInCharge) {
        var vm = this;
        vm.scholar = entity;
        vm.load = function (id) {
            Scholar.get({id: id}, function(result) {
                vm.scholar = result;
            });
        };
        var unsubscribe = $rootScope.$on('madrassaApp:scholarUpdate', function(event, result) {
            vm.scholar = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
