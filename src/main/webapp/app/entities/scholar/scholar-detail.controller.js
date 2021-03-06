(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('ScholarDetailController', ScholarDetailController);

    ScholarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Scholar', 'PersonInCharge', 'ClassRoom'];

    function ScholarDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Scholar, PersonInCharge, ClassRoom) {
        var vm = this;
        vm.scholar = entity;
        
        var unsubscribe = $rootScope.$on('madrassaApp:scholarUpdate', function(event, result) {
            vm.scholar = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
