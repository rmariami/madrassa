(function() {
    'use strict';

    angular
        .module('madrassaApp')
        .controller('PersonInChargeDetailController', PersonInChargeDetailController);

    PersonInChargeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PersonInCharge', 'Scholar'];

    function PersonInChargeDetailController($scope, $rootScope, $stateParams, entity, PersonInCharge, Scholar) {
        var vm = this;
        vm.personInCharge = entity;
        
        var unsubscribe = $rootScope.$on('madrassaApp:personInChargeUpdate', function(event, result) {
            vm.personInCharge = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
